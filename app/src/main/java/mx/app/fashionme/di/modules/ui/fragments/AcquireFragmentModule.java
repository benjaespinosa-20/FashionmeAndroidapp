package mx.app.fashionme.di.modules.ui.fragments;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mx.app.fashionme.ui.modules.acquire.fragments.AcquireFragment;

/**
 * AcquireFragmentModule
 *
 * Clase para inyecccion de dependencias del modulo acquire
 */
@Module
public abstract class AcquireFragmentModule {

    /**
     * Metodo que construye un fragmento de Acquire para inyectarlo
     * @return Instancia AcquireFragment
     */
    @ContributesAndroidInjector
    abstract AcquireFragment buildAcquireFragment();

}
