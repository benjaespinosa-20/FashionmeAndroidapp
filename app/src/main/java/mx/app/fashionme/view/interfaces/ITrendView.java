package mx.app.fashionme.view.interfaces;

import mx.app.fashionme.adapter.TrendAdapter;
import mx.app.fashionme.pojo.Trend;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITrendView {

    void setUpPresenter();
    void getData(int idTrend);
    void setToolbar();
    void showLoading(boolean show);
    void showEmpty(boolean show);
    void generateLinearLayoutVertical();
    TrendAdapter createAdapter(Trend trend);
    void initializeAdapter(TrendAdapter adapter);




    void showError(String error);

    void setButtonFav(boolean isFav);

    void favAdded();

    void favRemoved();
}
