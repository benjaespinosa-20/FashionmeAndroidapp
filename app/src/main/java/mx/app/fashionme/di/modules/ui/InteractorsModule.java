package mx.app.fashionme.di.modules.ui;

import dagger.Module;
import mx.app.fashionme.di.modules.ui.interactors.AcquireInteractorModule;
import mx.app.fashionme.di.modules.ui.interactors.ChatAssessorInteractorModule;
import mx.app.fashionme.di.modules.ui.interactors.HomeInteractorModule;

/**
 * InteractorsModule
 */
@Module(includes = {
        HomeInteractorModule.class,
        AcquireInteractorModule.class,
        ChatAssessorInteractorModule.class
})
public class InteractorsModule {
    // Empty class
}
