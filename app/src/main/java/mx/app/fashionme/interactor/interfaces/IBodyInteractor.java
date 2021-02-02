package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.View;

import java.util.List;

import clarifai2.dto.prediction.Concept;

/**
 * Created by heriberto on 20/03/18.
 */

public interface IBodyInteractor {

    void sendToClarifai(Uri uri, Context context, View v, BodyListener listener);
    void sendToAPI(Context context, String photoPath, View view, BodyListener listener);
    byte[] retrieveSelectedImage(@NonNull Context context, Uri uri);
    void sendToFashion(String currentBody, Context context, View view, OnRegisterBodyFinished callback);
    void checkVersion(BodyListener listener);
    void checkPermissions(Activity activity, BodyListener listener);

    void checkShowExplanation(Activity activity, Context context, BodyListener listener);

    void setThumbnail(Context context, Intent data, Uri uri, BodyListener listener);

    void setThumbnailFromGallery(Activity activity, Uri uri, View v, BodyListener listener);

    String getRealPathFromURI(Context context, Uri uri);

    interface OnRegisterBodyFinished {
        void onSuccessRegister();
        void onErrorRegister(View v, String error);
    }

    interface BodyListener {

        void onSendingRequest(View v);
        void onSuccess(List<Concept> body, View v);

        void onCheckVersion(boolean isNewVersion);
        void onCheckPermissions(boolean showExplication);
        void onCheckPermissions();

        void onCheckExplanation();
        
        void onError(View v, String errorMsg);

        void onGetThumbnail(Bitmap thumbnail);
        void onGetThumbnail(Uri uri);

        void onGetBodyType(String responseString, View v);
        void onGetBodyType();
    }
}
