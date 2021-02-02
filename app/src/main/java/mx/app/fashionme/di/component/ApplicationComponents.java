package mx.app.fashionme.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import mx.app.fashionme.di.modules.data.DataManagerModule;
import mx.app.fashionme.di.modules.data.DatabaseModule;
import mx.app.fashionme.di.modules.data.RepositoryModule;
import mx.app.fashionme.di.modules.domain.AsyncModule;
import mx.app.fashionme.di.modules.domain.DomainFactoriesModule;
import mx.app.fashionme.di.modules.domain.UseCasesModule;
import mx.app.fashionme.di.modules.ui.AppModule;
import mx.app.fashionme.di.modules.ui.ControllerBuildersModule;
import mx.app.fashionme.di.modules.ui.FragmentBuildersModule;
import mx.app.fashionme.di.modules.ui.InteractorsModule;
import mx.app.fashionme.di.modules.ui.PresentersModule;
import mx.app.fashionme.ui.app.FashionMeApp;

/**
 * ApplicationComponents
 *
 * @author heriberto martinez
 * @since 21-08-2020
 */
@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ControllerBuildersModule.class,
                FragmentBuildersModule.class,
                PresentersModule.class,
                InteractorsModule.class,
                DomainFactoriesModule.class,
                UseCasesModule.class,
                DataManagerModule.class,
                RepositoryModule.class,
                DatabaseModule.class,
                AsyncModule.class
        }
)
public interface ApplicationComponents {

    /**
     * Method to inject application
     *
     * @param app app instance
     */
    void inject(FashionMeApp app);

    /**
     * Builder interface
     */
    @Component.Builder
    interface Builder {

        /**
         * application
         *
         * @param app app instance
         * @return builder
         */
        @BindsInstance
        Builder application(Application app);

        /**
         * Build
         *
         * @return ApplicationComponents
         */
        ApplicationComponents build();

    }
}
