package mx.app.fashionme.presenter.interfaces;

import androidx.viewpager.widget.ViewPager;

import mx.app.fashionme.pojo.AnswerStyle;

/**
 * Created by heriberto on 3/04/18.
 */

public interface IStyleTestPresenter {
    void getQuestions();
    void sendAnswers(AnswerStyle answerStyle, ViewPager viewPager);

    void setAnalytics();
}
