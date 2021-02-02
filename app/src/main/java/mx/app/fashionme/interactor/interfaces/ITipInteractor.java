package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import mx.app.fashionme.pojo.Tip;

/**
 * Created by heriberto on 26/03/18.
 */

public interface ITipInteractor {

    void getTip(Context context, int idTip, OnGetTipListener callback);

    void checkFav(Context context, int idTip, TipListener listener);

    void removeFavByIdTip(Context context, int idTip, TipListener listener);

    void addToFav(Context context, int idTip, TipListener listener);

    interface OnGetTipListener {
        void onSuccess(Tip tip);
        void onFail(String error);
    }

    interface TipListener {
        void onCheckFav(boolean isFav);
        void onAddTrendFav();
        void onRemoveTrendFav();
        void onError(String error);
    }
}
