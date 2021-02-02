package mx.app.fashionme.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.ILoginInteractor;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.BaseLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 18/03/18.
 */

public class LoginInteractor implements ILoginInteractor {

    private static final String TAG = "LoginInteractor";
    private final Context context;

    public LoginInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void login(String email, String password, OnLoginFinishedListener callback) {
        // Check email and check password
        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);

        if (!(c1 && c2)) {
            return;
        }

        // Check network status
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return;
        }

        // Authentication
        signInUser(email, password, callback);
    }

    private boolean isValidEmail(String email, OnLoginFinishedListener callback) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            callback.onEmailError(context.getString(R.string.error_field_required));
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError(context.getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Mas reglas de negocio...

        return isValid;
    }

    private boolean isValidPassword(String password, OnLoginFinishedListener callback) {
        boolean isValid = true;

        if (TextUtils.isEmpty(password)) {
            callback.onPasswordError(context.getString(R.string.error_field_required));
            isValid = false;
        }

        // Mas reglas de negocio...

        return isValid;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager ccm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ccm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void signInUser(String email, String password, final OnLoginFinishedListener callback) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        final Call<BaseLogin<User>> userResponseCall = endpointsApi.login(new User(email, password));

        userResponseCall.enqueue(new Callback<BaseLogin<User>>() {
            @Override
            public void onResponse(Call<BaseLogin<User>> call, Response<BaseLogin<User>> response) {
                // Procesar errores
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
                        try {
                            callback.onAuthFailed(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e("LoginInteractor", e.getMessage());
                            callback.onAuthFailed("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onAuthFailed(apiError != null ? apiError.getError() : null);
                        return;
                    }
                }

                User userLoggedIn = response.body().getData();
                userLoggedIn.setToken(response.body().getAccess_token());
                // Guardar usuario en preferencias
                SessionPrefs.get(context).saveUser(userLoggedIn);
                callback.onAuthSuccess();
            }

            @Override
            public void onFailure(Call<BaseLogin<User>> call, Throwable t) {
                Log.d("LoginInteractor", t.getMessage());
                t.printStackTrace();
                callback.onAuthFailed("Algo salió mal, intenta más tarde.");

            }
        });
    }
}
