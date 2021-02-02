package mx.app.fashionme.view.interfaces;

import android.content.Context;

/**
 * Created by heriberto on 19/03/18.
 */

public interface IRegisterView {

    void showNameError(String error);

    void focusName();

    void showEmailError();

    void focusEmail();

    void showPasswordError(String s);

    void focusPassword();

    void showPasswordConfirmationError();

    void focusPasswordConfirmation();

    void showRegisterError(String error, Context context);

    void showGenderError(String error);

    void focusGender();

    void showCountryError(String error);

    void focusCountry();

    void showBirthdayError();

    void focusBirthday();

    void showLoadingIndicator(boolean show);

    void navigateToWelcomeScreen();

    String getName();

    String getEmail();

    String getPassword();

    String getPasswordConfirmation();

    String getGender();

    String getCountry();
}
