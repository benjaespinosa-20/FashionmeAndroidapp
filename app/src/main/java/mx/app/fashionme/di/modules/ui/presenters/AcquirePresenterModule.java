package mx.app.fashionme.di.modules.ui.presenters;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.ui.modules.acquire.contracts.AcquireContract;
import mx.app.fashionme.ui.modules.acquire.presenters.AcquirePresenter;

/**
 * AcquirePresenterModule
 * Clase para inyeccion de dependencias en el presenter
 *
 * @author heriberto martinez elizarraraz
 * @since 18-10-2020
 */
@Module
public class AcquirePresenterModule {

    /**
     * Metodo que provee una instancia de acquire presenter
     *
     * @param interactor instancia de acquire interactor
     *
     * @return Instancia de AcquirePresenter
     */
    @Provides
    AcquireContract.IAcquirePresenter provideAcquirePresenter(AcquireContract.IAcquireInteractor interactor) {
        return new AcquirePresenter(interactor);
    }
}
