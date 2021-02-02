package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public interface ICharacteristicView {
    void setAdapterCharacteristic(ArrayList<Characteristic> characteristics);
    void setToolbarCharacteristic();
    void setUpPresenter();
    void getError(String error);
    void sendCharacteristics();
    void navigateToHome();
    void alertBody();
}
