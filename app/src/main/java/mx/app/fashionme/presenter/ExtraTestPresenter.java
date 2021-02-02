package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IExtraTestInteractor;
import mx.app.fashionme.pojo.QuestionColor;
import mx.app.fashionme.presenter.interfaces.IExtraTestPresenter;
import mx.app.fashionme.view.ResultActivity;
import mx.app.fashionme.view.interfaces.IExtraTestView;

public class ExtraTestPresenter implements IExtraTestPresenter, IExtraTestInteractor.ExtraTestListener {

    private IExtraTestView view;
    private IExtraTestInteractor interactor;
    private Context context;
    private Activity activity;


    public ExtraTestPresenter(IExtraTestView view, IExtraTestInteractor interactor, Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }

    @Override
    public void loadData() {
        interactor.getQuestions(context, this);
    }

    @Override
    public void getFragment(String color) {
        interactor.getFragment(context, color, this);
    }

    @Override
    public void sendResultExtra(String color) {
        view.showSendingResult(true);
        interactor.sendResultExtra(context, color, this);
    }


    @Override
    public void onSuccessColor(Fragment fragment, String tag) {
        if (view != null) {
            view.showSendingResult(false);
            view.setFragment(fragment, tag);
        }
    }

    @Override
    public void onSuccessSendResult() {
        if (view != null) {
            view.showSendingResult(false);
            activity.startActivity(new Intent(activity, ResultActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
            //view.goToHome();
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showSendingResult(false);
            view.showError(err);
        }
    }

    @Override
    public void updateData(List<QuestionColor> data) {
        if (view != null) {

            for (QuestionColor question:data) {
                if (question.getType().equals("alt")) {
                    switch (context.getString(R.string.app_language)) {
                        case "spanish":
                            view.setQuestion(question.getSpanish().getQuestion());
                            view.setAnswers(question.getAnswers().getData());
                            break;
                        case "english":
                            view.setQuestion(question.getEnglish().getQuestion());
                            view.setAnswers(question.getAnswers().getData());
                            break;
                            default:
                                view.setQuestion(question.getSpanish().getQuestion());
                                view.setAnswers(question.getAnswers().getData());
                                break;
                    }
                }
            }

        }
    }
}
