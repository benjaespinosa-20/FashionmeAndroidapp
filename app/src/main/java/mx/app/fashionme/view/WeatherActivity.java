package mx.app.fashionme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.WeatherInteractor;
import mx.app.fashionme.presenter.WeatherPresenter;
import mx.app.fashionme.presenter.interfaces.IWeatherPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IWeatherView;

public class WeatherActivity extends AppCompatActivity implements IWeatherView {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    public static final int REQUEST_GPS_PERMISSION_CODE = 101;

    // UI
    private TextView tvGrades;
    private TextView tvDescWeather;
    private TextView tvCityName;
    private ImageView imgIconWeather;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    // Values
    private Context context;
    private Activity activity;

    private IWeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // UI References
        imgIconWeather  = findViewById(R.id.imgIconWeather);
        tvGrades        = findViewById(R.id.tvGrades);
        tvDescWeather   = findViewById(R.id.tvDescWeather);
        tvCityName      = findViewById(R.id.tvCityName);
        progressBar     = findViewById(R.id.progressBar);
        toolbar         = findViewById(R.id.toolbar);

        context         = getApplicationContext();
        activity        = this;

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        updateColors();

        setUpPresenterWeather();
        presenter.checkConditions();
    }

    @Override
    public void setUpPresenterWeather() {
        presenter = new WeatherPresenter(this, new WeatherInteractor(), context, activity);
    }

    @Override
    public void showExplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String titleText = getString(R.string.allow_access_location);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage(R.string.message_allow_gps)
                .setNegativeButton(R.string.button_deny_access, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton(R.string.button_allow_access, new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS_PERMISSION_CODE:
                if (grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Do the stuff that requires permission...
                        presenter.checkConditions();
                    }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                        // Should we show an explanation?
                        presenter.showExplanation();
                    }
                }
                break;
        }
    }

    @Override
    public void setWeather(int resource, double temp, String desc, String city, String country) {
        tvGrades.setText(String.valueOf(temp) + "°C");
        tvDescWeather.setText(desc);
        tvCityName.setText(city + ", " + country);
        Picasso.get().load(resource).into(imgIconWeather);
    }

    @Override
    public void showErrors(String err) {
        Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
