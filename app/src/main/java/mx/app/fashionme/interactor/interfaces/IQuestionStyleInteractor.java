package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import android.widget.RadioButton;

import java.util.ArrayList;

import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;

public interface IQuestionStyleInteractor {

    void getAnswers(Context context, QuestionStyle questionStyle, OnGetQuestionListener callback);
    void setAnswer(RadioButton radioButton);

    interface OnGetQuestionListener {
        void onSuccess(String questionText, ArrayList<RadioButton> radioButtons);
        void onError(String err);
    }
}
