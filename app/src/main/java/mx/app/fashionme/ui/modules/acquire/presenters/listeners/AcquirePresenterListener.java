package mx.app.fashionme.ui.modules.acquire.presenters.listeners;

import java.util.List;

import mx.app.fashionme.data.models.acquire.SkuDetailsModel;

/**
 * AcquirePresenterListener
 * Interfaz callback para respuestas al presenter
 *
 * @author heriberto martinez elizarraraz
 * @since 18-octubre-2020
 */
public interface AcquirePresenterListener {

    /**
     * Metodo de respuesta exitosa al obtener skus
     *
     * @param skus lista de skus
     */
    void onSuccessGettingSkus(List<SkuDetailsModel> skus);

    /**
     * Metodo de respuesta cuando no se encontraron skus
     */
    void onEmptySkus();

}
