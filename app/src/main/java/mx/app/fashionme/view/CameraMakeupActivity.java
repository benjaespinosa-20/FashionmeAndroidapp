package mx.app.fashionme.view;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Hdr;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;

public class CameraMakeupActivity extends AppCompatActivity {

    @BindView(R.id.camera_view)
    CameraView cameraView;

    @BindView(R.id.revertCameraButton)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_makeup);
        ButterKnife.bind(this);

        cameraView.setLifecycleOwner(this);
        setupCamera();
    }

    private void setupCamera() {

        cameraView.setHdr(Hdr.ON);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });
    }
}
