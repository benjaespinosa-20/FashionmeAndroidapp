package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import androidx.fragment.app.Fragment;

import java.util.List;

import mx.app.fashionme.pojo.QuestionColor;

public interface IExtraTestInteractor {
    void getQuestions(Context context, ExtraTestListener listener);
    void getFragment(Context context, String color, ExtraTestListener callback);

    void sendResultExtra(Context context, String color, ExtraTestListener callback);

    interface ExtraTestListener {
        void onSuccessColor(Fragment fragment, String tag);
        void onSuccessSendResult();
        void onError(String err);

        void updateData(List<QuestionColor> data);
    }

}
