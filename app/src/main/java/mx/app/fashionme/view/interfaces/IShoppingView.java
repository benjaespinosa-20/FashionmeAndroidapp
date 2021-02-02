package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.ClotheAdapter;
import mx.app.fashionme.pojo.Clothe;

public interface IShoppingView {
    void generateLinearLayoutVertical();

    ClotheAdapter createAdapter(ArrayList<Clothe> clothes);

    void initializeAdapter(ClotheAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenterClothe();

    void getClothe();

    void showError(String err);
}
