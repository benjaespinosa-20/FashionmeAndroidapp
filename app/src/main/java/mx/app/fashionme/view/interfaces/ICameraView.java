package mx.app.fashionme.view.interfaces;

import mx.app.fashionme.presenter.interfaces.ICameraPresenter;

public interface ICameraView {

    void setPresenter(ICameraPresenter presenter);

    void showDialog(byte[] jpeg);
}
