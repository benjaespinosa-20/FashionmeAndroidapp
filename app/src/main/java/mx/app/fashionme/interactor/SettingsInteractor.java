package mx.app.fashionme.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;

import android.widget.Toast;

import mx.app.fashionme.interactor.interfaces.ISettingsInteractor;

public class SettingsInteractor implements ISettingsInteractor {
    private static final String TAG = SettingsInteractor.class.getSimpleName();

    @Override
    public void checkVersion(SettingsListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.onCheckVersion(true);
        } else {
            listener.onCheckVersion(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkPermissions(Activity activity, SettingsListener listener) {
        int statusPermission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (statusPermission != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                listener.onCheckPermissions(true);
            } else {
                listener.onCheckPermissions(false);
            }
        } else {
            listener.onCheckPermissions();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkShowExplanation(Activity activity, Context context, SettingsListener listener) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "Como denegaste el acceso al almacenamiento, no se mostrará ninguna foto.", Toast.LENGTH_LONG).show();
        } else {
            listener.onCheckExplanation();
        }
    }

    @Override
    public void setContentPhotoProfile(Activity activity, Uri uri, SettingsListener listener) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filepath = cursor.getString(columnIndex);
        cursor.close();

        listener.onGetPathImageProfile(filepath);
    }
}
