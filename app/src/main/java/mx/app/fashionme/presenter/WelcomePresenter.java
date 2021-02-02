package mx.app.fashionme.presenter;

import android.content.Context;

import mx.app.fashionme.interactor.interfaces.IWelcomeInteractor;
import mx.app.fashionme.presenter.interfaces.IWelcomePresenter;
import mx.app.fashionme.view.interfaces.IWelcomeView;

/**
 * Created by heriberto on 24/03/18.
 */

public class WelcomePresenter implements IWelcomePresenter, IWelcomeInteractor {

    private IWelcomeView view;
    private IWelcomeInteractor interactor;
    private Context context;


    public WelcomePresenter(IWelcomeView view, IWelcomeInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void nextPage() {
        if (view != null) {
            view.goToNextPage();
        }
    }
}
