package mx.app.fashionme.di.modules.ui;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;

/**
 * AppModule
 */
@Module
public class AppModule {

    /**
     * Method to provide app context
     * @param application application instance
     * @return context
     */
    @Provides
    Context provideContext(Application application) {
        return application;
    }

    /**
     * Metodo para proveer una instancia de BillingAgentConfig
     * @param application Application instance
     * @return BillingAgentConfig instance
     */
    @Singleton
    @Provides
    BillingAgentConfig provideBillingConfig(Application application) {
        return new BillingAgentConfig(application);
    }

}
