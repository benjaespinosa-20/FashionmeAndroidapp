package mx.app.fashionme.di.modules.ui;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.view.ChatAssessorActivity;

/**
 * ControllerBuildersModule
 */
@Module
public abstract class ControllerBuildersModule {

    /**
     * Metodo que construye una actividad de Home para inyectarla
     * @return HomeActivity
     */
    @ContributesAndroidInjector
    abstract HomeController buildHomeController();

    /**
     * Metodo que construye una actividad de ChatAssessor para inyectarla
     *
     * @return ChatAssessorActivity
     */
    @ContributesAndroidInjector
    abstract ChatAssessorActivity buildChatAssessorController();

    //@ContributesAndroidInjector
    //abstract BuildingActivity buildBuildingController();
}
