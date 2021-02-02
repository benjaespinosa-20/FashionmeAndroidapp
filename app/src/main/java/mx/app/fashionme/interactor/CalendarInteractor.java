package mx.app.fashionme.interactor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.app.fashionme.R;
import mx.app.fashionme.db.LooksConstructor;
import mx.app.fashionme.interactor.interfaces.ICalendarInteractor;
import mx.app.fashionme.pojo.Look;

public class CalendarInteractor implements ICalendarInteractor {

    private static final String TAG = CalendarInteractor.class.getSimpleName();

    @Override
    public void getDataFromDB(Context context, LooksListener listener) {
        LooksConstructor constructor = new LooksConstructor(context);

        if (constructor.getDataLooksWithDate() == null) {
            listener.onGetLooksError("No hay looks asignados al calendario.");
            return;
        }

        List<EventDay> events = new ArrayList<>();

        for (Look look:constructor.getDataLooks()) {

            if (look.getDate() != null){
                try {
                    String myFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(sdf.parse(look.getDate()));

                    Drawable d = Drawable.createFromPath(look.getUri());

                    events.add(new EventDay(calendar, R.drawable.ic_accessibility_black_24dp));
                    listener.onGetLooksSuccess(events);
                } catch (ParseException e) {
                    e.printStackTrace();
                    listener.onGetLooksError(e.getMessage());
                }
            }
        }
    }

    @Override
    public void getLookByDate(Context context, String date, LooksListener listener) {
        LooksConstructor constructor = new LooksConstructor(context);
        if (constructor.getLookByDate(date) == null) {
            listener.onGetLooksError("Ocurrio un error al traer looks");
            return;
        }
        listener.onGetLookByDate(constructor.getLookByDate(date));
    }
}
