package mx.app.fashionme.restApi.model;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Subcategory;

/**
 * Created by heriberto on 13/03/18.
 */

public class SubcategoryResponse {
    private ArrayList<Subcategory> subcategories;

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
