package mx.app.fashionme.di.modules.ui.interactors;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.data.repositories.DataBaseRepository;
import mx.app.fashionme.domain.usecases.subscription.SubscriptionUseCase;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.home.interactors.HomeInteractor;
import mx.app.fashionme.ui.modules.home.contracts.HomeContracts;

/**
 * HomeInteractorModule
 * Clase para la inyeccion de dependencias de interactor
 * @author heriberto martinez
 * @since 24-08-2020
 */
@Module
public class HomeInteractorModule {

    /**
     * Metodo que provee una instancia de interactor
     *
     * @param dataBaseRepository instancia de base de datos
     * @param billingAgentConfig instancia de billing agent config
     * @param subscriptionUseCase instancia de subscription use case
     *
     * @return retorna una instancia de interactor
     */
    @Provides
    HomeContracts.IHomeInteractor provideHomeInteractor(DataBaseRepository dataBaseRepository, BillingAgentConfig billingAgentConfig,
                                                        SubscriptionUseCase subscriptionUseCase) {
        return new HomeInteractor(dataBaseRepository, billingAgentConfig, subscriptionUseCase);
    }

}
