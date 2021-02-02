package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IStyleResultInteractor;
import mx.app.fashionme.pojo.Style;
import mx.app.fashionme.presenter.interfaces.IStyleResultPresenter;
import mx.app.fashionme.view.interfaces.IStyleResultView;

public class StyleResultPresenter implements IStyleResultPresenter, IStyleResultInteractor.OnGetStyleFinished {

    private IStyleResultView view;
    private IStyleResultInteractor interactor;
    private Context context;

    public StyleResultPresenter(IStyleResultView view, IStyleResultInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getResult() {
        view.showLoading(true);
        interactor.getResult(context, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle params = new Bundle();
        params.putString("screen_name", "Resultado cuestionario de estilo/Android");
        firebaseAnalytics.logEvent("open_screen", params);
    }

    @Override
    public void onSuccess(Style style) {
        if (view != null) {
            view.showLoading(false);
            String lang = context.getString(R.string.app_language);
            String res;

            switch (lang) {
                case "spanish":
                    res = style.getSpanish().getName();
                    break;
                case "english":
                    res = style.getEnglish().getName();
                    break;
                default:
                    res = style.getSpanish().getName();
                    break;
            }

            view.setTvResult(res);
            view.setStyleImagesSlider(style.getImages().getData());
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }
}
