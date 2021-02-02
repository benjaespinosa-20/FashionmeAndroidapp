package mx.app.fashionme.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

public interface FaceContract {

    interface FaceView {

        void showExplicationPermissions();
        void showNeverAskDialogPermissions();
        void setImageFaceThumbnail(Bitmap thumbnail);
        void setImageFaceThumbnail(Uri uri);

        void showProgress(boolean show);

        void openCamera();
        void openGallery();

        String getCurrentPhotoFacePath();
        void setCurrentPhotoFacePath(String currentPhotoFacePath);

        void showError(View view, String error);
    }

    interface FacePresenter {

        void checkPermissionStatus(boolean camera, boolean gallery);
        void showExplanationPermissions();
        void setThumbnail(Intent data);
        void setThumbnail(Uri photoURI);
        void sendPhoto(String currentPath, View v);
        void setThumbnailFromGallery(Uri uri, View v);

        void setAnalytics();
    }

    interface FaceInteractor {

        void checkVersion(FaceListener listener);
        void checkPermissions(Activity activity, FaceListener listener);
        void checkShowExplanation(Activity activity, Context context, FaceListener listener);
        void setThumbnail(Context context, Intent data, Uri uri, FaceListener listener);
        void setThumbnailFromGallery(Activity activity, Uri uri, View v, FaceListener listener);

        String getRealPathFromURI(Context context, Uri uri);

        void sendToAPI(String photoPath, Context context, View v, FaceListener listener);
        void sendToFashionMEServer(String face, Context context, View view, FaceListener listener);


        interface FaceListener {
            void onSendingRequest(View v);

            void onCheckVersion(boolean isNewVersion);
            void onCheckPermissions(boolean showExplication);
            void onCheckPermissions();
            void onCheckExplanation();

            void onGetThumbnail(Bitmap thumbnail, Uri uri);

            void onGetFaceType(String face, View v);

            void onSuccessRegister();
            void onError(View v, String error);

        }
    }

}
