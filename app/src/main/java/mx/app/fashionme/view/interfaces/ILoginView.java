package mx.app.fashionme.view.interfaces;

import android.content.Context;

import mx.app.fashionme.presenter.interfaces.ILoginPresenter;

/**
 * Created by heriberto on 15/03/18.
 */

public interface ILoginView {

    void attemptLogin();

    void setPresenter(ILoginPresenter presenter);

    void showLoadingIndicator(boolean show);

    void setEmailError(String error);

    void setPasswordError(String error);

    void showLoginError(String msg);

    void showHomeScreen();

    void showNetworkError();

    void forgotPassword();
}
