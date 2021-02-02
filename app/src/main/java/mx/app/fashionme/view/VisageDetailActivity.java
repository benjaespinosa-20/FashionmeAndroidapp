package mx.app.fashionme.view;


import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.interactor.VisageDetailInteractor;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.presenter.VisageDetailPresenter;
import mx.app.fashionme.presenter.interfaces.IVisageDetailPresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.interfaces.IVisageDetailView;
import ss.com.bannerslider.Slider;

public class VisageDetailActivity extends AppCompatActivity implements IVisageDetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image_scrolling_top)
    Slider image_scrolling_top;

    @BindView(R.id.tv_scrolling)
    TextView tv_scrolling;

    @BindView(R.id.tv_scrolling_title)
    TextView tv_scrolling_title;

    private Context context;
    private IVisageDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visage_detail);

        ButterKnife.bind(this);

        context = getApplicationContext();
        setUpToolbar();

        presenter = new VisageDetailPresenter(context, VisageDetailActivity.this, this, new VisageDetailInteractor());

        presenter.initializeSlider();
        presenter.setData();
        image_scrolling_top.setOnSlideClickListener(presenter.setSliderClick());

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    private void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void setAdapterToSlider(DressCodeSliderAdapter adapter) {
        image_scrolling_top.setAdapter(adapter);
    }

    @Override
    public void setTitle(String name) {
        tv_scrolling_title.setText(name);
    }

    @Override
    public void setDescription(String desc) {
        tv_scrolling.setText(desc);
    }

    @Override
    public void openFullscreenImage(ArrayList<ImageTrend> images, int position) {
        Intent intentFullscreen = new Intent(this, FullscreenActivity.class);
        intentFullscreen.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, images);
        intentFullscreen.putExtra(Constants.POSITION_IMAGE, position);
        startActivity(intentFullscreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_media, menu);
        presenter.showItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.option_images:
//                presenter.startActivityImagesMedia();
//                return true;
            case R.id.option_video:
                presenter.startActivityVideosMedia();
                return true;
            case R.id.option_makeup:
                presenter.startActivityCameraMakeup();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
