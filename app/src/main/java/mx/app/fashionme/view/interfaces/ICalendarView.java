package mx.app.fashionme.view.interfaces;

import com.applandeo.materialcalendarview.EventDay;

import java.util.List;

import mx.app.fashionme.pojo.Look;

public interface ICalendarView {

    void setUpPresenterCalendar();
    void getLooksCalendar();
    void setEventsLook(List<EventDay> events);
    void showLookDialog(Look look);
}
