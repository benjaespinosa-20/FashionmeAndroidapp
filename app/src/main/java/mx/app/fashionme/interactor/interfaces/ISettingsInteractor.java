package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

public interface ISettingsInteractor {
    void checkVersion(SettingsListener listener);

    void checkPermissions(Activity activity, SettingsListener listener);

    void checkShowExplanation(Activity activity, Context context, SettingsListener listener);

    void setContentPhotoProfile(Activity activity, Uri uri, SettingsListener listener);

    interface SettingsListener {
        void onCheckVersion(boolean isNewVersion);
        void onCheckPermissions(boolean showExplication);
        void onCheckPermissions();
        void onCheckExplanation();
        void onGetPathImageProfile(String filepath);
    }
}
