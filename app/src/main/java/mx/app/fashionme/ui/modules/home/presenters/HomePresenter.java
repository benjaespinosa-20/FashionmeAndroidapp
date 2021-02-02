package mx.app.fashionme.ui.modules.home.presenters;

import android.app.Dialog;
import android.content.Context;

import javax.inject.Inject;

import mx.app.fashionme.ui.modules.commons.BasePresenter;
import mx.app.fashionme.ui.modules.home.contracts.HomeContracts;
import mx.app.fashionme.ui.modules.home.presenters.listeners.HomePresenterListener;
import mx.app.fashionme.utils.Utils;

/**
 * Created by heriberto on 21/03/18.
 */

public class HomePresenter extends BasePresenter<HomeContracts.IHomeView> implements HomeContracts.IHomePresenter, HomePresenterListener {
    /**
     * Home interactor instance
     */
    private HomeContracts.IHomeInteractor interactor;

    /**
     * Metodo constructor
     *
     * @param interactor Instancia inyectada de interactor
     */
    @Inject
    public HomePresenter(HomeContracts.IHomeInteractor interactor) {
        this.interactor = interactor;
    }

    /**
     * Metodo para inicializar alguna funcionalidad
     */
    @Override
    protected void init() {
        interactor.init(this);
    }

    @Override
    public void checkConditions(Context context) {
        if (mView != null) {
            mView.showProgress();
            interactor.checkLogin(context);
        }
    }

    @Override
    public void logout() {
        //interactor.logout();
    }

    @Override
    public void openDialogAbout(Dialog dialog) {
        interactor.openDialog(dialog);
    }

    @Override
    public void getVersionName(Context context) {
        interactor.getVersionName(context);
    }

    /**
     * Metodo para chatear con asesor
     */
    @Override
    public void chatWithConsultant() {
        if (mView != null) {
            mView.showProgress();
            interactor.chatWithConsultant();
        }
    }

    /**
     * Metodo para validar la suscripcion
     *
     * @param isPremium valor de premium desde back
     */
    @Override
    public void getSubscription(boolean isPremium) {
        if (mView != null) {
            mView.showProgress();
            interactor.getSubscription(isPremium);
        }
    }

    @Override
    public void onFailLogin() {
        if (mView != null) {
            mView.goToLoginPage();
        }
    }

    @Override
    public void onPassLogin(Context context) {
        interactor.getUser(context);
    }

    /*
    @Override
    public void onFailBody() {
        if (view != null) {
            SessionPrefs.get(context).saveResult(false);
            view.goToTakePhotoPage();
        }
    }

    @Override
    public void onPassBody() {
        //interactor.checkColor(context, this);
    }

    @Override
    public void onFailColor() {
        if (view != null) {
            SessionPrefs.get(context).saveResult(false);
            view.goToTestPage();
        }
    }

    @Override
    public void onPassColor() {
        //interactor.checkCharacteristics(context, this);
    }

    @Override
    public void onFailCharacteristics() {
        if (view != null) {
            SessionPrefs.get(context).saveResult(false);
            view.goToCharacteristicsPage();
        }
    }

    @Override
    public void onPassCharacteristics() {
        interactor.checkConditions(context, this);
    }
    */

    @Override
    public void onError(String err) {
        if (mView != null) {
            mView.hideProgress();
            mView.onError(err);
        }
    }

    @Override
    public void onSuccessConditions(String name, String email, String photoUri, boolean trial, boolean premium) {
        if (mView != null) {
            mView.hideProgress();
            mView.setUsername(
                    Utils.toCapitalName(name),
                    email,
                    photoUri
            );
            mView.generateGridLayout(3);
            mView.initializeAdapterItems(
                    mView.createAdapterItems(
                            trial,
                            premium
                    )
            );
            mView.generateGridLayoutItemsRecommend(4);
            mView.initializeAdapterItemsRecommend(
                    mView.createAdapterItemsRecommend(
                            trial,
                            premium
                    )
            );
        }
    }

    /**
     * Metodo de respuesta cuando no se tiene tiempo pendiente de chat
     */
    @Override
    public void onPendingChatTimeFinished() {
        if (mView != null) {
            mView.hideProgress();
            mView.onPendingChatTimeFinished();
        }
    }

    /**
     * Metodo de respuesta cuando se tiene tiempo pendiente de chat
     */
    @Override
    public void onPendingChatTime() {
        if (mView != null) {
            mView.hideProgress();
            mView.onPendingChatTime();
        }
    }

    @Override
    public void onNonSubscription() {
        if (mView !=null) {
            mView.hideProgress();
            mView.showSubscriptionDialog();
        }
    }

    @Override
    public void onSubscriptionActive() {
        if (mView !=null) {
            mView.hideProgress();
            mView.openModuleWithSubscriptionActive();
        }
    }

    @Override
    public void onSetDialog(Dialog dialog) {
        if (mView != null) {
            mView.onCreateDialogAbout(dialog);
            mView.showDialog(dialog);
        }
    }

    @Override
    public void onGetVersionName(String versionName) {
        if (mView != null) {
            mView.setVersionName(versionName);
        }
    }

}
