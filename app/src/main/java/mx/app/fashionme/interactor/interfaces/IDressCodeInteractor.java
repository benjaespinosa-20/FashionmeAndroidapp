package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import mx.app.fashionme.pojo.WayDressing;

public interface IDressCodeInteractor {

    void getDressCode(Context context, int idDressCode, OnGetDressCodeFinishedListener callback);

    void checkFavDressCode(Context context, int idDressCode, DressCodeListener listener);
    void removeFavByIdDressCode(Context context, int idDressCode, DressCodeListener listener);
    void addToFav(Context context, int idDressCode, DressCodeListener listener);

    interface OnGetDressCodeFinishedListener {
        void onSuccess(WayDressing wayDressing);
        void onFail(String err);

    }

    interface DressCodeListener {
        void onCheckFav(boolean isFav);
        void onAddTrendFav();
        void onRemoveTrendFav();
        void onError(String error);

    }
}
