package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

public interface IChecklistInteractor {
    void getDataFromAPi(Context context, int idJourney, ChecklistListener listener);

    void saveChecklist(ListView listView, int idJourney, Context context, ChecklistListener listener);

    interface ChecklistListener {
        void onGetJourney(ArrayList<Characteristic> list);
        void onError(String error);
        void onChecklistSaved();
    }
}
