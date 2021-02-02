package mx.app.fashionme.view.interfaces;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Created by heriberto on 3/04/18.
 */

public interface IStyleTestView {

    void getQuestions();
    void setAdapterViewPager(ArrayList<Fragment> fragments);
    void showErrors(String error);
    void showLoading(boolean show);
    void showPrevious(boolean show);
    void showNext(boolean show);
    void showSendButton(boolean show);
    void sendTestStyle();
    void goToHomePage(String style);

    void setUpPresenterStyleTest();

}
