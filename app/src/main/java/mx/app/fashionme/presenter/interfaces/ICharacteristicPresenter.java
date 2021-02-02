package mx.app.fashionme.presenter.interfaces;

import android.widget.ListView;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public interface ICharacteristicPresenter {
    void getData();
    void sendData(ListView listView);
}
