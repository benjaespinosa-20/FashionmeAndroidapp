package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Url;
import mx.app.fashionme.pojo.Video;

public interface IMediaVideoView {

    void showProgress();

    void hideProgress();

    void showLinearLayoutError();

    void hideLinearLayoutError();

    void showRecyclerView();

    void hideRecyclerView();

    void offline();

    void emptyList();

    void error(String error);

    void setItems(ArrayList<Video> dataVideo, ArrayList<Url> dataUrl);
}
