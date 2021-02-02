package mx.app.fashionme.interactor;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IMediaVideoInteractor;
import mx.app.fashionme.pojo.Url;
import mx.app.fashionme.pojo.Video;
import mx.app.fashionme.presenter.MakeupPresenter;

public class MediaVideoInteractor implements IMediaVideoInteractor {
    @Override
    public void getData(Context context, Activity activity, MediaVideoListener listener) {
        ArrayList<Video> videos = activity.getIntent().getParcelableArrayListExtra(MakeupPresenter.VIDEOS_VISAGE);

        if (videos != null) {
            listener.onSuccessGetData(videos);
        }

    }
}
