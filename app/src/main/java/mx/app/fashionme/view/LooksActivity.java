package mx.app.fashionme.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.LookAdapter;
import mx.app.fashionme.interactor.LooksInteractor;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.presenter.LooksPresenter;
import mx.app.fashionme.presenter.interfaces.ILooksPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ILooksView;

public class LooksActivity extends AppCompatActivity implements ILooksView {

    private static final String TAG = LooksActivity.class.getSimpleName();

    private Toolbar toolbar;
    private RecyclerView rvLooks;
    private LookAdapter lookAdapter;

    private GridLayoutManager glm;
    private LinearLayout llmEmpty;

    private ILooksPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looks);

        toolbar = findViewById(R.id.toolbar);
        rvLooks = findViewById(R.id.rvLooks);
        llmEmpty    = findViewById(R.id.empty_layout);
        setUpToolbarLooks();
        setUpPresenterLooks();
        getLooks();
        presenter.setAnalytics();

        updateColors();
    }

    @Override
    public void generateGridLayout(int columns) {
        glm = new GridLayoutManager(getApplicationContext(), columns);
        rvLooks.setLayoutManager(glm);
    }

    @Override
    public LookAdapter createAdapterLooks(ArrayList<Look> looks) {
        return new LookAdapter(looks, this, this);
    }

    @Override
    public void initializeAdapterLooks(LookAdapter adapter) {
        this.lookAdapter = adapter;
        rvLooks.setAdapter(adapter);
    }

    @Override
    public void showEmptyLooks() {
        llmEmpty.setVisibility(View.VISIBLE);
        TextView tvTitle = llmEmpty.findViewById(R.id.empty_title);
        TextView tvDescription = llmEmpty.findViewById(R.id.empty_description);
        tvTitle.setText("Sin looks");
        tvDescription.setText("Crea un look y agregalo a tus looks para que se muestren aqui");
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUpToolbarLooks() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setUpPresenterLooks() {
        presenter = new LooksPresenter(this, new LooksInteractor(), getApplicationContext());
    }

    @Override
    public void getLooks() {
        presenter.getLooks();
    }

    @Override
    public void openCalendarDialog(final Look look) {

        // Android datepicker
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                presenter.saveDateLook(look, sdf.format(myCalendar.getTime()));
            }
        };

        new DatePickerDialog(LooksActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        /*
        // Material Calendar
        DatePickerBuilder builder = new DatePickerBuilder(this, new OnSelectDateListener() {
            @Override
            public void onSelect(List<Calendar> calendar) {

            }
        })
                .date(Calendar.getInstance())
                .pickerType(CalendarView.ONE_DAY_PICKER);

        DatePicker datePicker = builder.build();
        datePicker.show();
        */

    }

    @Override
    public void rvChange() {
        lookAdapter.notifyDataSetChanged();
    }

    private void updateColors() {
        if (toolbar!=null){
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

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
