package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.LookAdapter;
import mx.app.fashionme.pojo.Look;

public interface ILooksView {

    void generateGridLayout(int columns);
    LookAdapter createAdapterLooks(ArrayList<Look> looks);
    void initializeAdapterLooks(LookAdapter adapter);

    void showEmptyLooks();
    void showError(String err);

    void setUpToolbarLooks();
    void setUpPresenterLooks();
    void getLooks();

    void openCalendarDialog(Look look);
    void rvChange();
}
