package mx.app.fashionme.domain.usecases.subscription.impl;

import android.content.Context;

import org.jetbrains.annotations.NotNull;


import javax.inject.Inject;

import mx.app.fashionme.domain.usecases.subscription.SubscriptionUseCase;
import mx.app.fashionme.pojo.Premium;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Response;

/**
 * SubscriptionUseCaseImpl
 * Clase de logica para actualizar la suscripcion
 */
public class SubscriptionUseCaseImpl implements SubscriptionUseCase {

    /**
     * Instancia de contexto de aplicacion
     */
    private Context mContext;
    /**
     * Instancia de callback para respuesta al interactor
     */
    private SubscriptionUseCase.Callback mCallback;

    /**
     * Constructor inyectado con dependencias
     *
     * @param context instancia de contexto de aplicacion
     */
    @Inject
    public SubscriptionUseCaseImpl(Context context) {
        this.mContext = context;
    }

    /**
     * Metodo de respuesta al actualizar suscripcion en servidor
     *
     * @param callback listener de respuesta
     * @return this
     */
    @Override
    public SubscriptionUseCase onUpdated(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Metodo para actualizar suscripcion en servidor
     *
     * @param premium bandera actualizar si/no es premium
     */
    @Override
    public void updateSubscription(boolean premium) {
        int userId = SessionPrefs.get(mContext).getUserId();
        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<User>> baseCall = endpointsApi.updatePremium(new Premium(premium), userId);

        baseCall.enqueue(new retrofit2.Callback<Base<User>>() {
            @Override
            public void onResponse(@NotNull Call<Base<User>> call, @NotNull Response<Base<User>> response) {
                mCallback.onUpdated();
            }

            @Override
            public void onFailure(@NotNull Call<Base<User>> call, @NotNull Throwable t) {
                t.printStackTrace();
                mCallback.onUpdated();
            }
        });
    }
}
