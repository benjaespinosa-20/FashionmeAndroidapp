package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Makeup;

public interface IMakeupView {

    void setRefeshingToSwipe(boolean refresh);

    void showProgress();

    void hideProgress();

    void showLinearLayoutError();

    void hideLinearLayoutError();

    void showRecyclerView();

    void hideRecyclerView();

    void offline();

    void emptyList();

    void error(String error);

    void setItems(ArrayList<Makeup> data, String typeMH);

    String getTypeMH();

    void setTypeMH(String typeMH);

}
