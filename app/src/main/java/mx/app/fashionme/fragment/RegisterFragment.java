package mx.app.fashionme.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.RegisterInteractor;
import mx.app.fashionme.presenter.interfaces.IRegisterPresenter;
import mx.app.fashionme.presenter.RegisterPresenter;
import mx.app.fashionme.utils.TextWatcherLabel;
import mx.app.fashionme.view.WelcomeActivity;
import mx.app.fashionme.view.interfaces.IRegisterView;


public class RegisterFragment extends Fragment implements IRegisterView, View.OnFocusChangeListener {

    // Miembros UI
    @BindView(R.id.til_name)
    TextInputLayout tilName;

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @BindView(R.id.til_confirm_password)
    TextInputLayout tilConfirmPassword;

    @BindView(R.id.tiet_name)
    EditText etName;

    @BindView(R.id.tiet_email)
    EditText etEmail;

    @BindView(R.id.tiet_password)
    EditText etPassword;

    @BindView(R.id.tiet_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.btn_signup)
    Button btnSignup;

    @BindView(R.id.gender_radio_group)
    RadioGroup rgGender;

    @BindView(R.id.signup_progress)
    ProgressBar progressRegister;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.tiet_birthday)
    EditText etBirthday;

    private IRegisterPresenter presenter;

    private String gender;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View uiRoot = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, uiRoot);
        presenter = new RegisterPresenter(this, new RegisterInteractor(), getContext());

        // Watchers
        etName.addTextChangedListener(new TextWatcherLabel(tilName));
        etEmail.addTextChangedListener(new TextWatcherLabel(tilEmail));
        etPassword.addTextChangedListener(new TextWatcherLabel(tilPassword));
        etConfirmPassword.addTextChangedListener(new TextWatcherLabel(tilConfirmPassword));

        etName.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etConfirmPassword.setOnFocusChangeListener(this);
        etBirthday.setOnFocusChangeListener(this);

        btnSignup.setOnClickListener(presenter.setClickListenrLogin());

        return uiRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etBirthday.setKeyListener(null);
    }

    @Override
    public void showNameError(String error) {
        etName.setError(error);
    }

    @Override
    public void focusName() {
        etName.requestFocus();
    }

    @Override
    public void showEmailError() {
        etEmail.setError(getString(R.string.error_invalid_email));
    }

    @Override
    public void focusEmail() {
        etEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String s) {
        etPassword.setError(s);
    }

    @Override
    public void focusPassword() {
        etPassword.requestFocus();
    }

    @Override
    public void showPasswordConfirmationError() {
        etConfirmPassword.setError(getString(R.string.error_password_confirmation));
    }

    @Override
    public void focusPasswordConfirmation() {
        etConfirmPassword.requestFocus();
    }

    @Override
    public void showRegisterError(String error, Context context) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGenderError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void focusGender() {
        rgGender.requestFocus();
    }

    @Override
    public void showCountryError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void focusCountry() {
        ccp.requestFocus();
    }

    @Override
    public void showBirthdayError() {
        etBirthday.setError(getResources().getString(R.string.error_field_required));
    }

    @Override
    public void focusBirthday() {
        etBirthday.requestFocus();
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        progressRegister.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void navigateToWelcomeScreen() {
        startActivity(new Intent(getActivity(), WelcomeActivity.class));
        getActivity().finish();
    }

    @Override
    public String getName() {
        return etName.getText().toString();
    }

    @Override
    public String getEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public String getPasswordConfirmation() {
        return etConfirmPassword.getText().toString();
    }

    @Override
    public String getGender() {
        int selectedGender = rgGender.getCheckedRadioButtonId();
        if (selectedGender == R.id.female_radio_btn)
            gender = getString(R.string.form_register_gender_woman_value);
        else if (selectedGender == R.id.male_radio_btn)
            gender = getString(R.string.form_register_gender_man_value);

        return gender;
    }

    @Override
    public String getCountry() {
        return ccp.getSelectedCountryNameCode();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case  R.id.tiet_name:
                if (b){
                    etName.requestFocus();
                } else {
                    presenter.onFocusName(getName());
                }
                break;
            case R.id.tiet_email:
                if (b) {
                    etEmail.requestFocus();
                } else {
                    presenter.onFocusEmail(getEmail());
                }
                break;
            case R.id.tiet_password:
                if (b) {
                    etPassword.requestFocus();
                } else {
                    presenter.onFocusPassword(etPassword.getText().length());
                }
                break;
            case R.id.tiet_confirm_password:
                if (b) {
                    etConfirmPassword.requestFocus();
                } else {
                    presenter.onFocusConfirmationPassword(getPassword(), getPasswordConfirmation());
                }
                break;
        }
    }
    
    @OnClick(R.id.tiet_birthday)
    public void showCalendar() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                etBirthday.setText(date);

                String format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                presenter.setDate(sdf.format(calendar.getTime()));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
