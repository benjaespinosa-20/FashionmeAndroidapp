package mx.app.fashionme.presenter;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IExtraFrioInteractor;
import mx.app.fashionme.pojo.Question;
import mx.app.fashionme.presenter.interfaces.IExtraFrioPresenter;
import mx.app.fashionme.view.interfaces.IExtraFrioView;

public class ExtraFrioPresenter implements IExtraFrioPresenter {

    private IExtraFrioView view;
    private IExtraFrioInteractor interactor;
    private Context context;

    public ExtraFrioPresenter(IExtraFrioView view, IExtraFrioInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

}
