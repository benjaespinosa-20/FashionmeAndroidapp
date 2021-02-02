package mx.app.fashionme.presenter.interfaces;

import com.afollestad.materialdialogs.MaterialDialog;
import com.otaliastudios.cameraview.CameraView;

public interface ICameraPresenter {
    void start();

    void onPicture(byte[] jpeg, CameraView camera);

    void capturePhoto(CameraView camera);

    void toggleCamera(CameraView camera);

    void returnData(byte[] jpeg);
}
