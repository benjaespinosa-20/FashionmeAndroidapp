package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Visage;

public interface IVisageView {

    void setRefreshingToSwipe(boolean refresh);

    void showProgress();

    void hideProgress();

    void showLinearLayoutError();

    void hideLinearLayoutError();

    void showRecyclerView();

    void hideRecyclerView();

    void offline();

    void emptyList();

    void error(String error);

    void setItems(ArrayList<Visage> data);
}
