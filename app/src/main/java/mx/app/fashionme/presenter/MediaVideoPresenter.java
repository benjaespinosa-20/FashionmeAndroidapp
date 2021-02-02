package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IMediaVideoInteractor;
import mx.app.fashionme.pojo.Video;
import mx.app.fashionme.presenter.interfaces.IMediaVideoPresenter;
import mx.app.fashionme.view.MediaVideoActivity;
import mx.app.fashionme.view.interfaces.IMediaVideoView;

public class MediaVideoPresenter implements IMediaVideoPresenter, IMediaVideoInteractor.MediaVideoListener {

    private static final String TAG = "MediaVideo";

    private Context context;
    private Activity activity;
    private IMediaVideoView view;
    private IMediaVideoInteractor interactor;

    public MediaVideoPresenter(Context context, Activity activity,
                               IMediaVideoView view, IMediaVideoInteractor interactor) {
        this.context    = context;
        this.activity   = activity;
        this.view       = view;
        this.interactor = interactor;
    }

    @Override
    public void getMedia() {
        if (view!=null) {
            view.showProgress();
            view.hideLinearLayoutError();
            if (!isOnline()) {
                view.hideProgress();
                view.hideRecyclerView();
                view.showLinearLayoutError();
                view.offline();
            } else {
                view.showRecyclerView();
                interactor.getData(context, activity, this);
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        }
        return info != null && info.isConnected();
    }

    @Override
    public View.OnClickListener setOnClickListenerTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMedia();
            }
        };
    }

    @Override
    public void onSuccessGetData(ArrayList<Video> dataVideo) {
        if (view != null) {
            view.hideProgress();
            view.hideLinearLayoutError();

            if (dataVideo.size() == 0) {
                view.showLinearLayoutError();
                view.emptyList();
            }
            view.setItems(dataVideo, null);
        }
    }
}
