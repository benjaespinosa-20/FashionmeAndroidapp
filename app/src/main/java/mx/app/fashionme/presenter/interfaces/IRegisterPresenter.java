package mx.app.fashionme.presenter.interfaces;

import android.view.View;

/**
 * Created by heriberto on 19/03/18.
 */

public interface IRegisterPresenter {

    void onFocusName(String name);

    void onFocusEmail(String email);

    void onFocusPassword(int passwordLength);

    void onFocusConfirmationPassword(String password, String confirmationPassword);

    void onFocusBirthday(String birthday);

    View.OnClickListener setClickListenrLogin();

    void setDate(String formatBirthday);
}
