package mx.app.fashionme.view.interfaces;

public interface IForgotPasswordView {

    void sendEmail(String email);

    void showMessage(String message);

    void showProgress(boolean show);
}
