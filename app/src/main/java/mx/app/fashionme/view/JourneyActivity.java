package mx.app.fashionme.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.JourneyListAdapter;
import mx.app.fashionme.interactor.JourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.presenter.JourneyPresenter;
import mx.app.fashionme.presenter.interfaces.IJourneyPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IJourneyView;

public class JourneyActivity extends AppCompatActivity implements IJourneyView {

    private Toolbar toolbar;
    private RecyclerView rvJourneys;
    private LinearLayout llNetwork, llEmpty;
    private Context context;
    private Activity activity;
    private IJourneyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        toolbar     = findViewById(R.id.action_bar);
        rvJourneys  = findViewById(R.id.rvJourney);
        llEmpty     = findViewById(R.id.llEmptyList);
        llNetwork   = findViewById(R.id.llNetwork);

        context     = getApplicationContext();
        activity    = JourneyActivity.this;

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenterJourneyList();
            getJourneys();
            presenter.setAnalytics(JourneyActivity.this);
        }
    }

    @Override
    public void generateLinearLayoutJourney() {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvJourneys.setLayoutManager(llm);
    }

    @Override
    public JourneyListAdapter createAdapter(ArrayList<Journey> journeys) {
        return new JourneyListAdapter(journeys, activity);
    }

    @Override
    public void initializeJourneyListAdapter(JourneyListAdapter adapter) {
        rvJourneys.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvJourneys.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOffline() {
        rvJourneys.setVisibility(View.GONE);
        llNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void setUpPresenterJourneyList() {
        presenter = new JourneyPresenter(this, new JourneyInteractor(), context);
    }

    @Override
    public void getJourneys() {
        presenter.getJourneys();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
