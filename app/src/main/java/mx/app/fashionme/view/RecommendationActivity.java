package mx.app.fashionme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.RecommendationAdapter;
import mx.app.fashionme.interactor.RecommendationInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.presenter.RecommendationPresenter;
import mx.app.fashionme.presenter.interfaces.IRecommendationPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IRecommendationView;

public class RecommendationActivity extends AppCompatActivity implements IRecommendationView {

    public static final int REQUEST_GPS_PERMISSION_CODE = 101;

    @BindView(R.id.fab_cart_recommended)
    FloatingActionButton fabCart;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    
    private static final String TAG = RecommendationActivity.class.getSimpleName();

    // UI Members
    private Toolbar toolbar;
    private RecyclerView rvRecommendations;

    private LinearLayout llNetwork, llEmpty;

    private IRecommendationPresenter presenter;

    private ArrayList<Clothe> clothesAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // UI References
        toolbar             = findViewById(R.id.toolbar);
        rvRecommendations   = findViewById(R.id.rvRecommendations);
        llEmpty             = findViewById(R.id.llEmptyList);
        llNetwork           = findViewById(R.id.llNetwork);

        clothesAll          = new ArrayList<>();

        ButterKnife.bind(this);

        setUpToolbar();
        updateColors();

        if (!isOnline()) {
            showOffline();
        } else {
            setUpPresenterRecommendations();
            getRecommendations();
            presenter.setSAnalytics();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecommendations();
                swipe.setRefreshing(false);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra(MyClosetActivity.MY_CLOSET, false)){
                fabCart.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void generateGridLayout(int columns) {
        GridLayoutManager glm = new GridLayoutManager(this, columns);
        rvRecommendations.setLayoutManager(glm);
    }

    @Override
    public RecommendationAdapter createAdapter(ArrayList<Clothe> clothes) {

        clothesAll.addAll(clothes);

        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveAllToCart(clothesAll);
            }
        });

        return new RecommendationAdapter(clothes, this);
    }

    @Override
    public void initializeAdapter(RecommendationAdapter adapter) {
        rvRecommendations.setAdapter(adapter);
    }

    @Override
    public void showEmptyList() {
        rvRecommendations.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOffline() {
        rvRecommendations.setVisibility(View.GONE);
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
    public void setUpPresenterRecommendations() {
        presenter = new RecommendationPresenter(this, new RecommendationInteractor(), getApplicationContext(), RecommendationActivity.this);
    }

    @Override
    public void getRecommendations() {
        //presenter.getRecommendations();
        presenter.checkConditions();
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFabCart() {
        fabCart.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
        rvRecommendations.setVisibility(show ? View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public void showExplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String title = getString(R.string.allow_access_location);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage(R.string.message_allow_gps)
                .setNegativeButton(R.string.button_deny_access, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RecommendationActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.button_allow_access, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_PERMISSION_CODE);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void showNeverAskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String titleText = "¿Permitir que Fashion Me tenga acceso a tu ubicación?";
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage("Fashion Me usa la ubicación para activar algunas funciones, " +
                        "ayudarte a tener sugerencias de ropa, conocer el clima y mucho más." + "\n" +
                        "\nPara permitir el acceso, haz clic en \"Configuración de aplicaciones\" " +
                        "a continuación y activa \"Ubicación\" en el menú \"Permisos\".")
                .setNegativeButton("AHORA NO", null)
                .setPositiveButton("CONFIGURACIÓN DE APLICACIONES", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void showAlertGPS() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Activa tu localizacion")
                .setMessage("La ubicacion esta desactivada. \nPor favor activa la ubicacion " +
                        "para usar esta app.")
                .setPositiveButton("Configuracion de ubicacion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        presenter.checkConditions();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        presenter.showExplanation();
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopLocationUpdates();
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
