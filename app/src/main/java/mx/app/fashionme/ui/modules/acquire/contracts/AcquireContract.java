package mx.app.fashionme.ui.modules.acquire.contracts;

import java.util.List;

import mx.app.fashionme.data.models.acquire.SkuDetailsModel;
import mx.app.fashionme.ui.modules.acquire.presenters.listeners.AcquirePresenterListener;
import mx.app.fashionme.ui.modules.commons.BaseContract;
import mx.app.fashionme.ui.modules.home.enums.HomeEnum;

/**
 * AcquireContract
 *
 * Interfaz contrato que contiene las interfaces de vista, presenter e interactor
 * para el modulo Acquire
 *
 * @author heriberto martinez elizarraraz
 * @since 18-octubre-2020
 */
public interface AcquireContract {

    /**
     * IAcquireView
     * Interfaz de metodos que seran ocupados en la vista
     */
    interface IAcquireView extends BaseContract.IViewBase {

        /**
         * Metodo para llenar recycler view con data obtenida
         *
         * @param skus lista de skus
         */
        void populateData(List<SkuDetailsModel> skus);

    }

    /**
     * IAcquirePresenter
     * Interfaz para los metodos de presenter
     */
    interface IAcquirePresenter extends BaseContract.IPresenterBase<IAcquireView> {

        /**
         * Metodo para obtener la descripcion de productos
         * registrados en google play console
         *
         * @param type enum de tipo de compra
         */
        void getSkus(HomeEnum type);

    }

    /**
     * IAcquireInteractor
     * Interfaz para interactor
     */
    interface IAcquireInteractor {

        /**
         * Metodo para iniciar acquire interactor
         *
         * @param listener listener para responder a presenter
         */
        void init(AcquirePresenterListener listener);

        /**
         * Metodo para obtener la descripcion de productos
         * registrados en google play console
         *
         * @param type enum de tipo de compra
         */
        void getSkus(HomeEnum type);

    }


}
