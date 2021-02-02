package mx.app.fashionme.view.interfaces;

import android.widget.RadioButton;

import java.util.ArrayList;

import mx.app.fashionme.pojo.AnswerStyle;

public interface IQuestionStyleView {

    void setQuestionWithAnswers(String question, ArrayList<RadioButton> answers);
    void clickAnswer(RadioButton radioButton);
    void setError(String error);
    void setUpPresenterQuestionsStyle();
}
