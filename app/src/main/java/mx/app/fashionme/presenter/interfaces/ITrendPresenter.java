package mx.app.fashionme.presenter.interfaces;

import android.app.Activity;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITrendPresenter {

    void getTrend(int idTrend);

    void checkFav(int idTrend);

    void removeFavTrend(int idTrend);

    void addTrendToFav(int idTrend);

    void setAnalytics(Activity activity);
}
