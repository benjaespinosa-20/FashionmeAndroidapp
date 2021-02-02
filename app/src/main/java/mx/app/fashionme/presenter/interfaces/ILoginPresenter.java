package mx.app.fashionme.presenter.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by heriberto on 18/03/18.
 */

public interface ILoginPresenter {

    void start();

    void attemptLogin(String email, String password);

    TextView.OnEditorActionListener setOnEditorActionListenerLogin();

    View.OnClickListener setOnClickListenerLogin();

    View.OnClickListener setOnClickListenerForgot();
}
