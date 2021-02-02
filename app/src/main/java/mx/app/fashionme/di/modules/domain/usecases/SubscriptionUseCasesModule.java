package mx.app.fashionme.di.modules.domain.usecases;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.domain.usecases.subscription.SubscriptionUseCase;
import mx.app.fashionme.domain.usecases.subscription.impl.SubscriptionUseCaseImpl;

/**
 * SubscriptionUseCasesModule
 * Modulo de inyeccion de dependencias para los casos de uso de suscripcion
 */
@Module
public class SubscriptionUseCasesModule {

    /**
     * Metodo para proveer una instancia de subscription use case
     * @param application instancia de application
     * @return instancia de SubscriptionUseCaseImpl
     */
    @Provides
    SubscriptionUseCase provideSubscriptionUseCaseImpl(Application application) {
        return new SubscriptionUseCaseImpl(application);
    }
}
