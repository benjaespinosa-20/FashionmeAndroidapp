package mx.app.fashionme.presenter;

import mx.app.fashionme.interactor.interfaces.IForgotPasswordInteractor;
import mx.app.fashionme.presenter.interfaces.IForgotPasswordPresenter;
import mx.app.fashionme.view.interfaces.IForgotPasswordView;

public class ForgotPasswordPresenter implements IForgotPasswordPresenter, IForgotPasswordInteractor.ForgotPasswordListener {

    private IForgotPasswordView view;
    private IForgotPasswordInteractor interactor;

    public ForgotPasswordPresenter(IForgotPasswordInteractor interactor) {
        this.interactor = interactor;
    }


    @Override
    public void recoverPassword(String email) {
        if (view != null) {
            view.showProgress(true);
            interactor.recoverPassword(email, this);
        }
    }

    @Override
    public void setView(IForgotPasswordView view) {
        this.view = view;
    }

    @Override
    public void onNetworkConnectFailed(String message) {
        if (view != null) {
            view.showProgress(false);
            view.showMessage(message);
        }

    }

    @Override
    public void onEmailError(String error) {
        if (view != null) {
            view.showProgress(false);
            view.showMessage(error);
        }
    }

    @Override
    public void onSuccess(String message) {
        if (view != null) {
            view.showProgress(false);
            view.showMessage(message);
        }
    }

    @Override
    public void onFailed(String error) {
        if (view != null) {
            view.showProgress(false);
            view.showMessage(error);
        }

    }
}
