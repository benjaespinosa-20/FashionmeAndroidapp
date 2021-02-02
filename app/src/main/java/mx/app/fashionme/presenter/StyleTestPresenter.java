package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.fragment.QuestionStyleFragment;
import mx.app.fashionme.interactor.interfaces.IStyleTestInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;
import mx.app.fashionme.presenter.interfaces.IStyleTestPresenter;
import mx.app.fashionme.view.interfaces.IStyleTestView;

/**
 * Created by heriberto on 3/04/18.
 */

public class StyleTestPresenter implements IStyleTestPresenter, IStyleTestInteractor.OnGetStyleQuestionListener, IStyleTestInteractor.OnSendAnswersFinishedListener {

    public static final String QUESTION = "QUESTION";
    public static final String ANSWERS = "ANSWERS";
    private IStyleTestView view;
    private IStyleTestInteractor interactor;
    private Context context;
    private AnswerStyle answerStyle;

    public StyleTestPresenter(IStyleTestView view, IStyleTestInteractor interactor, Context context, AnswerStyle answerStyle) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
        this.answerStyle = answerStyle;
    }


    @Override
    public void getQuestions() {
        view.showLoading(true);
        interactor.getQuestions(context, this);
    }

    @Override
    public void sendAnswers(AnswerStyle answerStyle, ViewPager viewPager) {
        view.showLoading(true);
        interactor.sendStyleAnswers(context, answerStyle, viewPager, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle params = new Bundle();
        params.putString("screen_name", "Cuestionario de estilo/Android");
        firebaseAnalytics.logEvent("open_screen", params);
    }

    @Override
    public void onSuccess(ArrayList<QuestionStyle> questions) {
        if (view != null){
            view.showLoading(false);
            ArrayList<Fragment> fragments = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                Bundle args = new Bundle();
                args.putParcelable(QUESTION, questions.get(i));
                args.putParcelable(ANSWERS, answerStyle);
                QuestionStyleFragment fragment = new QuestionStyleFragment();
                fragment.setArguments(args);
                fragments.add(fragment);
            }
            view.setAdapterViewPager(fragments);
            view.showNext(true);
        }
    }

    @Override
    public void onError(String error) {
        if (view != null){
            view.showLoading(false);
            view.showPrevious(false);
            view.showNext(false);
            view.showSendButton(false);
            view.showErrors(error);
        }

    }

    @Override
    public void onSuccessSend(String style) {
        if (view != null){
            view.goToHomePage(style);
        }
    }

    @Override
    public void onFailSend(String error) {
        if (view != null){
            view.showLoading(false);
            view.showSendButton(false);
            view.showErrors(error);
        }
    }
}
