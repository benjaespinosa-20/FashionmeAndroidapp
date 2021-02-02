package mx.app.fashionme.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IForgotPasswordInteractor;
import mx.app.fashionme.pojo.Forgot;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordInteractor implements IForgotPasswordInteractor {

    private final Context context;

    public ForgotPasswordInteractor(Context context) {
        this.context    = context;
    }

    @Override
    public void recoverPassword(String email, ForgotPasswordListener listener) {

        // Check email and check password
        boolean c1 = isValidEmail(email, listener);

        if (!c1) {
            return;
        }

        // Check network status
        if (!isNetworkAvailable()) {
            listener.onNetworkConnectFailed(context.getString(R.string.error_network));
            return;
        }

        // Authentication
        recover(email, listener);
    }

    private void recover(String email, ForgotPasswordListener listener) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        Call<Forgot> forgotCall = endpointsApi.recoverPassword(new Forgot(email));

        forgotCall.enqueue(new Callback<Forgot>() {
            @Override
            public void onResponse(Call<Forgot> call, Response<Forgot> response) {
                // Procesar errores
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
                        try {
                            listener.onFailed(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e("LoginInteractor", e.getMessage());
                            listener.onFailed("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onFailed(apiError != null ? apiError.getError() : null);
                        return;
                    }
                }

                if (response.body() != null) {
                    listener.onSuccess(response.body().getData());
                }

            }

            @Override
            public void onFailure(Call<Forgot> call, Throwable t) {
                Log.d("ForgotPassword", t.getMessage(), t);
                listener.onFailed("Algo salió mal, intenta más tarde.");
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager ccm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ccm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isValidEmail(String email, ForgotPasswordListener listener) {
        boolean isValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener.onEmailError(context.getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Mas reglas de negocio...

        return isValid;
    }
}
