package mx.app.fashionme.presenter;

import android.content.Context;

import mx.app.fashionme.interactor.interfaces.IExtraCalidoInteractor;
import mx.app.fashionme.presenter.interfaces.IExtraCalidoPresenter;
import mx.app.fashionme.view.interfaces.IExtraCalidoView;

public class ExtraCalidoPresenter implements IExtraCalidoPresenter {

    private IExtraCalidoView view;
    private IExtraCalidoInteractor interactor;
    private Context context;

    public ExtraCalidoPresenter(IExtraCalidoView view, IExtraCalidoInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

}