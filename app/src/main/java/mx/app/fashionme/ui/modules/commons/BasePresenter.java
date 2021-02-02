package mx.app.fashionme.ui.modules.commons;

/**
 * BasePresenter
 * Clase base de presenters
 * @author heriberto martinez
 * @since 23-08-2020
 * @param <T> Generico de presenter
 */
public abstract class BasePresenter<T extends BaseContract.IViewBase> implements BaseContract.IPresenterBase<T> {

    /**
     * View base
     */
    protected T mView = null;

    /**
     * Metodo para inicializar alguna funcionalidad
     */
    protected abstract void init();

    /**
     * Metodo para establecer una vista
     * @param view vista generica
     */
    @Override
    public void setView(T view) {
        if (mView == null) {
            mView = view;
            init();
        }
    }

    /**
     * Metodo para destruir
     */
    @Override
    public void onDestroy() {
        if (mView != null) {
            mView.hideProgress();
            mView = null;
        }
    }
}
