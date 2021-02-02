package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.pojo.Look;

public interface ICalendarInteractor {
    void getDataFromDB(Context context, LooksListener listener);
    void getLookByDate(Context context, String date, LooksListener listener);

    interface LooksListener {
        void onGetLooksSuccess(List<EventDay> events);
        void onGetLooksError(String error);
        void onGetLookByDate(ArrayList<Look> looks);
    }
}
