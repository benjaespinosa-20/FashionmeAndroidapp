package mx.app.fashionme.view.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import java.io.IOException;

/**
 * Created by heriberto on 20/03/18.
 */

public interface IBodyView {

    void showError(View view, String error);

    void navigateToTest(View v);

    void setUpPresenter();

    void openCamera();

    void openGallery();

    void showExplication();

    void showNeverAskDialog();

    void setImageBodyThumbnail(Bitmap thumbnail);

    void setImageBodyThumbnail(Uri uri);

    void showProgressDialog(boolean show);

    String getCurrentPhotoPath();

    void setCurrentPhotoPath(String currentPhotoPath);
}
