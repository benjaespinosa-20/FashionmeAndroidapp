package mx.app.fashionme.presenter.interfaces;

import mx.app.fashionme.view.interfaces.IForgotPasswordView;

public interface IForgotPasswordPresenter {

    void recoverPassword(String email);

    void setView(IForgotPasswordView view);
}
