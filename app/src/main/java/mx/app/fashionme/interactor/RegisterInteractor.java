package mx.app.fashionme.interactor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IRegisterInteractor;
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
 * Created by heriberto on 19/03/18.
 */

public class RegisterInteractor implements IRegisterInteractor {

    private static final String REGEX_WORDS = "^[a-zA-ZáÁéÉíÍóÓúÚñÑüÜ\\s]+$";

    @Override
    public void signup(String name, String email, String password, String passwordConfirmation,
                       String gender, String country, String birthdayFinal, final Context context, final OnSignUpFinishedListener callback) {

        if (TextUtils.isEmpty(name)) {
            callback.onNameError(context.getString(R.string.error_field_required), true);
            return;
        }

        if (!isValidName(name)) {
            callback.onNameError(context.getString(R.string.invalid_name_characters), true);
            return;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            callback.onEmailError(true);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            callback.onPasswordError(context.getString(R.string.error_invalid_password), true);
            return;
        }

        if (password.length() < 8) {
            callback.onPasswordError(context.getString(R.string.error_password_unless_8), true);
            return;
        }

        if (TextUtils.isEmpty(passwordConfirmation) || !isConfirmationPassword(password, passwordConfirmation)) {
            callback.onPasswordConfirmationError(true);
            return;
        }

        if (gender == null) {
            callback.onGenderError(context.getString(R.string.not_genre_selected), true);
            return;
        }

        if (country == null) {
            callback.onCountryError(context.getString(R.string.not_country_selected), true);
            return;
        }

        if (birthdayFinal == null) {
            callback.onBirthdayError(true);
            return;
        }

        RestApiAdapter restApiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<BaseLogin<User>> userResponseCall = endpointsApi.register(new User(email, password, name, gender, passwordConfirmation, country, birthdayFinal));

        userResponseCall.enqueue(new Callback<BaseLogin<User>>() {
            @Override
            public void onResponse(Call<BaseLogin<User>> call, Response<BaseLogin<User>> response) {
                // Error process
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e("RegisterInteractor", e.getMessage());
                            callback.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError != null ? apiError.getError() : null);
                        return;
                    }
                }

                User userRegister = response.body().getData();
                userRegister.setToken(response.body().getAccess_token());
                SessionPrefs.get(context).saveUser(userRegister);
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<BaseLogin<User>> call, Throwable t) {
                Log.d("RegisterInteractor", "ERROR NULL");
                t.printStackTrace();
                callback.onError("Error de conexión, intenta más tarde");
            }
        });
    }

    @Override
    public void validateName(String name, Context context, OnSignUpFinishedListener callback) {

        if (name.equals("")) {
            callback.onNameError(context.getString(R.string.error_field_required), false);
        } else if (!isValidName(name))
            callback.onNameError(context.getString(R.string.invalid_name_characters), false);
    }

    @Override
    public void validateEmail(String email, Context context, OnSignUpFinishedListener callback) {
        if (TextUtils.isEmpty(email) || !isValidEmail(email))
            callback.onEmailError(false);
    }

    @Override
    public void validatePassword(int passwordLength, Context context, OnSignUpFinishedListener callback) {
        if (passwordLength < 8)
            callback.onPasswordError(context.getString(R.string.error_password_unless_8), false);
    }

    @Override
    public void validateConfirmationPassword(String password, String confirmationPassword, Context context, OnSignUpFinishedListener callback) {
        if (!isConfirmationPassword(password, confirmationPassword))
            callback.onPasswordConfirmationError(false);
    }

    @Override
    public void validateBirthday(String birthday, Context context, OnSignUpFinishedListener callback) {
        if (TextUtils.isEmpty(birthday))
            callback.onBirthdayError(false);
    }

    private boolean isValidName(String name) {
        return Pattern.compile(REGEX_WORDS).matcher(name.trim()).matches();
    }

    private boolean isValidEmail(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private boolean isConfirmationPassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
