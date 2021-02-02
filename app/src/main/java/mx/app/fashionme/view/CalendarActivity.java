package mx.app.fashionme.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.CalendarInteractor;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.presenter.CalendarPresenter;
import mx.app.fashionme.presenter.interfaces.ICalendarPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ICalendarView;

public class CalendarActivity extends AppCompatActivity implements ICalendarView, OnDayClickListener {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private CalendarView calendarView;
    private Toolbar toolbar;

    private ICalendarPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView    = findViewById(R.id.calendarView);
        toolbar         = findViewById(R.id.toolbar_calendar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        Calendar calendar = Calendar.getInstance();
        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        setUpPresenterCalendar();
        getLooksCalendar();
        updateColors();

        calendarView.setOnDayClickListener(this);
        calendarView.setHeaderColor(Utils.getTabColor(this));
    }

    @Override
    public void setUpPresenterCalendar() {
        presenter = new CalendarPresenter( this, new CalendarInteractor(), CalendarActivity.this);
    }

    @Override
    public void getLooksCalendar() {
        presenter.getLooksWithDataFromDB();
    }

    @Override
    public void setEventsLook(List<EventDay> events) {
        calendarView.setEvents(events);
    }

    @Override
    public void showLookDialog(Look look) {
        final File file = new File(look.getUri());

        final Dialog fullScreenDialog = new Dialog(this, R.style.DialogFullscreen);
        fullScreenDialog.setContentView(R.layout.dialog_fullscreen);
        ImageView img_full_screen_dialog = fullScreenDialog.findViewById(R.id.img_full_screen_dialog);
        Picasso.get().load(file).into(img_full_screen_dialog);
        ImageView img_dialog_fullscreen_close = fullScreenDialog.findViewById(R.id.img_dialog_fullscreen_close);
        img_dialog_fullscreen_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenDialog.dismiss();
            }
        });
        fullScreenDialog.show();
    }

    @Override
    public void onDayClick(EventDay eventDay) {
        presenter.showLook(eventDay);
    }

    public void updateColors() {

        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(this));
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }
}
