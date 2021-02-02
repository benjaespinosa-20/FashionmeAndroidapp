package mx.app.fashionme.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import mx.app.fashionme.contract.FaceContract;
import mx.app.fashionme.view.FaceActivity;

public class FacePresenter implements FaceContract.FacePresenter, FaceContract.FaceInteractor.FaceListener {

    public static final String TAG = FacePresenter.class.getSimpleName();
    private FaceContract.FaceView view;
    private FaceContract.FaceInteractor interactor;
    private Context context;
    private Activity activity;
    private boolean isCameraPetition;
    private boolean isGalleryPetition;

    public FacePresenter(FaceContract.FaceView view, FaceContract.FaceInteractor interactor,
                         Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;

    }

    @Override
    public void checkPermissionStatus(boolean camera, boolean gallery) {
        this.isCameraPetition = camera;
        this.isGalleryPetition = gallery;
        interactor.checkVersion(this);

    }

    @Override
    public void showExplanationPermissions() {
        interactor.checkShowExplanation(activity, context, this);
    }

    @Override
    public void setThumbnail(Intent data) {
        interactor.setThumbnail(context, data, null, this);
    }

    @Override
    public void setThumbnail(Uri photoURI) {
        interactor.setThumbnail(context, null, photoURI, this);
    }

    @Override
    public void sendPhoto(String currentPath, View v) {
        if (view != null) {
            view.showProgress(true);
        }

        interactor.sendToAPI(currentPath, context, v, this);
    }

    @Override
    public void setThumbnailFromGallery(Uri uri, View v) {
        interactor.setThumbnailFromGallery(activity, uri, v, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Rostro/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSendingRequest(View v) {

    }

    @Override
    public void onCheckVersion(boolean isNewVersion) {
        if (isNewVersion) {
            interactor.checkPermissions(activity, this);
        } else {
            if (view != null) {
                if (isCameraPetition) {
                    view.openCamera();
                } else if (isGalleryPetition) {
                    view.openCamera();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckPermissions(boolean showExplication) {
        if (showExplication) {
            if (view != null) {
                view.showExplicationPermissions();
            }
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, FaceActivity.REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onCheckPermissions() {
        if (view != null) {
            if (isCameraPetition) {
                view.openCamera();
            } else if (isGalleryPetition) {
                view.openGallery();
            }
        }
    }

    @Override
    public void onCheckExplanation() {
        if (view != null)
            view.showNeverAskDialogPermissions();
    }

    @Override
    public void onGetThumbnail(Bitmap thumbnail, Uri uri) {
        if (thumbnail == null) {
            if (view != null) {
                view.setImageFaceThumbnail(uri);
                if (view.getCurrentPhotoFacePath() == null){
                    view.setCurrentPhotoFacePath(interactor.getRealPathFromURI(context, uri));
                }
            }
        } else if (uri == null) {
            if (view != null)
                view.setImageFaceThumbnail(thumbnail);
        }
    }

    @Override
    public void onGetFaceType(String face, View v) {
        interactor.sendToFashionMEServer(face, context, v, this);
    }

    @Override
    public void onSuccessRegister() {
        //context.startActivity(new Intent(context, BuildingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }

    @Override
    public void onError(View v, String error) {
        if (view != null) {
            view.showProgress(false);
            view.showError(v, error);
        }
    }
}
