package mx.app.fashionme.presenter.interfaces;

import android.widget.ListView;

public interface IChecklistPresenter {
    void getChecklist(int idJourney);
    void saveChecklist(ListView listView, int idJourney);

    void setAnalytics();
}
