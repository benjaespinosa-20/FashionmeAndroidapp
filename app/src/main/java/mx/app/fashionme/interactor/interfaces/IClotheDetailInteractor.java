package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;

import mx.app.fashionme.presenter.ClotheDetailPresenter;

public interface IClotheDetailInteractor {
    void checkFav(Context context, int idClothe, ClotheDetailListener listener);

    void checkShopping(Context context, int idClothe, ClotheDetailListener listener);

    void addToFav(Context context, int idClothe, ClotheDetailListener listener);

    void removeFavByIdClothe(Context context, int idClothe, ClotheDetailListener listener);

    void addToCart(Context context, int idClothe, String name, String englishName, String link, String url, ClotheDetailListener listener);

    void removeCartByIdClothe(Context context, int idClothe, ClotheDetailListener listener);

    void showZoomImage(Activity activity, String url);

    interface ClotheDetailListener {
        void onCheckFav(boolean isFav);
        void onCheckShopping(boolean isInCar);
        void onAddClotheToFav();
        void onRemoveClotheFav();

        void onAddClotheToCart();

        void onRemoveClotheCart();
        void onError(String err);
    }
}
