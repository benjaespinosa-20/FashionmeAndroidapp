package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.MyClosetAdapter;
import mx.app.fashionme.interactor.MyClosetInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.presenter.MyClosetPresenter;
import mx.app.fashionme.presenter.interfaces.IMyClosetPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IMyClosetView;

public class MyClosetActivity extends AppCompatActivity implements IMyClosetView {

    public static final String MY_CLOSET = "my-closet";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_my_closet)
    RecyclerView rvMyCloset;
    @BindView(R.id.root_my_closet)
    CoordinatorLayout rootMyCloset;

    private MyClosetAdapter adapter;
    private List<Clothe> resultList = new ArrayList<>();
    private IMyClosetPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);

        ButterKnife.bind(this);

        presenter = new MyClosetPresenter(new MyClosetInteractor(this), getApplicationContext());

        setupToolbar();
        updateColors();

        adapter = new MyClosetAdapter(resultList, this);
        rvMyCloset.setAdapter(adapter);
        rvMyCloset.setItemAnimator(new DefaultItemAnimator());
        rvMyCloset.setHasFixedSize(true);
        rvMyCloset.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        presenter.getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_continue, menu);
        menu.getItem(0).setTitle(R.string.item_home_recommendations);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mContinue:
                Intent intent = new Intent(this, RecommendationActivity.class);
                intent.putExtra(MY_CLOSET, true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateData(List<Clothe> data) {
        resultList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        Snackbar.make(rootMyCloset, error, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    public void addItem(View view) {
        startActivity(new Intent(this, RegisterClosetActivity.class));
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();

            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
            toolbar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
        }
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
