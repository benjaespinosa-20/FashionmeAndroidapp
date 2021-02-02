package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
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

import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.TipAdapter;
import mx.app.fashionme.interactor.TipInteractor;
import mx.app.fashionme.pojo.Tip;
import mx.app.fashionme.presenter.TipPresenter;
import mx.app.fashionme.presenter.interfaces.ITipPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.ITipView;

public class TipActivity extends AppCompatActivity implements ITipView {

    public static final String ID_TIP = "id_tip";
    private Toolbar toolbar;
    private RecyclerView rvTip;
    private ProgressBar progressBarTip;

    private CoordinatorLayout rootView;
    private LinearLayout llEmptyListTip;

    private ITipPresenter presenter;
    private int idTip;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        toolbar         = findViewById(R.id.action_bar_tip);
        rvTip           = findViewById(R.id.rvTip);
        progressBarTip  = findViewById(R.id.progressBarTip);
        llEmptyListTip  = findViewById(R.id.llEmptyListTip);
        rootView    = findViewById(R.id.rootViewTip);

        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            idTip = parameters.getInt(ID_TIP, 0);
        }

        setToolbarTip();
        updateColors();
        setUpPresenterTip();
        getDataTip(idTip);
        presenter.setAnalytics(TipActivity.this);
    }

    @Override
    public void setUpPresenterTip() {
        presenter = new TipPresenter(this, new TipInteractor(), this);
    }

    @Override
    public void getDataTip(int idTip) {
        presenter.getTip(idTip);
    }

    @Override
    public void setToolbarTip() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public void showLoading(boolean show) {
        progressBarTip.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showEmpty(boolean show) {
        rvTip.setVisibility(show ? View.INVISIBLE:View.VISIBLE);
        llEmptyListTip.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void generateLinearLayoutVerticalTip() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTip.setLayoutManager(llm);
    }

    @Override
    public TipAdapter createTipAdapter(Tip tip) {
        this.idTip = tip.getId();
        return new TipAdapter(tip, this);
    }

    @Override
    public void initializeTipAdapter(TipAdapter adapter) {
        rvTip.setAdapter(adapter);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(rootView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.getTip(idTip);
                    }
                })
                .show();
    }

    @Override
    public void setButtonFav(boolean isFav) {
        if (isFav) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(TipActivity.this, R.drawable.ic_favorite));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav){
                        presenter.removeFavTip(idTip);
                    }
                    return false;
                }
            });
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(TipActivity.this, R.drawable.outline_favorite_border_white_24));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav){
                        presenter.addFavTip(idTip);
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void favAdded() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(TipActivity.this, R.drawable.ic_favorite));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav){
                    presenter.removeFavTip(idTip);
                }
                return false;
            }
        });
    }

    @Override
    public void favRemoved() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(TipActivity.this, R.drawable.outline_favorite_border_white_24));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav){
                    presenter.addFavTip(idTip);
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

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Message";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
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
