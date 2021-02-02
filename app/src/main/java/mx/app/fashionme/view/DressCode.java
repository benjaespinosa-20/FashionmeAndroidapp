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
import mx.app.fashionme.adapter.DressCodeAdapter;
import mx.app.fashionme.interactor.DressCodeInteractor;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.presenter.DressCodePresenter;
import mx.app.fashionme.presenter.interfaces.IDressCodePresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IDressCodeView;

public class DressCode extends AppCompatActivity implements IDressCodeView {

    private Toolbar toolbar;
    private RecyclerView rvDC;
    private ProgressBar progressBar;

    private CoordinatorLayout rootView;
    private LinearLayout llEmptyListDC;

    private IDressCodePresenter presenter;

    private int idDressCode;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dress_code);

        toolbar         = findViewById(R.id.action_bar_dressCode);
        rvDC            = findViewById(R.id.rvDressCode);
        progressBar     = findViewById(R.id.progressBarDressCode);
        llEmptyListDC   = findViewById(R.id.llEmptyListDressCode);
        rootView        = findViewById(R.id.rootViewDressCode);

        Bundle bundle = getIntent().getExtras();
        idDressCode = bundle != null ? bundle.getInt(WayDressingActivity.DRESS_CODE, 0) : 0;

        setToolbarDressCode();
        updateColors();
        setUpPresenterDressCode();
        getDataDressCode();
    }

    @Override
    public void setUpPresenterDressCode() {
        presenter = new DressCodePresenter( this, new DressCodeInteractor(), getApplicationContext());
    }

    @Override
    public void getDataDressCode() {
        presenter.getDataDressCode(idDressCode);
    }

    @Override
    public void setToolbarDressCode() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showEmpty(boolean show) {
        rvDC.setVisibility(show ? View.INVISIBLE:View.VISIBLE);
        llEmptyListDC.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void generateLinearLayoutDressCode() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDC.setLayoutManager(llm);
    }

    @Override
    public DressCodeAdapter createAdapter(WayDressing wayDressing) {
        return new DressCodeAdapter(wayDressing, this);
    }

    @Override
    public void initializeAdapter(DressCodeAdapter adapter) {
        rvDC.setAdapter(adapter);
    }

    @Override
    public void showError(String err) {
        Snackbar.make(rootView, err, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.getDataDressCode(idDressCode);
                    }
                })
                .show();
    }

    @Override
    public void setButtonFav(boolean isFav) {
        if (isFav) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(DressCode.this, R.drawable.ic_favorite));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav) {
                        presenter.removeFavWay(idDressCode);
                    }
                    return false;
                }
            });
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(DressCode.this, R.drawable.outline_favorite_border_white_24));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav) {
                        presenter.addFavWay(idDressCode);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void favAdded() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(DressCode.this, R.drawable.ic_favorite));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav) {
                    presenter.removeFavWay(idDressCode);
                }
                return false;
            }
        });
    }

    @Override
    public void favRemoved() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(DressCode.this, R.drawable.outline_favorite_border_white_24));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav) {
                    presenter.addFavWay(idDressCode);
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