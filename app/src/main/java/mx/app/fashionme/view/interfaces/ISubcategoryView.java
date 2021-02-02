package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.SubcategoryAdapter;
import mx.app.fashionme.pojo.Subcategory;

/**
 * Created by heriberto on 13/03/18.
 */

public interface ISubcategoryView {

    void generateLinearLayoutVertical();

    SubcategoryAdapter createAdapter(ArrayList<Subcategory> subcategories);

    void initializeAdapter(SubcategoryAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenter();

    void getSubcategories();

    void showError(String err);
}
