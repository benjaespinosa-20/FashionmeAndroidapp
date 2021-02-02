package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.ClotheAdapter;
import mx.app.fashionme.pojo.Clothe;

/**
 * Created by heriberto on 14/03/18.
 */

public interface IClotheView {

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
