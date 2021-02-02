package mx.app.fashionme.restApi.model;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;

/**
 * Created by heriberto on 14/03/18.
 */

public class ClotheResponse {
    private ArrayList<Clothe> clothes;

    public ArrayList<Clothe> getClothes() {
        return clothes;
    }

    public void setClothes(ArrayList<Clothe> clothes) {
        this.clothes = clothes;
    }
}
