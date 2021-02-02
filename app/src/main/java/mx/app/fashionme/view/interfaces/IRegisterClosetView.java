package mx.app.fashionme.view.interfaces;

import android.net.Uri;

public interface IRegisterClosetView {

    void setCurrentPhotoPath(String currentPhotoPath);

    void showError(String error);

    void setImageThumbnail(Uri data);

    String getCurrentPhotoPath();

    void uploadProgress(boolean show);

    void showErrorName(String error);

    void goToCloset();
}
