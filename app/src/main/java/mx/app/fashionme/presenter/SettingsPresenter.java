package mx.app.fashionme.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import mx.app.fashionme.view.interfaces.ISettingsView;
import mx.app.fashionme.settings.PhotoProfilePreference;
import mx.app.fashionme.interactor.interfaces.ISettingsInteractor;
import mx.app.fashionme.presenter.interfaces.ISettingsPresenter;

public class SettingsPresenter implements ISettingsPresenter, ISettingsInteractor.SettingsListener {

    private static final String TAG = SettingsPresenter.class.getSimpleName();

    private ISettingsView view;
    private ISettingsInteractor interactor;
    private Context context;
    private Activity activity;

    public SettingsPresenter(ISettingsView view, ISettingsInteractor interactor,
                             Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void checkPermissionConditions() {
        interactor.checkVersion(this);
    }

    @Override
    public void showExplanation() {
        interactor.checkShowExplanation(activity, context, this);
    }

    @Override
    public void setImageProfile(Uri uri) {
        interactor.setContentPhotoProfile(activity, uri, this);
    }

    @Override
    public void onCheckVersion(boolean isNewVersion) {
        if (isNewVersion) {
            interactor.checkPermissions(activity, this);
        } else {
            view.openGallery();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckPermissions(boolean showExplication) {
        if (showExplication) {
            if (view != null) {
                view.showExplication();
            }
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PhotoProfilePreference.REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onCheckPermissions() {
        view.openGallery();
    }

    @Override
    public void onCheckExplanation() {
        if (view != null) {
            view.showNeverAskDialog();
        }
    }

    @Override
    public void onGetPathImageProfile(String filepath) {
        if (view != null) {
            view.setPreferenceImageProfile(filepath);
        }
    }
}
