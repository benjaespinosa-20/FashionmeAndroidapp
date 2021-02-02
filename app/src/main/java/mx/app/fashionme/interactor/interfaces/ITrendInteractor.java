package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import mx.app.fashionme.pojo.Trend;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITrendInteractor {

    void getTrend(Context context, int idTrend, OnGetTrendListener callback);

    void checkFav(Context context, int idTrend, TrendListener listener);

    void addToFav(Context context, int idTrend, TrendListener listener);

    void removerFavByIdTrend(Context context, int idTrend, TrendListener listener);

    interface OnGetTrendListener {
        void onSuccess(Trend trend);
        void onFailure(String error);
    }

    interface TrendListener {
        void onCheckFav(boolean isFav);
        void onAddTrendFav();
        void onRemoveTrendFav();
        void onError(String error);

    }
}
