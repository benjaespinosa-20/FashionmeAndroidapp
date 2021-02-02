package mx.app.fashionme.presenter.interfaces;

/**
 * Created by heriberto on 14/03/18.
 */

public interface IClothePresenter {

    void getClothes(int subcategoryId, boolean isClosetIdeal);

    void setAnalytics(boolean isClosetIdeal);
}
