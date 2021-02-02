package mx.app.fashionme.ui.modules.commons;

/**
 * BaseContract
 * interface comun para todos los contratos
 *
 * @author heriberto martinez
 * @since 23-08-2020
 */
public interface BaseContract {

    /**
     * Interfaz base de views
     */
    interface IViewBase {

        /**
         * Metodo para mostrar progress bar
         */
        void showProgress();

        /**
         * Metodo para ocultar progress bar
         */
        void hideProgress();

        /**
         * Metodo para mostrar mensajes de error
         */
        void onError(String error);

    }

    /**
     * Interfaz base de presenters
     * @param <T> Interfaz generica de views
     */
    interface IPresenterBase<T extends IViewBase> {

        /**
         * Metodo para establecer una vista
         * @param view vista generica
         */
        void setView(T view);

        /**
         * Metodo para destruir
         */
        void onDestroy();

    }
}
