package mx.app.fashionme.view;

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
import mx.app.fashionme.adapter.SubcategoryAdapter;
import mx.app.fashionme.interactor.SubcategoryInteractor;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.presenter.interfaces.ISubcategoryPresenter;
import mx.app.fashionme.presenter.SubcategoryPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ISubcategoryView;

public class SubcategoryActivity extends AppCompatActivity implements ISubcategoryView {

    // Miembros UI
    private Toolbar toolbar;
    private RecyclerView rvSubcategories;

    private LinearLayout llNetwork, llEmpty;

    private ISubcategoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        // Ui
        toolbar         = findViewById(R.id.miActionBarSubcats);
        rvSubcategories = findViewById(R.id.rvSubcategory);
        llEmpty         = findViewById(R.id.llEmptyListSubcats);
        llNetwork       = findViewById(R.id.llNetworkSubcats);

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenter();
            getSubcategories();
            presenter.setAnalytics();
        }
    }

    @Override
    public void generateLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSubcategories.setLayoutManager(llm);
    }

    @Override
    public SubcategoryAdapter createAdapter(ArrayList<Subcategory> subcategories) {
        SubcategoryAdapter adapter = new SubcategoryAdapter(subcategories, this);
        return adapter;
    }

    @Override
    public void initializeAdapter(SubcategoryAdapter adapter) {
        rvSubcategories.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvSubcategories.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOffline() {
        rvSubcategories.setVisibility(View.GONE);
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
    public void setUpPresenter() {
        presenter = new SubcategoryPresenter(this, new SubcategoryInteractor(), getApplicationContext(), SubcategoryActivity.this);
    }

    @Override
    public void getSubcategories() {
        Bundle parametros = getIntent().getExtras();
        int categoryId = parametros.getInt("idCategory");
        presenter.getSubcategories(categoryId);
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
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
