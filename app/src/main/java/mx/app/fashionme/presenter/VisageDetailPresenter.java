package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.interactor.interfaces.IVisageDetailInteractor;
import mx.app.fashionme.pojo.English;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.pojo.Spanish;
import mx.app.fashionme.presenter.interfaces.IVisageDetailPresenter;
import mx.app.fashionme.utils.PicassoImageLoadingService;
import mx.app.fashionme.view.CameraMakeupActivity;
import mx.app.fashionme.view.MediaImageActivity;
import mx.app.fashionme.view.MediaVideoActivity;
import mx.app.fashionme.view.interfaces.IVisageDetailView;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class VisageDetailPresenter implements IVisageDetailPresenter, IVisageDetailInteractor.VisageDetailListener {

    private Context context;
    private Activity activity;
    private IVisageDetailView view;
    private IVisageDetailInteractor interactor;

    public VisageDetailPresenter(Context context, Activity activity, IVisageDetailView view, IVisageDetailInteractor interactor) {
        this.context    = context;
        this.activity   = activity;
        this.view       = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeSlider() {
        Slider.init(new PicassoImageLoadingService(context));
        if (view != null) {
            ArrayList<ImageTrend> images = activity.getIntent().getParcelableArrayListExtra(VisagePresenter.IMAGES_VISAGE);
            interactor.getAdapter(images, this);
        }
    }

    @Override
    public void setData() {
        if (view != null) {

            Spanish spanish = activity.getIntent().getParcelableExtra(VisagePresenter.OBJ_SPANISH);
            English english = activity.getIntent().getParcelableExtra(VisagePresenter.OBJ_ENGLISH);

            switch (activity.getResources().getString(R.string.app_language)) {
                case "spanish":
                    view.setTitle(spanish.getName());
                    view.setDescription(spanish.getDesc());
                    break;
                case "english":
                    view.setTitle(english.getName());
                    view.setDescription(english.getDesc());
                    break;
                    default:
                        view.setTitle(spanish.getName());
                        view.setDescription(spanish.getDesc());
                        break;
            }
        }
    }

    @Override
    public void showItems(Menu menu) {
        if (view != null) {
            if (!activity.getIntent().getStringExtra(MakeupPresenter.TYPE_KEY).equals(MakeupPresenter.TYPE_VALUE)){
                //menu.removeItem(R.id.option_images);
                menu.removeItem(R.id.option_video);
            }

            if (activity.getIntent().getStringExtra(MakeupPresenter.TYPE_KEY_M) == null) {
                menu.removeItem(R.id.option_makeup);
            }
        }
    }

    @Override
    public void startActivityVideosMedia() {
        Intent intent = new Intent(activity, MediaVideoActivity.class);
        intent.putParcelableArrayListExtra(MakeupPresenter.URLS_VISAGE, activity.getIntent().getParcelableArrayListExtra(MakeupPresenter.URLS_VISAGE));
        intent.putParcelableArrayListExtra(MakeupPresenter.VIDEOS_VISAGE, activity.getIntent().getParcelableArrayListExtra(MakeupPresenter.VIDEOS_VISAGE));
        activity.startActivity(intent);
    }

    @Override
    public void startActivityImagesMedia() {
        Intent intent = new Intent(activity, MediaImageActivity.class);
        intent.putParcelableArrayListExtra(VisagePresenter.IMAGES_VISAGE, activity.getIntent().getParcelableArrayListExtra(MakeupPresenter.IMAGES_VISAGE));
        activity.startActivity(intent);
    }

    @Override
    public void startActivityCameraMakeup() {
        Intent intent = new Intent(activity, CameraMakeupActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public OnSlideClickListener setSliderClick() {
        ArrayList<ImageTrend> images = activity.getIntent().getParcelableArrayListExtra(VisagePresenter.IMAGES_VISAGE);
        return new OnSlideClickListener() {
            @Override
            public void onSlideClick(int position) {
                if (view != null) {
                    view.openFullscreenImage(images, position);
                }
            }
        };
    }

    @Override
    public void onGetAdapter(DressCodeSliderAdapter adapter) {
        if (view != null) {
            view.setAdapterToSlider(adapter);
        }
    }
}
