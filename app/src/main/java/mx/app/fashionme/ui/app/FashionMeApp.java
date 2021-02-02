package mx.app.fashionme.ui.app;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import mx.app.fashionme.di.component.AppInjector;
import mx.app.fashionme.di.component.DaggerApplicationComponents;
import timber.log.Timber;

/**
 * Fashion me app config
 *
 * @author heriberto martinez
 * @since 21-08-2020
 */
public class FashionMeApp extends MultiDexApplication implements HasActivityInjector {

    /**
     * Activity injector instance
     */
    @Inject
    public DispatchingAndroidInjector<Activity> mActivityInjector;

    public static FashionMeApp instance = null;

    /**
     * attachBaseContext
     *
     * @param base context instance
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * onCreate
     */
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponents.builder()
                .application(this)
                .build()
                .inject(this);
        AppInjector.init(this);

        Timber.plant(new Timber.DebugTree());

        instance = this;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mActivityInjector;
    }
}
