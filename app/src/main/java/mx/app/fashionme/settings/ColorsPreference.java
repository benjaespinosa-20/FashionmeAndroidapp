package mx.app.fashionme.settings;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kizitonwose.colorpreference.ColorDialog;
import com.kizitonwose.colorpreference.ColorShape;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;

public class ColorsPreference extends AppCompatActivity implements ColorDialog.OnColorSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.colorButton)
    Button colorButton;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private int toolbarColor;
    private int iconColor;
    List<String> colorPrimaryList;
    List<String> colorPrimaryDarkList;
    SharedPreferences preferences;
    private final String TOOLBAR_COLOR_KEY = "toolbar-key";
    private final String ICON_COLOR_KEY = "icon-key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle("Colores");

        colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbarColor = preferences.getInt(TOOLBAR_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimary));
        iconColor = preferences.getInt(ICON_COLOR_KEY, ContextCompat.getColor(this, R.color.colorPrimaryDark));

        toolbar.setBackgroundColor(toolbarColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(iconColor));

        updateStatusBarColor(toolbarColor);
    }

    @OnClick(R.id.colorButton)
    public void changeToolbarColor(){
        showColorDialog(toolbar);
    }

    @OnClick(R.id.fab)
    public void changeIconsColor(View view) {
        showColorDialog(view);
    }

    private void showColorDialog(View v) {
        new ColorDialog.Builder(this)
                .setColorShape(v instanceof Toolbar ? ColorShape.SQUARE : ColorShape.CIRCLE)
                .setColorChoices(R.array.color_choices)
                .setTag(String.valueOf(v instanceof Toolbar ? R.id.toolbar : R.id.fab))
                .setSelectedColor(v instanceof Toolbar ? toolbarColor : iconColor)
                .show();
    }

    private void updateStatusBarColor(int newColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(newColor);
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    public void onColorSelected(int newColor, String tag) {
        View view = findViewById(Integer.parseInt(tag));

        if (view instanceof Toolbar) {
            toolbar.setBackgroundColor(newColor);
            toolbarColor = newColor;
            preferences.edit().putInt(TOOLBAR_COLOR_KEY, newColor).apply();
            // change the status bar color
            updateStatusBarColor(newColor);
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(newColor));
            iconColor = newColor;
            preferences.edit().putInt(ICON_COLOR_KEY, newColor).apply();
        }


    }
}
