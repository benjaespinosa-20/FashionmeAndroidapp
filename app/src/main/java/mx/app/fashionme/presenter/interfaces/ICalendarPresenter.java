package mx.app.fashionme.presenter.interfaces;

import com.applandeo.materialcalendarview.EventDay;

public interface ICalendarPresenter {

    void getLooksWithDataFromDB();
    void showLook(EventDay eventDay);
}
