package mx.app.fashionme.view.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.ColorQuestionsViewModel;

/**
 * Created by heriberto on 22/03/18.
 */

public interface IColorTestView {

    void showError(String error);

    void showProgressDialog();

    void hideProgressDialog();

    void navigateToExtraTest();

    void updateData(List<ColorQuestionsViewModel> data);
}
