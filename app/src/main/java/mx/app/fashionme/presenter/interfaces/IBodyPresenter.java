package mx.app.fashionme.presenter.interfaces;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * Created by heriberto on 20/03/18.
 */

public interface IBodyPresenter {

    void checkPermissionsStatus(boolean cameraPetition, boolean galleryPetition);

    void showExplanation();

    void setThumbnail(Intent data);

    void setThumbnail(Uri photoURI);

    void sendPhoto(String currentPhotoPath, View v);

    void setThumbnailFromGallery(Uri uri, View v);
}
