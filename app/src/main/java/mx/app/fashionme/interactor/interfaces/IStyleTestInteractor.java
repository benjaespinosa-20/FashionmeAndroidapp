package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;

/**
 * Created by heriberto on 3/04/18.
 */

public interface IStyleTestInteractor {
    void getQuestions(Context context, OnGetStyleQuestionListener callback);
    void sendStyleAnswers(Context context, AnswerStyle answerStyle, ViewPager viewPager, OnSendAnswersFinishedListener callback);
    String getGenre(Context context);

    interface OnGetStyleQuestionListener {
        void onSuccess(ArrayList<QuestionStyle> questions);
        void onError(String error);
    }

    interface OnSendAnswersFinishedListener {
        void onSuccessSend(String style);
        void onFailSend(String error);
    }
}
