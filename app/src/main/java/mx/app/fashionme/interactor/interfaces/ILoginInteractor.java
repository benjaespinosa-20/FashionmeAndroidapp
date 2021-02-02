package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

/**
 * Created by heriberto on 18/03/18.
 */

public interface ILoginInteractor {

    void login(String email, String password, OnLoginFinishedListener callback);

    interface OnLoginFinishedListener {

        /**
         * Reporta al presenter que hubo un error en el email
         * @param error Mensaje que se mostrará al usuario
         */
        void onEmailError(String error);

        /**
         * Reporta al presenter que hubo un error en el password
         * @param error Mensaje que se mostrará al usuario
         */
        void onPasswordError(String error);

        /**
         * Reporta al presenter la no disponibilidad de la red.
         */
        void onNetworkConnectFailed();

        /**
         * Reporta al presenter un error en la autenticación en Firebase.
         * @param msg Mensaje que se mostrara al usuario
         */
        void onAuthFailed(String msg);

        /**
         * Reporta al presenter que la autenticación en Firebase fue exitosa.
         */
        void onAuthSuccess();
    }
}
