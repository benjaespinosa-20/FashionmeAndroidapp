package mx.app.fashionme.presenter;


import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.Log;

import com.otaliastudios.cameraview.CameraView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.app.fashionme.interactor.interfaces.ICameraInteractor;
import mx.app.fashionme.presenter.interfaces.ICameraPresenter;
import mx.app.fashionme.view.interfaces.ICameraView;

public class CameraPresenter implements ICameraPresenter, ICameraInteractor.Callback {

    private static final String TAG = "CameraPresenter";
    private final ICameraView view;
    private final ICameraInteractor interactor;
    private final Activity activity;


    private boolean mCapturingPicture;

    public CameraPresenter(@NonNull ICameraView view,
                           @NonNull ICameraInteractor interactor,
                           Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.activity   = activity;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onPicture(byte[] jpeg, CameraView camera) {
        if (view != null) {
            view.showDialog(jpeg);
            mCapturingPicture = false;
        }
    }

    @Override
    public void capturePhoto(CameraView camera) {
        if (mCapturingPicture) return;
        mCapturingPicture = true;
        camera.capturePicture();
    }

    @Override
    public void toggleCamera(CameraView camera) {
        if (mCapturingPicture) return;
        camera.toggleFacing();
    }

    @Override
    public void returnData(byte[] jpeg) {
        //Toast.makeText(activity, "Return data", Toast.LENGTH_SHORT).show();
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e("blah3", "picture file was null!");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(jpeg);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "picture file not found!");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.e(TAG, "hooray " + pictureFile.toURI().getRawPath());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("image", pictureFile.toURI().getRawPath());
        activity.setResult(Activity.RESULT_OK, returnIntent);
        activity.finish();
    }

    private File getOutputMediaFile() {
        Log.e(TAG, "Get me the o/p file");
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "FashionMe");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("FashionMeApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        Log.e(TAG, "Before returning " + mediaFile.getAbsolutePath());

        return mediaFile;
    }
}
