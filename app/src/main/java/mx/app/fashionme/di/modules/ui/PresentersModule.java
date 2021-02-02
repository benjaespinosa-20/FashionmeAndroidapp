package mx.app.fashionme.di.modules.ui;

import dagger.Module;
import mx.app.fashionme.di.modules.ui.presenters.AcquirePresenterModule;
import mx.app.fashionme.di.modules.ui.presenters.ChatAssessorPresenterModule;
import mx.app.fashionme.di.modules.ui.presenters.HomePresenterModule;

/**
 * PresentersModule
 */
@Module(includes = {
        HomePresenterModule.class,
        AcquirePresenterModule.class,
        ChatAssessorPresenterModule.class
})
public class PresentersModule {
    // Empty class
}
