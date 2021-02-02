package mx.app.fashionme.view.interfaces;

/**
 * Created by heriberto on 14/03/18.
 */

public interface IClotheDetailView {

    void initializeViews();

    void setViews(String urlImage, String name, String brand, String price, int id, String link);

    void setFabFav(boolean isFav);

    void setFabShopping(boolean isInCar);

    void favAdded();
    void favRemoved();

    void shoppingAdded();

    void shoppingRemoved();
    void showError(String error);
}
