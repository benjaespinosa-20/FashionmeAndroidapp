package mx.app.fashionme.presenter;

import android.content.Context;
import android.widget.RadioButton;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IQuestionStyleInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;
import mx.app.fashionme.presenter.interfaces.IQuestionStylePresenter;
import mx.app.fashionme.view.interfaces.IQuestionStyleView;

public class QuestionStylePresenter implements IQuestionStylePresenter, IQuestionStyleInteractor.OnGetQuestionListener {

    private IQuestionStyleView view;
    private IQuestionStyleInteractor interactor;
    private Context context;

    public QuestionStylePresenter(IQuestionStyleView view, IQuestionStyleInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }


    @Override
    public void getAnswers(QuestionStyle questionStyle) {
        interactor.getAnswers(context, questionStyle, this);
    }

    @Override
    public void onSuccess(String questionText, ArrayList<RadioButton> radioButtons) {
        if (view != null){
            view.setQuestionWithAnswers(questionText, radioButtons);
        }
    }

    @Override
    public void onError(String err) {
        if (view != null){
            view.setError(err);
        }

    }
}
