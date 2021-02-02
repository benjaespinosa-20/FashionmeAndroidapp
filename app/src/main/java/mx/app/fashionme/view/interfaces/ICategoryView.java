package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.CategoryAdapter;
import mx.app.fashionme.pojo.Category;

/**
 * Created by heriberto on 13/03/18.
 */

public interface ICategoryView {

    /**
     * Genera el linear layout para el recyclerview
     */
    void generateLinearLayoutVertical();


    /**
     * Crea el adaptador para el recyclerview
     * @param categories Lista de objetos Category
     * @return Adaptador
     */
    CategoryAdapter createAdapter(ArrayList<Category> categories);

    /**
     * Inicializa el adaptador en el recyclerview
     * @param adapter El adaptador de tipo Category
     */
    void initializeAdapter(CategoryAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenter();

    void getCategories();

    void showError(String error);

    void showProgress(boolean show);
}
