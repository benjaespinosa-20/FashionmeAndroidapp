package mx.app.fashionme.presenter.interfaces;

/**
 * Created by heriberto on 13/03/18.
 */

public interface ICategoryPresenter {

    /**
     * Obtiene las categorias de la fuente de datos (DB o WebService)
     */
    void getCategories();

    void setAnalytics();
}
