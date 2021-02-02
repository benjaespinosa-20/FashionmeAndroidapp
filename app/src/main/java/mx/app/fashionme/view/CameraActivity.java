package mx.app.fashionme.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.CameraInteractor;
import mx.app.fashionme.presenter.CameraPresenter;
import mx.app.fashionme.presenter.interfaces.ICameraPresenter;
import mx.app.fashionme.view.interfaces.ICameraView;

public class CameraActivity extends AppCompatActivity implements ICameraView {

    @BindView(R.id.camera)
    CameraView camera;

    @BindView(R.id.type_filter)
    ImageView filter_image;

    // Custom View Dialog
    private ImageView imagePreview;

    private ICameraPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_camera);
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        ButterKnife.bind(this);

        CameraInteractor cameraInteractor = new CameraInteractor(getApplicationContext());
        new CameraPresenter(this, cameraInteractor, CameraActivity.this);

        camera.setLifecycleOwner(this);
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                presenter.onPicture(jpeg, camera);
            }
        });

        Intent params = getIntent();

        String type = params.getStringExtra("filter_type");
        if (type != null) {
            if (type.equals("face")) {
                //todo cambiar imagen por la de la cara
                filter_image.setVisibility(View.INVISIBLE);
                camera.toggleFacing();
            } else if (type.equals("other")) {
                filter_image.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.capturePhoto)
    public void capturePhoto() {
        presenter.capturePhoto(camera);
    }

    @OnClick(R.id.toggleCamera)
    public void toggleCamera() {
        presenter.toggleCamera(camera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(ICameraPresenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        } else {
            throw new RuntimeException("Presenter not be null");
        }
    }

    @Override
    public void showDialog(byte[] jpeg) {

        MaterialDialog dialog =
                new MaterialDialog.Builder(this)
                        .customView(R.layout.dialog_camera_preview, false)
                        .positiveText("Ok")
                        .negativeText("Cancelar")
                        .onPositive(
                                (dialog1, which) -> presenter.returnData(jpeg)
                        )
                        .build();
        dialog.getActionButton(DialogAction.POSITIVE);
        imagePreview = dialog.getCustomView().findViewById(R.id.image_camera_preview);

        if (jpeg != null) {
            imagePreview.setImageBitmap(BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length));
        }
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isStarted()) {
            camera.start();
        }
    }
}
