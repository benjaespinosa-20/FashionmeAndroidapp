package mx.app.fashionme.ui.modules.acquire.interactors;

import com.android.billingclient.api.SkuDetails;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mx.app.fashionme.data.models.acquire.SkuDetailsModel;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.acquire.contracts.AcquireContract;
import mx.app.fashionme.ui.modules.acquire.presenters.listeners.AcquirePresenterListener;
import mx.app.fashionme.ui.modules.home.enums.HomeEnum;

public class AcquireInteractor implements AcquireContract.IAcquireInteractor {

    /**
     * Instancia de presenter listener
     */
    private AcquirePresenterListener mListener;
    /**
     * Instancia de configuracion de facturación
     */
    private BillingAgentConfig agentConfig;

    /**
     * Constructor con instancias inyectadas
     */
    @Inject
    public AcquireInteractor(BillingAgentConfig agentConfig){
        this.agentConfig = agentConfig;
    }

    /**
     * Metodo para iniciar acquire interactor
     *
     * @param listener listener para responder a presenter
     */
    @Override
    public void init(AcquirePresenterListener listener) {
        this.mListener = listener;
    }

    /**
     * Metodo para obtener la descripcion de productos
     * registrados en google play console
     */
    @Override
    public void getSkus(HomeEnum type) {
        List<SkuDetails> skus = agentConfig.getmSubscriptionsList();
        if (skus.size() == 0) {
            mListener.onEmptySkus();
        } else {
            List<SkuDetailsModel> items = new ArrayList<>();
            for (SkuDetails sku: skus) {
                if (sku.getType().equals(type.name().toLowerCase())) {
                    SkuDetailsModel item = new SkuDetailsModel(
                            sku.getSku(), sku.getType(),sku.getPrice(), sku.getTitle(), sku.getDescription(), sku.getOriginalJson());
                    items.add(item);
                }
            }
            mListener.onSuccessGettingSkus(items);
        }
    }
}
