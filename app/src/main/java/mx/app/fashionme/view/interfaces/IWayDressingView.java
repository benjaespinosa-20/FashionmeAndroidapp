package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.WayDressingAdapter;
import mx.app.fashionme.pojo.WayDressing;

public interface IWayDressingView {
    void generateWayDressingLinearLayoutVertical();

    WayDressingAdapter createWayDressingAdapter(ArrayList<WayDressing> ways);

    void initializeWayDressingAdapter(WayDressingAdapter adapter);

    void showEmptyListWays();

    void showOfflineWays();

    void setUpToolbarWays();

    boolean isOnlineWays();

    void setUpPresenterWays();

    void getWaysDressing();

    void showErrorWays(String error);
}
