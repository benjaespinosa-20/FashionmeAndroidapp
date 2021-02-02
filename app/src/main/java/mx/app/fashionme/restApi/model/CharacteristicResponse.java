package mx.app.fashionme.restApi.model;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public class CharacteristicResponse {
    private ArrayList<Characteristic> arrayList;

    public ArrayList<Characteristic> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Characteristic> arrayList) {
        this.arrayList = arrayList;
    }
}
