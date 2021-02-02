package mx.app.fashionme.ui.modules.home.presenters.listeners;

import android.app.Dialog;
import android.content.Context;

/**
 * Interfaz callback para respuestas de interactor
 * @author heriberto martinez
 * @since 23-08-2020
 */
public interface HomePresenterListener {

    void onFailLogin();
    void onPassLogin(Context context);
    void onSetDialog(Dialog dialog);
    void onGetVersionName(String versionName);
    void onError(String err);
    void onSuccessConditions(String name, String email, String photoUri, boolean trial, boolean premium);

    /**
     * Metodo de respuesta cuando no se tiene tiempo pendiente de chat
     */
    void onPendingChatTimeFinished();

    /**
     * Metodo de respuesta cuando se tiene tiempo pendiente de chat
     */
    void onPendingChatTime();

    /**
     * Metodo de respuesta cuando no se tiene suscripcion activa
     */
    void onNonSubscription();

    /**
     * Metodo de respuesta cuando se tiene suscripcion activa
     */
    void onSubscriptionActive();

}
