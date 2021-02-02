package mx.app.fashionme.ui.modules.home.contracts;

import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import mx.app.fashionme.adapter.HomeItemAdapter;
import mx.app.fashionme.ui.modules.commons.BaseContract;
import mx.app.fashionme.ui.modules.home.presenters.listeners.HomePresenterListener;

/**
 * HomeContracts
 * Interfaz contrato que contiene interfaces para vista, presentador e interactor
 *
 * @author heriberto martinez
 * @since 21-08-2020
 */
public interface HomeContracts {

    /**
     * IHomeView
     * Contiene metodos de vista
     */
    interface IHomeView extends BaseContract.IViewBase {

        void setUpToolbar();

        void setUpNavigationView();

        void setupDrawerContent(NavigationView navigationView);

        void selectItem(MenuItem item);

        void goToLoginPage();

        void goToTakePhotoPage();

        void goToCharacteristicsPage();

        void goToTestPage();

        void goToResultPage();

        void goToProfilePage();

        void goToLooksPage();

        void goToSettingsPage();

        void generateGridLayout(int columns);

        HomeItemAdapter createAdapterItems(boolean trial, boolean premium);

        void initializeAdapterItems(HomeItemAdapter adapter);

        void generateGridLayoutItemsRecommend(int columns);

        HomeItemAdapter createAdapterItemsRecommend(boolean trial, boolean premium);

        void initializeAdapterItemsRecommend(HomeItemAdapter adapter);

        void setUsername(String username, String email, String photoUri);

        void showLoading(boolean show);

        void showSnackBar(String msg);

        void logout();

        void onCreateDialogAbout(Dialog dialog);

        void showDialog(Dialog dialog);

        void setVersionName(String version);

        void showDialogPremium(String message, String title);

        void showDialogChat(String message, String title);

        /**
         * Metodo de vista cuando el tiempo de chat ha finalizado
         */
        void onPendingChatTimeFinished();

        /**
         * Metodo de vista cuando el tiempo de chat
         */
        void onPendingChatTime();

        /**
         * Metodo para mostrar dialogo de suscripcion
         */
        void showSubscriptionDialog();

        /**
         * Metodo para abrir el modulo con suscripcion activa
         */
        void openModuleWithSubscriptionActive();

    }

    /**
     * IHomePresenter
     * Contains methods for presenter
     */
    interface IHomePresenter extends BaseContract.IPresenterBase<IHomeView> {

        void checkConditions(Context context);

        void logout();

        void openDialogAbout(Dialog dialog);

        void getVersionName(Context context);
        //String getGender(Context context);

        /**
         * Metodo para chatear con asesor
         */
        void chatWithConsultant();

        /**
         * Metodo para validar la suscripcion
         *
         * @param isPremium valor de premium desde back
         */
        void getSubscription(boolean isPremium);

    }

    /**
     * IHomeInteractor
     * Contains methods for interactor
     */
    interface IHomeInteractor {

        /**
         * Metodo para iniciar home interactor
         *
         * @param listener para responder a presenter
         */
        void init(HomePresenterListener listener);

        void checkLogin(Context context);

        void getUser(Context context);

        void openDialog(Dialog dialog);

        void getVersionName(Context appContext);
        //void checkConditions(Context context, OnCheckedConditionsListener callback);
        //void checkBody(Context context, OnCheckedConditionsListener callback);
        //void checkColor(Context context, OnCheckedConditionsListener callback);
        //void checkCharacteristics(Context context, OnCheckedConditionsListener callback);
        //boolean isShownResult(Context context);
        //void updateTokenDevice(Context context);

        /**
         * Metodo para chatear con asesor
         */
        void chatWithConsultant();

        /**
         * Metodo para validar la suscripcion
         *
         * @param isPremium valor de premium desde back
         */
        void getSubscription(boolean isPremium);

    }
}
