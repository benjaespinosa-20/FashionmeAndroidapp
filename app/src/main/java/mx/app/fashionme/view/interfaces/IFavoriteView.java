package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.FavoriteAdapter;
import mx.app.fashionme.pojo.Favorite;

public interface IFavoriteView {
    void generateLinearLayoutVertical();

    FavoriteAdapter createAdapter(ArrayList<Favorite> favorites);

    void initializeAdapter(FavoriteAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenterClothe();

    void getClothe();

    void showError(String err);
}
