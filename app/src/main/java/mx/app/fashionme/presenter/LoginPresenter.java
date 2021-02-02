package mx.app.fashionme.presenter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import mx.app.fashionme.interactor.interfaces.ILoginInteractor;
import mx.app.fashionme.presenter.interfaces.ILoginPresenter;
import mx.app.fashionme.view.interfaces.ILoginView;

/**
 * Created by heriberto on 18/03/18.
 */

public class LoginPresenter implements ILoginPresenter, ILoginInteractor.OnLoginFinishedListener {

    private final ILoginView iLoginView;
    private final ILoginInteractor iLoginInteractor;
    private final Context context;

    public LoginPresenter(@NonNull ILoginView iLoginView,
                          @NonNull ILoginInteractor iLoginInteractor,
                          Context context) {
        this.iLoginView         = iLoginView;
        this.iLoginInteractor   = iLoginInteractor;
        this.context            = context;
        iLoginView.setPresenter(this);
    }

    @Override
    public void start() {
        // Comprobar si el usuario está logueado
    }

    @Override
    public void attemptLogin(String email, String password) {
        iLoginView.showLoadingIndicator(true);
        iLoginInteractor.login(email, password, this);
    }

    @Override
    public TextView.OnEditorActionListener setOnEditorActionListenerLogin() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    iLoginView.attemptLogin();

                    // Ocultar teclado
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    procesado = true;
                }
                return procesado;
            }
        };
    }

    @Override
    public View.OnClickListener setOnClickListenerLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iLoginView.attemptLogin();
            }
        };
    }

    @Override
    public View.OnClickListener setOnClickListenerForgot() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iLoginView.forgotPassword();
            }
        };
    }

    @Override
    public void onEmailError(String error) {
        iLoginView.showLoadingIndicator(false);
        iLoginView.setEmailError(error);
    }

    @Override
    public void onPasswordError(String error) {
        iLoginView.showLoadingIndicator(false);
        iLoginView.setPasswordError(error);
    }

    @Override
    public void onNetworkConnectFailed() {
        iLoginView.showLoadingIndicator(false);
        iLoginView.showNetworkError();
    }

    @Override
    public void onAuthFailed(String msg) {
        iLoginView.showLoadingIndicator(false);
        iLoginView.showLoginError(msg);
    }

    @Override
    public void onAuthSuccess() {
        iLoginView.showHomeScreen();
    }

}
