package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.TrendAdapter;
import mx.app.fashionme.interactor.TrendInteractor;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.presenter.TrendPresenter;
import mx.app.fashionme.presenter.interfaces.ITrendPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ITrendView;

public class TrendActivity extends AppCompatActivity implements ITrendView {

    private static final String TAG = TrendActivity.class.getSimpleName();
    public static final String ID_TREND = "id_trend";
    private Toolbar toolbar;
    private RecyclerView rvTrend;
    private ProgressBar progressBar;

    private LinearLayout llEmptyListTrend;

    private ITrendPresenter presenter;
    private int idTrend;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        toolbar             = findViewById(R.id.action_bar_trend);
        rvTrend             = findViewById(R.id.rvTrend);
        progressBar         = findViewById(R.id.progressBarTrend);
        llEmptyListTrend    = findViewById(R.id.llEmptyListTrend);

        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            idTrend = parameters.getInt(ID_TREND, 0);
        }

        setToolbar();
        updateColors();
        setUpPresenter();
        getData(idTrend);
        presenter.setAnalytics(TrendActivity.this);
    }


    @Override
    public void setUpPresenter() {
        presenter = new TrendPresenter(this, new TrendInteractor(), this);
    }

    @Override
    public void getData(int idTrend) {
        presenter.getTrend(idTrend);
    }

    @Override
    public void setToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE: View.INVISIBLE);
    }

    @Override
    public void showEmpty(boolean show) {
        rvTrend.setVisibility(show ? View.INVISIBLE: View.VISIBLE);
        llEmptyListTrend.setVisibility(show ? View.VISIBLE: View.INVISIBLE);
    }

    @Override
    public void generateLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTrend.setLayoutManager(llm);
    }

    @Override
    public TrendAdapter createAdapter(Trend trend) {
        this.idTrend = trend.getId();
        return new TrendAdapter(trend, this);
    }

    @Override
    public void initializeAdapter(TrendAdapter adapter) {
        rvTrend.setAdapter(adapter);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setButtonFav(boolean isFav) {
        if (isFav) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(TrendActivity.this, R.drawable.ic_favorite));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav){
                        presenter.removeFavTrend(idTrend);
                    }
                    return false;
                }
            });
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(TrendActivity.this, R.drawable.outline_favorite_border_white_24));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav){
                        presenter.addTrendToFav(idTrend);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void favAdded() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(TrendActivity.this, R.drawable.ic_favorite));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav){
                    presenter.removeFavTrend(idTrend);
                }
                return false;
            }
        });
    }

    @Override
    public void favRemoved() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(TrendActivity.this, R.drawable.outline_favorite_border_white_24));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav){
                    presenter.addTrendToFav(idTrend);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.action_menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "message goes here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
