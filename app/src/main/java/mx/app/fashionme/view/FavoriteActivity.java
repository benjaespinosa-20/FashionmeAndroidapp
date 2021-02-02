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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.FavoriteAdapter;
import mx.app.fashionme.interactor.FavoriteInteractor;
import mx.app.fashionme.pojo.Favorite;
import mx.app.fashionme.presenter.FavoritePresenter;
import mx.app.fashionme.presenter.interfaces.IFavoritePresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IFavoriteView;

public class FavoriteActivity extends AppCompatActivity implements IFavoriteView {

    private Toolbar toolbar;
    private RecyclerView rvFavs;

    private LinearLayout llNetwork, llEmpty;

    private IFavoritePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothe_favorite);

        // UI
        toolbar     = findViewById(R.id.action_bar_clothe);
        rvFavs      = findViewById(R.id.rvFavs);
        llEmpty     = findViewById(R.id.empty_layout);
        llNetwork   = findViewById(R.id.llNetworkClothe);

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenterClothe();
            getClothe();
            presenter.setAnalytics();
        }
    }

    @Override
    public void generateLinearLayoutVertical() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFavs.setLayoutManager(linearLayoutManager);
    }

    @Override
    public FavoriteAdapter createAdapter(ArrayList<Favorite> favorites) {
        return new FavoriteAdapter(favorites, this);
    }

    @Override
    public void initializeAdapter(FavoriteAdapter adapter) {
        rvFavs.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvFavs.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);

        TextView tvTitle = llEmpty.findViewById(R.id.empty_title);
        TextView tvDescription = llEmpty.findViewById(R.id.empty_description);
        tvTitle.setText("Sin favoritos");
        tvDescription.setText("Agrega tu ropa favorita y se mostrara aqui");
    }

    @Override
    public void showOffline() {
        rvFavs.setVisibility(View.GONE);
        llNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
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
        presenter = new FavoritePresenter( this, new FavoriteInteractor(), getApplicationContext());

    }

    @Override
    public void getClothe() {
        presenter.getClothes();
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    private void updateColors() {
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
