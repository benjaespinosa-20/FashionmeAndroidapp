package mx.app.fashionme.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.utils.Constants;

public class FullscreenActivity extends AppCompatActivity {

    private ImageViewer.Builder<ImageTrend> imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        List<ImageTrend> images = getIntent().getParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR);
        imageViewer = new ImageViewer.Builder<>(this, images);

        imageViewer.setFormatter(new ImageViewer.Formatter<ImageTrend>() {
            @Override
            public String format(ImageTrend imageTrend) {
                return imageTrend.getUrlImage();
            }
        })
                .setStartPosition(getIntent().getIntExtra(Constants.POSITION_IMAGE, 0))
                .allowSwipeToDismiss(true)
                .hideStatusBar(false)
                .show();

        imageViewer.setOnDismissListener(new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {
                FullscreenActivity.this.finish();
            }
        });
    }
}
