package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.ImageTrend;

public interface IStyleResultView {

    void setUpToolbarStyleResult();
    void setUpPresenterStyleResult();
    void getResult();
    void showLoading(boolean show);
    void showError(String err);
    void setTvResult(String styleResult);
    void setStyleImagesSlider(ArrayList<ImageTrend> images);
}
