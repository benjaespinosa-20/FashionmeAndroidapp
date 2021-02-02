package mx.app.fashionme.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Window;
import android.widget.FrameLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;

public class OtherSettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private int toolbarColor;
    private int iconColor;
    List<String> colorPrimaryList;
    List<String> colorPrimaryDarkList;
    SharedPreferences preferences;
    private final String TOOLBAR_COLOR_KEY = "toolbar-key";
    private final String ICON_COLOR_KEY = "icon-key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.pref_content)
    FrameLayout prefContent;

    private static Toolbar toolbarStatic;
    private static Window windowStatic;


    /**
     * Listener de valor de preferencia que actualiza el resumen de preferencia
     * para mostrar el nuevo valor
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Vincula el resumen de preferencia a su valor. Mas especifico, cuando el
     * valor de preferencia cambia, su resumen (linea de texto debajo del
     * titulo de preferencia) es actualizado para mostrar el valor. El resumen tambien
     * actualizado inmediatamente al llamar este metodo. El formato exacto de visualizacion es
     * dependiendo del tipo de preferencia.
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static void changeColors(int newColor, Context context) {
        toolbarStatic.setBackgroundColor(newColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            List<String> colorPrimaryList = Arrays.asList(context.getResources().getStringArray(R.array.color_choices));
            List<String> colorPrimaryDarkList = Arrays.asList(context.getResources().getStringArray(R.array.color_choices_700));

            String newColorString = getColorHexStatic(newColor);
            windowStatic.setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            windowStatic.setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }

    }

    private static String getColorHexStatic(int tabColor) {
        return String.format("#%02x%02x%02x", Color.red(tabColor), Color.green(tabColor), Color.blue(tabColor));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);
        ButterKnife.bind(this);

        toolbarStatic = toolbar;
        windowStatic = getWindow();

        setSupportActionBar(toolbar);
        setUpActionBar();
        setTitle(R.string.settings);

        colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbarColor = preferences.getInt(TOOLBAR_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimary));
        iconColor = preferences.getInt(ICON_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimaryDark));

        toolbar.setBackgroundColor(toolbarColor);

        updateStatusBarColor(toolbarColor);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.pref_content, new SettingsFragment())
                .commit();
    }

    private void updateStatusBarColor(int newColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(newColor);
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Metodo que determina si el dispositivo tiene una pantalla extra-large. Por
     * ejemplo, tablets de 10".
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public void setUpActionBar () {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private String getColorHex(int tabColor) {
        return String.format("#%02x%02x%02x", Color.red(tabColor), Color.green(tabColor), Color.blue(tabColor));
    }

    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        private static final String TAG = SettingsActivity.SettingsFragment.class.getSimpleName();

        public SettingsFragment() {

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_settings_general);
            bindPreferenceSummaryToValue(findPreference("language"));
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals("language")) {

                String selectedLang = sharedPreferences.getString(key, "");

                Locale locale = null;

                if (selectedLang.equals("0")) {
                    locale = new Locale("es", "ES");

                } else if (selectedLang.equals("1")) {
                    locale = new Locale("en", "US");
                }

                Locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                refresh();

            } else if (key.equals("toolbar-key")) {

                int newColor = sharedPreferences.getInt(key, ContextCompat.getColor(getContext(), R.color.colorPrimary));
                changeColors(newColor, getContext());
            }
        }

        private void refresh() {
            Intent refresh = new Intent(getActivity(), OtherSettingsActivity.class);
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(refresh);
            getActivity().finish();
        }

        @Override
        public void onResume() {
            super.onResume();
            // Registrar escucha
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Eliminar registro de la escucha
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }

}
