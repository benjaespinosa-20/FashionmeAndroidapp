package mx.app.fashionme.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.prediction.Concept;
import mx.app.fashionme.interactor.interfaces.IBodyInteractor;
import mx.app.fashionme.pojo.BodyType;
import mx.app.fashionme.presenter.interfaces.IBodyPresenter;
import mx.app.fashionme.view.BodyActivity;
import mx.app.fashionme.view.CharacteristicActivity;
import mx.app.fashionme.view.interfaces.IBodyView;

/**
 * Created by heriberto on 20/03/18.
 */

public class BodyPresenter implements IBodyPresenter, IBodyInteractor.OnRegisterBodyFinished, IBodyInteractor.BodyListener {

    public static final String TAG = BodyPresenter.class.getSimpleName();
    private IBodyView view;
    private IBodyInteractor interactor;
    private Context context;
    private Activity activity;
    private boolean isCameraPetition;
    private boolean isGalleryPetition;

    public BodyPresenter(IBodyView view, IBodyInteractor interactor, Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void checkPermissionsStatus(boolean cameraPetition, boolean galleryPetition) {
        this.isCameraPetition = cameraPetition;
        this.isGalleryPetition = galleryPetition;
        interactor.checkVersion(this);
    }

    @Override
    public void showExplanation() {
        interactor.checkShowExplanation(activity, context, this);
    }

    @Override
    public void setThumbnail(Intent data) {
        interactor.setThumbnail(context, data, null,this);
    }

    @Override
    public void setThumbnail(Uri photoURI) {
        interactor.setThumbnail(context, null, photoURI, this);
    }

    @Override
    public void sendPhoto(String currentPhotoPath, View v) {
        if (view != null) {
            view.showProgressDialog(true);
            interactor.sendToAPI(context, currentPhotoPath, v, this);
        }
    }

    @Override
    public void setThumbnailFromGallery(Uri uri, View v) {
        interactor.setThumbnailFromGallery(activity, uri, v, this);
    }

    @Override
    public void onSendingRequest(View v) {
        if (view != null){
            view.navigateToTest(v);
        }
    }

    @Override
    public void onSuccess(List<Concept> body, View v) {
        float lastValue = 0;
        BodyType currentBody = new BodyType();

        if (view != null){
            ArrayList<BodyType> types = new ArrayList<>();

            for (int i = 0; i < body.size(); i++) {
                BodyType bodyType = new BodyType();

                bodyType.setId(body.get(i).id());
                bodyType.setName(body.get(i).name());
                bodyType.setValue(body.get(i).value());

                types.add(bodyType);
            }
            for (BodyType type:types) {
                if (type.getValue() > lastValue){
                    lastValue = type.getValue();
                    currentBody = type;
                }
            }
            //interactor.sendToFashion(currentBody, context, v, this);
        }
    }

    @Override
    public void onSuccessRegister() {
        context.startActivity(new Intent(context, CharacteristicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finish();
    }

    @Override
    public void onErrorRegister(View v, String error) {
        if (view != null){
            view.showProgressDialog(false);
            view.showError(v, error);
        }
    }

    @Override
    public void onCheckVersion(boolean isNewVersion) {
        if (isNewVersion) {
            interactor.checkPermissions(activity, this);
        } else {
            if (isCameraPetition){
                view.openCamera();
            } else if (isGalleryPetition){
                view.openGallery();
            }
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
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, BodyActivity.REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onCheckPermissions() {
        if (view != null) {
            if (isCameraPetition){
                view.openCamera();
            } else if (isGalleryPetition){
                view.openGallery();
            }
        }
    }

    @Override
    public void onCheckExplanation() {
        if (view != null) {
            view.showNeverAskDialog();
        }
    }

    @Override
    public void onError(View v, String error) {
        if (view != null) {
            view.showProgressDialog(false);
            view.showError(v, error);
        }
    }

    @Override
    public void onGetThumbnail(Bitmap thumbnail) {
        if (view != null) {
            view.setImageBodyThumbnail(thumbnail);
        }
    }

    @Override
    public void onGetThumbnail(Uri uri) {
        if (view != null) {
            view.setImageBodyThumbnail(uri);
            if (view.getCurrentPhotoPath() == null){
                view.setCurrentPhotoPath(interactor.getRealPathFromURI(context, uri));
            }
        }
    }

    @Override
    public void onGetBodyType(String responseString, View v) {
        Log.i(TAG, "Body Type=" + responseString);
        interactor.sendToFashion(responseString, context, v, this);
    }

    @Override
    public void onGetBodyType() {
        context.startActivity(new Intent(context, CharacteristicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
