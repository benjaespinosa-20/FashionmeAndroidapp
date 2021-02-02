package mx.app.fashionme.presenter.interfaces;

import java.util.List;

import mx.app.fashionme.pojo.ColorQuestionsViewModel;
import mx.app.fashionme.view.interfaces.IColorTestView;

/**
 * Created by heriberto on 22/03/18.
 */

public interface IColorTestPresenter {
    void setView(IColorTestView colorTestActivity);

    void loadData();

    void sendAnswers(List<ColorQuestionsViewModel> resultList);
}
