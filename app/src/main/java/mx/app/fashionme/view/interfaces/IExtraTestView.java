package mx.app.fashionme.view.interfaces;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import mx.app.fashionme.pojo.AnswerColor;

public interface IExtraTestView {

    void setListenerRG();
    void setFragment(Fragment fragment, String tag);
    void getFragmentTest(String color);
    void showError(String err);
    void showSendingResult(boolean show);


    void setQuestion(String title);

    void setAnswers(ArrayList<AnswerColor> data);
}
