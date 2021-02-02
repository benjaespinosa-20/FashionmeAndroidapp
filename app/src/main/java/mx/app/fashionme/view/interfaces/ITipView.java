package mx.app.fashionme.view.interfaces;

import mx.app.fashionme.adapter.TipAdapter;
import mx.app.fashionme.pojo.Tip;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITipView {

    void setUpPresenterTip();
    void getDataTip(int idTip);
    void setToolbarTip();
    void showLoading(boolean show);
    void showEmpty(boolean show);
    void generateLinearLayoutVerticalTip();
    TipAdapter createTipAdapter(Tip tip);
    void initializeTipAdapter(TipAdapter adapter);

    void showError(String error);

    void setButtonFav(boolean isFav);
    void favAdded();
    void favRemoved();
}
