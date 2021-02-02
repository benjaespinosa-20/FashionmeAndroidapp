package mx.app.fashionme.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.ForgotPasswordInteractor;
import mx.app.fashionme.presenter.ForgotPasswordPresenter;
import mx.app.fashionme.presenter.interfaces.IForgotPasswordPresenter;
import mx.app.fashionme.utils.TextWatcherLabel;
import mx.app.fashionme.view.interfaces.IForgotPasswordView;

public class ForgotPasswordFragment extends DialogFragment implements IForgotPasswordView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.text_field_email)
    TextInputEditText textFieldEmail;
    @BindView(R.id.til_email_error)
    TextInputLayout tilEmailError;
    @BindView(R.id.btnRecover)
    Button btnRecover;
    Unbinder unbinder;

    private View root;
    private IForgotPasswordPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        presenter = new ForgotPasswordPresenter(new ForgotPasswordInteractor(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.forgot_password_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);

        textFieldEmail.addTextChangedListener(new TextWatcherLabel(tilEmailError));
        setupToolbar();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void sendEmail(String email) {
        presenter.recoverPassword(email);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {
        progress.setVisibility(show ? View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.btnRecover)
    public void clickRecover(View view){
        sendEmail(textFieldEmail.getText().toString());
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle(R.string.recover_password);
    }

}
