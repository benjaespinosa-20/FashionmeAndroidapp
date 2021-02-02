package mx.app.fashionme.view;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.WayDressingAdapter;
import mx.app.fashionme.interactor.WayDressingInteractor;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.presenter.WayDressingPresenter;
import mx.app.fashionme.presenter.interfaces.IWayDressingPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IWayDressingView;

public class WayDressingActivity extends AppCompatActivity implements IWayDressingView {

    public static final String DRESS_CODE = "DRESS_CODE";

    // Miembros UI
    private Toolbar toolbar;
    private RecyclerView rvWays;

    private LinearLayout llNetwork, llEmpty;
    private CoordinatorLayout rootView;

    private IWayDressingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_dressing);

        // UI
        toolbar     = findViewById(R.id.toolbarWays);
        rvWays      = findViewById(R.id.rvWays);
        rootView    = findViewById(R.id.rootViewWays);
        llEmpty     = findViewById(R.id.llEmptyList);
        llNetwork   = findViewById(R.id.llNetwork);

        setUpToolbarWays();
        updateColors();

        if (!isOnlineWays()) {
            showOfflineWays();
        } else {
            setUpPresenterWays();
            getWaysDressing();
            presenter.setAnalytics(WayDressingActivity.this);
        }
    }

    @Override
    public void generateWayDressingLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvWays.setLayoutManager(llm);
    }

    @Override
    public WayDressingAdapter createWayDressingAdapter(ArrayList<WayDressing> ways) {
        return new WayDressingAdapter(ways, this);
    }

    @Override
    public void initializeWayDressingAdapter(WayDressingAdapter adapter) {
        rvWays.setAdapter(adapter);
    }

    @Override
    public void showEmptyListWays() {
        rvWays.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOfflineWays() {
        rvWays.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpToolbarWays() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public boolean isOnlineWays() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void setUpPresenterWays() {
        presenter = new WayDressingPresenter( this, new WayDressingInteractor(), this);
    }

    @Override
    public void getWaysDressing() {
        presenter.getWays();
    }

    @Override
    public void showErrorWays(String error) {
        Snackbar.make(rootView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getWays();
                    }
                })
                .show();
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
