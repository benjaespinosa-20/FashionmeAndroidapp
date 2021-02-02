package mx.app.fashionme.di.modules.ui.interactors;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.acquire.contracts.AcquireContract;
import mx.app.fashionme.ui.modules.acquire.interactors.AcquireInteractor;

/**
 * AcquireInteractorModule
 * Clase para la inyeccion de dependencias de interactor
 *
 * @author heriberto martinez elizarraraz
 * @since 18-10-2020
 */
@Module
public class AcquireInteractorModule {

    /**
     * Metodo que provee una instancia de interactor
     * @return instancia de AcquireInteractor
     */
    @Provides
    AcquireContract.IAcquireInteractor provideAcquireInteractor(BillingAgentConfig agentConfig){
        return new AcquireInteractor(agentConfig);
    }
}
