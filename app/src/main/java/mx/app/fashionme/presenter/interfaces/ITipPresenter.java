package mx.app.fashionme.presenter.interfaces;

import android.app.Activity;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITipPresenter {

    void getTip(int idTip);

    void checkFav(int idTip);
    void removeFavTip(int idTip);
    void addFavTip(int idTip);

    void setAnalytics(Activity activity);
}
