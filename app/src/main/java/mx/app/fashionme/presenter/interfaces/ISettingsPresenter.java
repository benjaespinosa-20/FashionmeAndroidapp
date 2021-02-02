package mx.app.fashionme.presenter.interfaces;

import android.net.Uri;

public interface ISettingsPresenter {
    void checkPermissionConditions();

    void showExplanation();

    void setImageProfile(Uri uri);
}
