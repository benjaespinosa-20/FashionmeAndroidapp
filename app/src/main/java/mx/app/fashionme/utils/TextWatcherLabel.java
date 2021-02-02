package mx.app.fashionme.utils;

import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by heriberto on 18/03/18.
 */

public class TextWatcherLabel implements TextWatcher {

    private final TextInputLayout textInputLayout;

    public TextWatcherLabel(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        textInputLayout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
