package mx.app.fashionme.restApi.model;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Category;

/**
 * Created by heriberto on 13/03/18.
 */

public class CategoryResponse {

    private ArrayList<Category> categories;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
