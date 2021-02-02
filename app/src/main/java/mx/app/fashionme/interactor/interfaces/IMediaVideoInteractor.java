package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Video;

public interface IMediaVideoInteractor {

    void getData(Context context, Activity activity, MediaVideoListener listener);

    interface MediaVideoListener {

        void onSuccessGetData(ArrayList<Video> dataVideo);
    }
}
