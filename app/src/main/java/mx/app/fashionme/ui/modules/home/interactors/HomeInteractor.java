package mx.app.fashionme.ui.modules.home.interactors;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import mx.app.fashionme.R;
import mx.app.fashionme.data.database.entities.ChatEntity;
import mx.app.fashionme.data.repositories.DataBaseRepository;
import mx.app.fashionme.domain.usecases.subscription.SubscriptionUseCase;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.home.contracts.HomeContracts;
import mx.app.fashionme.ui.modules.home.presenters.listeners.HomePresenterListener;
import mx.app.fashionme.ui.utils.Constants;
import mx.app.fashionme.view.StartActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 21/03/18.
 */

public class HomeInteractor implements HomeContracts.IHomeInteractor {

    /**
     * Listener para responder a presenter
     */
    private HomePresenterListener mListener = null;
    /**
     * Instancia del repositorio de base de datos
     */
    private DataBaseRepository mDataBaseRepository;
    /**
     * Instancia del agente de configuracion de facturación
     */
    private BillingAgentConfig mBillingConfig;
    /**
     * Instancia de subscription use case
     */
    private SubscriptionUseCase subscriptionUseCase;
    /**
     * Variable TAG
     */
    public static final String TAG = HomeInteractor.class.getSimpleName();
    /**
     * Metodo constructor
     */
    @Inject
    public HomeInteractor(DataBaseRepository dataBaseRepository, BillingAgentConfig billingAgentConfig, SubscriptionUseCase subscriptionUseCase) {
        this.mDataBaseRepository = dataBaseRepository;
        this.mBillingConfig = billingAgentConfig;
        this.subscriptionUseCase = subscriptionUseCase;
    }

    /**
     * Metodo para iniciar home interactor
     * @param listener para responder a presenter
     */
    @Override
    public void init(HomePresenterListener listener) {
        this.mListener = listener;
    }

    /*
    @Override
    public void checkConditions(Context context, final OnCheckedConditionsListener callback) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String photUri = sharedPref.getString("pref_photo", null);

        callback.onSuccessConditions(SessionPrefs.get(context).getUserName(), SessionPrefs.get(context).getEmail(), photUri);
    }
    */

    @Override
    public void checkLogin(Context context) {
        // no está logueado
        if (!SessionPrefs.get(context).isLoggedIn()) {
            mListener.onFailLogin();
            return;
        }
        mListener.onPassLogin(context);
    }

