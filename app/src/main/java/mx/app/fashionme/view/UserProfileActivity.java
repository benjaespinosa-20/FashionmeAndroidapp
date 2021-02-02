package mx.app.fashionme.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.settings.SettingsActivity;
import mx.app.fashionme.utils.Utils;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private Toolbar toolbar;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvGenreProfile;
    private TextView tvColorProfile;
    private TextView tvBodyProfile;
    private TextView tvFaceProfile;
    private TextView tvStyleProfile;
    private ImageView imgPalette;
    private ImageView ivUserImage;
    private RelativeLayout btnPhotoProfile;
    private RelativeLayout btnMyCloset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar_profile);
        AppBarLayout appBar = findViewById(R.id.app_bar);

        tvName          = findViewById(R.id.tvName);
        tvEmail         = findViewById(R.id.tvEmail);
        tvGenreProfile  = findViewById(R.id.tvGenreProfile);
        tvColorProfile  = findViewById(R.id.tvColorProfile);
        tvBodyProfile   = findViewById(R.id.tvBodyProfile);
        tvFaceProfile   = findViewById(R.id.tvFaceProfile);
        tvStyleProfile  = findViewById(R.id.tvStyleProfile);
        imgPalette      = findViewById(R.id.imgPalette);
        ivUserImage     = findViewById(R.id.ivUserImage);
        btnPhotoProfile = findViewById(R.id.btnPhotoProfile);
        btnMyCloset     = findViewById(R.id.btnMyCloset);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            View view = getLayoutInflater().inflate(R.layout.logo_toolbar, null);
            toolbar.addView(view);
            toolbar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
        }

        appBar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));

        tvName.setText(Utils.toCapitalName(SessionPrefs.get(this).getUserName().toLowerCase()));
        tvEmail.setText(SessionPrefs.get(this).getEmail());
        String lang = getResources().getString(R.string.app_language);
        switch (lang) {
            case "spanish":
                if (SessionPrefs.get(getApplicationContext()).getGenre().equals("woman")){
                    tvGenreProfile.setText(("Mujer"));
                } else if (SessionPrefs.get(getApplicationContext()).getGenre().equals("man")){
                    tvGenreProfile.setText(("Hombre"));
                }
                break;
            case "english":
                tvGenreProfile.setText(Utils.toCapitalName(SessionPrefs.get(getApplicationContext()).getGenre()));
                break;
            default:
                if (SessionPrefs.get(getApplicationContext()).getGenre().equals("woman")){
                    tvGenreProfile.setText(("Mujer"));
                } else if (SessionPrefs.get(getApplicationContext()).getGenre().equals("man")){
                    tvGenreProfile.setText(("Hombre"));
                }
                break;
        }

        String color = SessionPrefs.get(getApplicationContext()).getColor(getString(R.string.app_language));
        tvColorProfile.setText(color);
        int palette = 0;
        if (color != null){
            switch (color.toUpperCase()) {
                case "PRIMAVERA":
                    palette = R.drawable.spring_palette;
                    break;
                case "VERANO":
                    palette = R.drawable.spring_palette;
                    break;
                case "OTOÑO":
                    palette = R.drawable.fall_palette;
                    break;
                case "INVIERNO":
                    palette = R.drawable.winter_palette;
                    break;
            }

            if (palette != 0){
                Picasso.get().load(palette).into(imgPalette);
            }
        }



        tvBodyProfile.setText(SessionPrefs.get(this).getBodyType(getString(R.string.app_language)));

        tvFaceProfile.setText(SessionPrefs.get(this).getFaceType(getString(R.string.app_language)));

        String style = SessionPrefs.get(this).getStyle(lang);
        if (style == null) {
            tvStyleProfile.setText(R.string.not_answer_test_style);
        } else {
            tvStyleProfile.setText(style);
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String photUri = sharedPref.getString("pref_photo", null);
        if (photUri != null) {
            ivUserImage.setImageURI(Uri.parse(photUri));
        }

        btnPhotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, SettingsActivity.class));
            }
        });

        btnMyCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, MyClosetActivity.class));
            }
        });

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Perfil/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);

        updateStatusBarColor();
    }

    private void updateStatusBarColor() {
        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(getApplicationContext()));
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }
}
