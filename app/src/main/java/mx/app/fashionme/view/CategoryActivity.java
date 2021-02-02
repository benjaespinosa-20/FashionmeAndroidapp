package mx.app.fashionme.view;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.CategoryAdapter;
import mx.app.fashionme.interactor.CategoryInteractor;
import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.presenter.CategoryPresenter;
import mx.app.fashionme.presenter.interfaces.ICategoryPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ICategoryView;

public class CategoryActivity extends AppCompatActivity implements ICategoryView {

    // Miembros UI
    private Toolbar toolbar;
    private RecyclerView rvCategories;
    private LinearLayout llNetwork, llEmpty;
    private ProgressBar progressBar;

    private ICategoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // UI
        toolbar         = findViewById(R.id.miActionBar);
        rvCategories    = findViewById(R.id.rvCategory);
        llEmpty         = findViewById(R.id.llEmptyList);
        llNetwork       = findViewById(R.id.llNetwork);
        progressBar     = findViewById(R.id.progressBar);

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenter();
            getCategories();
            presenter.setAnalytics();
        }

    }

    @Override
    public void generateLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvCategories.setLayoutManager(llm);
    }

    @Override
    public CategoryAdapter createAdapter(ArrayList<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(categories, this);
        return adapter;
    }

    @Override
    public void initializeAdapter(CategoryAdapter adapter) {
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvCategories.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOffline() {
        rvCategories.setVisibility(View.GONE);
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
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void setUpPresenter() {
        presenter = new CategoryPresenter(this, new CategoryInteractor(), getApplicationContext(), CategoryActivity.this);
    }

    @Override
    public void getCategories() {
        presenter.getCategories();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE:View.GONE);
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
