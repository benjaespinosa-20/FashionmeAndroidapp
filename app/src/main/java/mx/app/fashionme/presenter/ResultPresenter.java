package mx.app.fashionme.presenter;

import android.content.Context;
import android.widget.Toast;

import mx.app.fashionme.interactor.interfaces.IResultInteractor;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.IResultPresenter;
import mx.app.fashionme.view.interfaces.IResultView;

public class ResultPresenter implements IResultPresenter, IResultInteractor.OnGetResultListener {

    private IResultView view;
    private IResultInteractor interactor;
    private Context context;

    public ResultPresenter(IResultView view, IResultInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getResults() {
        if (view != null) {
            view.showProgressBar();
        }
        interactor.getResults(context, this);
    }

    @Override
    public void onSuccess(String name, String body, String color, String imageUrl) {
        if (view != null){
            view.hideProgressBar();
            view.setResults(name, body, color, imageUrl);
        }
    }

    @Override
    public void onFail(String err) {
        if (view != null) {
            switch (err) {
                case "body":
                    String token = SessionPrefs.get(context).getTokenBodyApi();
                    if (token == null) {
                        view.goToBodyActivity();
                    } else {
                        interactor.sendTokenAPIBody(token, context, this);
                    }

                    break;
                case "color":
                    view.goToTestColorActivity();
                    break;
                default:
                    Toast.makeText(context, "Ocurrio un error al obtener tus resultados", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
