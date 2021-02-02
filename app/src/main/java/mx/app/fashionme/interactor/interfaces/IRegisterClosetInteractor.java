package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import mx.app.fashionme.pojo.DataRegisterClotheViewModel;

public interface IRegisterClosetInteractor {

    void setThumbnailFromGallery(Activity activity, Uri data, RegisterClosetListener listener);

    String getRealPathFromURI(Context context, Uri data);

    void setThumbnail(Context context, Uri uri, RegisterClosetListener listener);

    void register(Context context, String name, String photo, ArrayList<DataRegisterClotheViewModel> subcategories, ArrayList<DataRegisterClotheViewModel> colors, ArrayList<DataRegisterClotheViewModel> dressCodes, ArrayList<DataRegisterClotheViewModel> climates, RegisterClosetListener listener);

    interface RegisterClosetListener {

        void onError(String error);

        void onGetThumbnail(Uri data);

        void onErrorName(String error);

        void onSuccessUpload();
    }
}
