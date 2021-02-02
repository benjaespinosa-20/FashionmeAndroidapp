package mx.app.fashionme.domain.usecases.subscription;

/**
 * SubscriptionUseCase
 * Caso de uso para actualizar suscripcion
 */
public interface SubscriptionUseCase {

    /**
     * Metodo de respuesta al actualizar suscripcion en servidor
     *
     * @param callback listener de respuesta
     * @return this
     */
    SubscriptionUseCase onUpdated(SubscriptionUseCase.Callback callback);

    /**
     * Metodo para actualizar suscripcion en servidor
     *
     * @param premium bandera actualizar si/no es premium
     */
    void updateSubscription(boolean premium);

    /**
     * Interfaz de escucha
     */
    interface Callback {
        /**
         * Metodo on updated
         */
        void onUpdated();
    }

}
