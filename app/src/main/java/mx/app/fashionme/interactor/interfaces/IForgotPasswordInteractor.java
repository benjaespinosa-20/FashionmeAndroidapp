package mx.app.fashionme.interactor.interfaces;

public interface IForgotPasswordInteractor {

    void recoverPassword(String email, ForgotPasswordListener listener);

    interface ForgotPasswordListener {

        void onNetworkConnectFailed(String message);

        void onEmailError(String error);

        void onSuccess(String message);

        void onFailed(String error);
    }

}
