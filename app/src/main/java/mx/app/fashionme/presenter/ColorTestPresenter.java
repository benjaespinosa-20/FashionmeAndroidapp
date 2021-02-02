package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import mx.app.fashionme.interactor.interfaces.IColorTestInteractor;
import mx.app.fashionme.pojo.ColorQuestionsViewModel;
import mx.app.fashionme.presenter.interfaces.IColorTestPresenter;
import mx.app.fashionme.view.ResultActivity;
import mx.app.fashionme.view.interfaces.IColorTestView;

/**
 * Created by heriberto on 22/03/18.
 */

public class ColorTestPresenter implements IColorTestPresenter, IColorTestInteractor.TestListener {

    static final String TAG = ColorTestPresenter.class.getSimpleName();

    private IColorTestView view;
    private IColorTestInteractor interactor;
    private Context context;
    private Activity activity;

    public ColorTestPresenter(IColorTestInteractor interactor, Context context, Activity activity) {
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void setView(IColorTestView view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        interactor.getQuestions(context, this);
    }

    @Override
    public void sendAnswers(List<ColorQuestionsViewModel> resultList) {
        if (view != null) {
            view.showProgressDialog();
        }
        interactor.sendAnswersToServer(resultList, context, this);
    }

    @Override
    public void onSuccessSend(boolean extra) {
        if (view != null) {

            if (extra){
                view.hideProgressDialog();
                view.navigateToExtraTest();
                return;
            }

            view.hideProgressDialog();
            activity.startActivity(new Intent(activity, ResultActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
        }
    }

    @Override
    public void onErrorSend(String error) {
        if (view != null) {
            view.hideProgressDialog();
            view.showError(error);
        }
    }

    @Override
    public void onErrorGet(String error) {
        if (view != null) {
            view.showError(error);
        }

    }

    @Override
    public void updateData(List<ColorQuestionsViewModel> data) {
        if (view != null) {
            view.updateData(data);
        }
    }
}