    @Override
    public void getUser(Context appContext) {
        int userid = SessionPrefs.get(appContext).getUserId();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        String photoUri = sp.getString("pref_photo", null);

        if (userid == 0) {
            mListener.onError(appContext.getString(R.string.algo_salio_mal));
            SessionPrefs.get(appContext).logOut(appContext);
            appContext.startActivity(new Intent(appContext, StartActivity.class));
            return;
        }

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        Call<Base<User>> call = endpointsApi.getUserById(userid);

        call.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        mListener.onError("Algo salio mal, intenta más tarde");
                        try {
                            Log.e(TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        mListener.onError(apiError.getError());
                        Log.e(TAG, apiError.getError());
                        return;
                    }
                }

                if (response.body() != null) {
                    mListener.onSuccessConditions(
                            response.body().getData().getName(),
                            response.body().getData().getEmail(),
                            photoUri,
                            response.body().getData().isTrial(),
                            response.body().getData().isPremium()
                    );
                } else {
                    mListener.onError("Error en la conexión");
                }
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, ">>>onFailure: ", t);
                t.printStackTrace();
                mListener.onError("Error en la conexión");
            }
        });
    }

    @Override
    public void openDialog(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        mListener.onSetDialog(dialog);
    }

    @Override
    public void getVersionName(Context appContext) {
        String versionName = "";

        try {
            versionName = appContext.getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            mListener.onGetVersionName(versionName);
        }
    }

    /**
     * Metodo para chatear con asesor
     */
    @Override
    public void chatWithConsultant() {
        List<ChatEntity> chatEntities = mDataBaseRepository.chatDao().loadAllValues();
        if (chatEntities.isEmpty()) {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.id = 1;
            chatEntity.boughtTimeInMillis = 0;
            chatEntity.remainingTime = 0;
            mDataBaseRepository.chatDao().insert(chatEntity);
        }

        long remainingTime = mDataBaseRepository.chatDao().getRemainingTime();
        if (remainingTime > 0) {
            mListener.onPendingChatTime();
        } else {
            List<Purchase> purchases = mBillingConfig.queryPurchases(BillingClient.SkuType.INAPP);
            if (purchases.size() == 0) {
                mListener.onPendingChatTimeFinished();
            } else {
                String sku = purchases.get(0).getSku();
                long time = 0;
                switch (sku) {
                    case Constants.FIVE_MINUTES_CHAT_SKU:
                        time = 5 * 60 * 1000;
                        break;

                    case Constants.TEN_MINUTES_CHAT_SKU:
                        time = 10 * 60 * 1000;
                        break;

                    default:
                        break;
                }
                ChatEntity chatEntity = new ChatEntity();
                chatEntity.id = 1;
                chatEntity.boughtTimeInMillis = time;
                chatEntity.remainingTime = time;
                mDataBaseRepository.chatDao().update(chatEntity);
                mBillingConfig.handlePurchases(purchases);
                mListener.onPendingChatTime();
            }
        }
    }

    /**
     * Metodo para validar la suscripcion
     *
     * @param isPremium valor de premium desde back
     */
    @Override
    public void getSubscription(boolean isPremium) {
        List<Purchase> purchases = mBillingConfig.queryPurchases(BillingClient.SkuType.SUBS);
        if (isPremium) {
            if (purchases.isEmpty()) {
                subscriptionUseCase
                        .onUpdated(() -> mListener.onNonSubscription())
                        .updateSubscription(false);
            } else {
                mListener.onSubscriptionActive();
            }
        } else {
            if (!purchases.isEmpty()) {
                subscriptionUseCase
                        .onUpdated(() -> mListener.onSubscriptionActive())
                        .updateSubscription(true);
            } else {
                mListener.onNonSubscription();
            }
        }
    }


    /*

    @Override
    public void checkColor(Context context, final OnCheckedConditionsListener callback) {


        int userId = SessionPrefs.get(context).getUserId();
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<User>> userCall = endpointsApi.getUserById(userId);

        userCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        callback.onError("Algo salio mal, intenta más tarde");
                        try {
                            Log.e(TAG,response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError.getError());
                        Log.e(TAG, apiError.getError());
                        return;
                    }
                }

                if (response.body().getData().getColor() == null) {
                    callback.onFailColor();
                    return;
                }

                Colorimetry colorimetry = response.body().getData().getColor().getData();

                String colorSpanish = colorimetry.getSpanish().getColor_name();
                String colorEnglish = colorimetry.getEnglish().getColor_name();

                SessionPrefs.get(context).saveColor(colorSpanish,
                        colorEnglish,
                        response.body().getData().getColor().getData().getUrlImage());

                callback.onPassColor();
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                t.printStackTrace();
                callback.onError("Error en la conexión");
            }
        });
    }

    @Override
    public void checkCharacteristics(Context context, final OnCheckedConditionsListener callback) {

        int userId = SessionPrefs.get(context).getUserId();
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<User>> userCall = endpointsApi.getUserById(userId);
        userCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        callback.onError("Algo salio mal, intenta más tarde");
                        try {
                            Log.e(TAG,response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError.getError());
                        Log.e(TAG, apiError.getError());
                        return;
                    }
                }

                if (response.body().getData().getCharacteristics() == null || response.body().getData().getCharacteristics().getData().size() == 0){
                    callback.onFailCharacteristics();
                    return;
                }

                callback.onPassCharacteristics();
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                t.printStackTrace();
                callback.onError("Error en la conexión");
            }
        });

    }

    @Override
    public boolean isShownResult(Context context) {
        return SessionPrefs.get(context).isShownResult();
    }

    @Override
    public void updateTokenDevice(Context context) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        requestUpdateToken(task.getResult().getToken(), context);
                    }
                });
    }

    private void requestUpdateToken(String token, Context context) {
        if (token != null && !SessionPrefs.get(context).isTokenAssociated()) {
            int userId = SessionPrefs.get(context).getUserId();
            DeviceToken deviceToken = new DeviceToken();
            deviceToken.setToken(token);

            RestApiAdapter apiAdapter = new RestApiAdapter();

            EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

            Call<Base<DeviceToken>> call = endpointsApi.updateTokenByUser(userId, deviceToken);

            call.enqueue(new Callback<Base<DeviceToken>>() {
                @Override
                public void onResponse(Call<Base<DeviceToken>> call, Response<Base<DeviceToken>> response) {
                    if (!response.isSuccessful()) {
                        if (response.errorBody().contentType().type().equals("text")) {
                            try {
                                Log.d(TAG, response.errorBody().string());
                                //Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                return;
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                                //Toast.makeText(context, "Algo salió mal", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            //Toast.makeText(context, apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde");
                            return;
                        }
                    }
                    SessionPrefs.get(context).saveTokenAssociated(true);
                }

                @Override
                public void onFailure(Call<Base<DeviceToken>> call, Throwable t) {
                    Log.e(TAG, t.getMessage(), t);
                    //t.printStackTrace();
                    //Toast.makeText(context, "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "No se pudo actualizar el token");
        }
    }
    */

}
