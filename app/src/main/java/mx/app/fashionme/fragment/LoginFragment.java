package mx.app.fashionme.fragment;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.LoginInteractor;
import mx.app.fashionme.presenter.interfaces.ILoginPresenter;
import mx.app.fashionme.presenter.LoginPresenter;
import mx.app.fashionme.utils.TextWatcherLabel;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.view.interfaces.ILoginView;


public class LoginFragment extends Fragment implements ILoginView {

    // Miembros UI
    @BindView(R.id.text_field_email)
    TextInputEditText email;

    @BindView(R.id.text_field_password)
    TextInputEditText password;

    @BindView(R.id.btnLogin)
    Button signInButton;

    @BindView(R.id.login_form)
    View loginForm;

    @BindView(R.id.login_progress)
    ProgressBar loginProgress;

    @BindView(R.id.til_email_error)
    TextInputLayout emailError;

    @BindView(R.id.til_password_error)
    TextInputLayout passwordError;

    @BindView(R.id.tv_forgot_password)
    TextView tvForgot;

    private static final String DIALOG_TAG = "dialog";
    private ForgotPasswordFragment fragment;

    private ILoginPresenter presenter;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        // Setup de argumentos en caso de que los haya
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Extracción de argumentos en caso de que los haya
        }
        setRetainInstance(true);

        LoginInteractor loginInteractor = new LoginInteractor(getContext());
        new LoginPresenter(this, loginInteractor, getContext());

        // Try to restore dialog fragment if we were showing it prior to screen rotation
        if (savedInstanceState != null) {
            fragment = (ForgotPasswordFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(DIALOG_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View uiRoot = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, uiRoot);

        email.addTextChangedListener(new TextWatcherLabel(emailError));
        password.addTextChangedListener(new TextWatcherLabel(passwordError));
        password.setOnEditorActionListener(presenter.setOnEditorActionListenerLogin());
        signInButton.setOnClickListener(presenter.setOnClickListenerLogin());
        tvForgot.setOnClickListener(presenter.setOnClickListenerForgot());

        return uiRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void attemptLogin() {
        presenter.attemptLogin(
                email.getText().toString(),
                password.getText().toString());

    }

    @Override
    public void setPresenter(ILoginPresenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        } else {
            throw new RuntimeException("El presenter no puede ser nulo");
        }
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        tvForgot.setVisibility(show ? View.GONE : View.VISIBLE);
        loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmailError(String error) {
        emailError.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        passwordError.setError(error);
    }

    @Override
    public void showLoginError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showHomeScreen() {
        startActivity(new Intent(getActivity(), HomeController.class));
        getActivity().finish();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(),
                getActivity().getResources().getString(R.string.error_network), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void forgotPassword() {
        if (fragment == null) {
            fragment = new ForgotPasswordFragment();
        }

        if (!isForgotPasswordFragmentShown()) {
            fragment.show(getActivity().getSupportFragmentManager(), DIALOG_TAG);
        }
    }

    private boolean isForgotPasswordFragmentShown() {
        return fragment != null && fragment.isVisible();
    }
}
