package mx.app.fashionme.di.component;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * ActivityLifecycleCallbacksImpl
 *
 * @author heriberto martinez
 * @since 21-08-2020
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    /**
     * onActivityCreated
     * @param activity instance
     * @param bundle save instance
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        handleActivity(activity);
    }

    /**
     * onActivityStarted
     *
     * @param activity instance
     */
    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // not implemented
    }

    /**
     * onActivityResumed
     * @param activity instance
     */
    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        // not implemented
    }

    /**
     * onActivityPaused
     * @param activity instance
     */
    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        // not implemented
    }

    /**
     * onActivityStopped
     * @param activity
     */
    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        // not implemented
    }

    /**
     * onActivitySaveInstanceState
     * @param activity instance
     * @param bundle instance
     */
    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
        // not implemented
    }

    /**
     * onActivityDestroyed
     * @param activity instance
     */
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        // not implemented
    }

    /**
     * Method to handle activity
     * @param activity instance
     */
    private void handleActivity(Activity activity) {
        if (activity instanceof HasSupportFragmentInjector) {
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        /**
                         * onFragmentCreated
                         * @param fm fragment manager instance
                         * @param f fragment instance
                         * @param savedInstanceState bundle instance
                         */
                        @Override
                        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            if (f instanceof Injectable) {
                                AndroidSupportInjection.inject(f);
                            }
                        }
                    }, true);
        }

    }
}
