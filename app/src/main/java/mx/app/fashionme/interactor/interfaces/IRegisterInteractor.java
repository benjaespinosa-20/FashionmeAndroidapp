package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

/**
 * Created by heriberto on 19/03/18.
 */

public interface IRegisterInteractor {

    void signup(String name, String email, String password, String passwordConfirmation,
                String gender, String country, String birthdayFinal, Context context, OnSignUpFinishedListener callback);

    void validateName(String name, Context context, OnSignUpFinishedListener callback);

    void validateEmail(String email, Context context, OnSignUpFinishedListener callback);

    void validatePassword(int passwordLength, Context context, OnSignUpFinishedListener callback);

    void validateConfirmationPassword(String password, String confirmationPassword, Context context, OnSignUpFinishedListener callback);

    void validateBirthday(String birthday, Context context, OnSignUpFinishedListener callback);

    interface OnSignUpFinishedListener {
        void onNameError(String error, boolean focus);
        void onEmailError(boolean focus);
        void onPasswordError(String error, boolean focus);
        void onPasswordConfirmationError(boolean focus);
        void onGenderError(String error, boolean focus);
        void onCountryError(String error, boolean focus);
        void onBirthdayError(boolean focus);

        void onError(String error);
        void onSuccess();
    }
}
