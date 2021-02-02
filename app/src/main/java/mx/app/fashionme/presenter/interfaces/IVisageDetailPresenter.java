package mx.app.fashionme.presenter.interfaces;


import android.view.Menu;

import ss.com.bannerslider.event.OnSlideClickListener;

public interface IVisageDetailPresenter {

    void initializeSlider();

    void setData();

    void showItems(Menu menu);

    void startActivityVideosMedia();

    void startActivityImagesMedia();

    void startActivityCameraMakeup();

    OnSlideClickListener setSliderClick();
}
