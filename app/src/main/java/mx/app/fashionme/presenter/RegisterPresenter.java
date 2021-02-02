package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IRegisterInteractor;
import mx.app.fashionme.presenter.interfaces.IRegisterPresenter;
import mx.app.fashionme.view.interfaces.IRegisterView;

/**
 * Created by heriberto on 19/03/18.
 */

public class RegisterPresenter implements IRegisterPresenter, IRegisterInteractor.OnSignUpFinishedListener {

    private IRegisterView view;
    private IRegisterInteractor interactor;
    private Context context;

    private String birthdayFinal;

    public RegisterPresenter(IRegisterView view, IRegisterInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void onFocusName(String name) {
        interactor.validateName(name, context, this);
    }

    @Override
    public void onFocusEmail(String email) {
        interactor.validateEmail(email, context, this);
    }

    @Override
    public void onFocusPassword(int passwordLength) {
        interactor.validatePassword(passwordLength, context, this);
    }

    @Override
    public void onFocusConfirmationPassword(String password, String confirmationPassword) {
        interactor.validateConfirmationPassword(password, confirmationPassword, context, this);
    }

    @Override
    public void onFocusBirthday(String birthdayLength) {
        interactor.validateBirthday(birthdayLength, context, this);
    }

    @Override
    public View.OnClickListener setClickListenrLogin() {
        IRegisterInteractor.OnSignUpFinishedListener listener = this;

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view != null) {

                    if (!isOnline()){
                        view.showRegisterError(context.getString(R.string.error_network), context);
                        return;
                    }
                    view.showLoadingIndicator(true);
                    interactor.signup(view.getName(), view.getEmail(),view.getPassword(),view.getPasswordConfirmation(),view.getGender(),
                            view.getCountry(), getBirthdayFinal(), context, listener);
                }
            }
        };
    }

    @Override
    public void setDate(String formatBirthday) {
        setBirthdayFinal(formatBirthday);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = null;
        if (cm != null) {
            active = cm.getActiveNetworkInfo();
        }

        return active != null && active.isConnected();
    }

    @Override
    public void onNameError(String error, boolean focus) {
        if (view != null) {
            if (focus){
                view.focusName();
                hideInput();
            }
            view.showNameError(error);
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onEmailError(boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusEmail();
                hideInput();
            }
            view.showEmailError();
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onPasswordError(String error, boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusPassword();
                hideInput();
            }
            view.showPasswordError(error);
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onPasswordConfirmationError(boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusPasswordConfirmation();
                hideInput();
            }
            view.showPasswordConfirmationError();
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onGenderError(String error, boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusGender();
                hideInput();
            }
            view.showGenderError(error);
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onCountryError(String error, boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusCountry();
                hideInput();
            }

            view.showCountryError(error);
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onBirthdayError(boolean focus) {
        if (view != null) {
            if (focus) {
                view.focusBirthday();
                hideInput();
            }
            view.showBirthdayError();
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.showRegisterError(error, context);
            view.showLoadingIndicator(false);
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.navigateToWelcomeScreen();
        }
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public String getBirthdayFinal() {
        return birthdayFinal;
    }

    public void setBirthdayFinal(String birthdayFinal) {
        this.birthdayFinal = birthdayFinal;
    }
}
