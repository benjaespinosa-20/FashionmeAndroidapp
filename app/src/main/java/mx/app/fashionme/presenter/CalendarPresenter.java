package mx.app.fashionme.presenter;

import android.content.Context;
import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mx.app.fashionme.interactor.interfaces.ICalendarInteractor;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.presenter.interfaces.ICalendarPresenter;
import mx.app.fashionme.view.interfaces.ICalendarView;

public class CalendarPresenter implements ICalendarPresenter, ICalendarInteractor.LooksListener {

    private static final String TAG = CalendarPresenter.class.getSimpleName();

    private ICalendarView view;
    private ICalendarInteractor interactor;
    private Context context;

    public CalendarPresenter(ICalendarView view, ICalendarInteractor interactor, Context context) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
    }


    @Override
    public void getLooksWithDataFromDB() {
        interactor.getDataFromDB(context, this);
    }

    @Override
    public void showLook(EventDay eventDay) {
//        if (eventDay.getImageResource() != 0){
            Calendar clickedDayCalendar = eventDay.getCalendar();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String calendarString = simpleDateFormat.format(clickedDayCalendar.getTime());
            interactor.getLookByDate(context, calendarString, this);
//        }
    }

    @Override
    public void onGetLooksSuccess(List<EventDay> events) {
        if (view != null) {
            Log.d(TAG, "Success");
            view.setEventsLook(events);
        }
    }

    @Override
    public void onGetLooksError(String error) {
        if (view != null) {
            Log.d(TAG, error);
        }
    }

    @Override
    public void onGetLookByDate(ArrayList<Look> looks) {
        for (Look look: looks) {
            if (view != null) {
                view.showLookDialog(look);
            }
        }
    }
}
