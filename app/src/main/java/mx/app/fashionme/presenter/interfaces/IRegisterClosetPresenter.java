package mx.app.fashionme.presenter.interfaces;

import android.net.Uri;

import java.util.ArrayList;

import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.view.interfaces.IRegisterClosetView;

public interface IRegisterClosetPresenter {

    void setView(IRegisterClosetView view);

    void openBottomDialog();

    void setThumbnailFromGallery(Uri data);

    void setThumbnail(Uri uri);

    void sendData(String name, String photo,
                  ArrayList<DataRegisterClotheViewModel> subcategories, ArrayList<DataRegisterClotheViewModel> colors,
                  ArrayList<DataRegisterClotheViewModel> dressCodes, ArrayList<DataRegisterClotheViewModel> climates);
}
