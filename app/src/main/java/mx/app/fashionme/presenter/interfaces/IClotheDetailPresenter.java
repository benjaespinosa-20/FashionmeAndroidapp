package mx.app.fashionme.presenter.interfaces;


import android.app.Activity;

/**
 * Created by heriberto on 14/03/18.
 */

public interface IClotheDetailPresenter {

    void getDetails();

    void showDetails(String url, String name, String brand, String price, int id, String link);

    void shareOnFacebook(String urlImage);
    void checkFav(int idClothe);
    void checkShopping(int idClothe);

    void addClotheToFav(int idClothe);

    void removeFav(int idClothe);

    void addToShopping(int idClothe, String name, String englishName, String link, String url);

    void removeShopping(int idClothe);

    //void showZoom(Activity activity, String url);

    void setAnalytics();
}
