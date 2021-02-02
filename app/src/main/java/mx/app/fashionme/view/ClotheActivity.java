package mx.app.fashionme.view;

import android.content.SharedPreferences;
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

import mx.app.fashionme.adapter.ClotheAdapter;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.ClotheInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.ClothePresenter;
import mx.app.fashionme.presenter.interfaces.IClothePresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IClotheView;

public class ClotheActivity extends AppCompatActivity implements IClotheView {

    // UI Members
    private Toolbar toolbar;
    private RecyclerView rvClothes;

    private LinearLayout llNetwork, llEmpty;

    private boolean isClosetIdeal;
    private IClothePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothe);

        // UI
        toolbar     = findViewById(R.id.action_bar_clothe);
        rvClothes   = findViewById(R.id.rvClothe);
        llEmpty     = findViewById(R.id.llEmptyListClothe);
        llNetwork   = findViewById(R.id.llNetworkClothe);

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenterClothe();
            SharedPreferences sharedPreferences = getSharedPreferences(SessionPrefs.PREFS_FILE_NAME, 0);
            isClosetIdeal = sharedPreferences.getBoolean(SessionPrefs.KEY_CLOSET_IDEAL, false);
            getClothe();
            presenter.setAnalytics(isClosetIdeal);
        }

    }

    @Override
    public void generateLinearLayoutVertical() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClothes.setLayoutManager(linearLayoutManager);
    }

    @Override
    public ClotheAdapter createAdapter(ArrayList<Clothe> clothes) {
        ClotheAdapter clotheAdapter = new ClotheAdapter(clothes, this);
        return clotheAdapter;
    }

    @Override
    public void initializeAdapter(ClotheAdapter adapter) {
        rvClothes.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvClothes.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOffline() {
        rvClothes.setVisibility(View.GONE);
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
    public void setUpPresenterClothe() {
        presenter = new ClothePresenter(this, new ClotheInteractor(), getApplicationContext());
    }

    @Override
    public void getClothe() {
        Bundle parametros = getIntent().getExtras();
        int subcategoryId = parametros.getInt("idSubcategory");
        presenter.getClothes(subcategoryId, isClosetIdeal);
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
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
