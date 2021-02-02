package mx.app.fashionme.ui.modules.commons;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Clase base para controllers (activities)
 * @author heriberto martinez elizarraraz
 * @since 209-08-2020
 */
public abstract class BaseController extends AppCompatActivity implements HasSupportFragmentInjector {

    /**
     * Variable inyectada DispatchingAndroidInjector
     */
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    /**
     * Metodo sobreescrito de dagger
     * @return fragment dispatching android inyector
     */
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    /**
     * Metdoo sobreescrito para attach base context
     * @param newBase contexto base
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    /**
     * Metodo oncreate
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
