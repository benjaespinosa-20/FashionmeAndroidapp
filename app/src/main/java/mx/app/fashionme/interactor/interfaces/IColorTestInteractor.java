package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import java.util.List;

import mx.app.fashionme.pojo.ColorQuestionsViewModel;

/**
 * Created by heriberto on 22/03/18.
 */

public interface IColorTestInteractor {

    void getQuestions(Context context, TestListener listener);

    void sendAnswersToServer(List<ColorQuestionsViewModel> resultList, Context context, TestListener listener);

    interface TestListener {
        void onSuccessSend(boolean extra);
        void onErrorSend(String error);

        void onErrorGet(String error);

        void updateData(List<ColorQuestionsViewModel> data);
    }
}
