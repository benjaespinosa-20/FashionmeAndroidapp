package mx.app.fashionme.di.modules.ui.presenters;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.ui.modules.home.presenters.HomePresenter;
import mx.app.fashionme.ui.modules.home.contracts.HomeContracts;

/**
 * HomePresenterModule.
 * Clase para inyeccion de dependencias en el presenter
 * @author heriberto martinez
 * @since 24-08-2020
 */
@Module
public class HomePresenterModule {

    /**
     * Metodo que provee una instancia de home presenter
     * @param interactor instancia de home interactor
     * @return HomePresenter
     */
    @Provides
    HomeContracts.IHomePresenter provideHomePresenter(HomeContracts.IHomeInteractor interactor) {
        return new HomePresenter(interactor);
    }
}
