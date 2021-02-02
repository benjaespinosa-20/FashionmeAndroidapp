package mx.app.fashionme.di.component;

import android.app.Application;

/**
 * AppInjector
 *
 * @author heriberto martinez
 * @since 21-08-2020
 */
public class AppInjector {

    /**
     * Method to init configuration
     * @param app application instance
     */
    public static void init(Application app) {
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
    }

}
