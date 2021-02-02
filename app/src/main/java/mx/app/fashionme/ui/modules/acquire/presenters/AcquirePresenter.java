package mx.app.fashionme.ui.modules.acquire.presenters;

import java.util.List;

import javax.inject.Inject;

import mx.app.fashionme.data.models.acquire.SkuDetailsModel;
import mx.app.fashionme.ui.modules.acquire.contracts.AcquireContract;
import mx.app.fashionme.ui.modules.acquire.presenters.listeners.AcquirePresenterListener;
import mx.app.fashionme.ui.modules.commons.BasePresenter;
import mx.app.fashionme.ui.modules.home.enums.HomeEnum;

public class AcquirePresenter extends BasePresenter<AcquireContract.IAcquireView> implements AcquireContract.IAcquirePresenter, AcquirePresenterListener {

    /**
     * Instancia de interactor
     */
    private AcquireContract.IAcquireInteractor interactor;

    /**
     * Metodo constructor con inyeccion de dependencia
     *
     * @param interactor Instancia de interactor
     */
    @Inject
    public AcquirePresenter(AcquireContract.IAcquireInteractor interactor){
        this.interactor = interactor;
    }

    /**
     * Metodo para inicializar alguna funcionalidad
     */
    @Override
    protected void init() {
        interactor.init(this);
    }

    /**
     * Metodo para obtener la descripcion de productos
     * registrados en google play console
     *
     * @param type enum de tipo de compra
     */
    @Override
    public void getSkus(HomeEnum type) {
        if (mView != null) {
            mView.showProgress();
            interactor.getSkus(type);
        }
    }

    /**
     * Metodo de respuesta exitosa al obtener skus
     *
     * @param skus lista de skus
     */
    @Override
    public void onSuccessGettingSkus(List<SkuDetailsModel> skus) {
        if (mView != null) {
            mView.hideProgress();
            mView.populateData(skus);
        }

    }

    /**
     * Metodo de respuesta cuando no se encontraron skus
     */
    @Override
    public void onEmptySkus() {
        if (mView != null) {
            mView.hideProgress();
            mView.onError("No se encontraron items");
        }
    }

}
