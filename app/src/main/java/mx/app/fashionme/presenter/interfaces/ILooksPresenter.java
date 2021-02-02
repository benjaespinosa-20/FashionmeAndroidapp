package mx.app.fashionme.presenter.interfaces;

import mx.app.fashionme.pojo.Look;

public interface ILooksPresenter {

    void getLooks();
    void saveDateLook(Look look, String date);

    void setAnalytics();
}
