package mx.app.fashionme.di.modules.domain;

import dagger.Module;
import mx.app.fashionme.di.modules.domain.usecases.SubscriptionUseCasesModule;

/**
 * UseCasesModule
 */
@Module(includes = {SubscriptionUseCasesModule.class})
public class UseCasesModule {
}
