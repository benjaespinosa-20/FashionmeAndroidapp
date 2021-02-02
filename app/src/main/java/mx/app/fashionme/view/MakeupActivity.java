package mx.app.fashionme.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.RecyclerViewBaseAdapter;
import mx.app.fashionme.interactor.MakeupInteractor;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.presenter.MakeupPresenter;
import mx.app.fashionme.presenter.interfaces.IMakeupPresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IMakeupView;

public class MakeupActivity extends AppCompatActivity implements IMakeupView {

    @BindView(R.id.rvMakeups)
    RecyclerView recyclerView;

    @BindView(R.id.srlSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.llErrors)
    LinearLayout llErrors;

    @BindView(R.id.tvErrorTitle)
    TextView tvErrorTitle;

    @BindView(R.id.tvErrorDescription)
    TextView tvErrorDescription;

    @BindView(R.id.btnTryAgain)
    Button btnTryAgain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private RecyclerViewBaseAdapter adapter;

    private IMakeupPresenter presenter;

    private String typeMH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup);

        ButterKnife.bind(this);
        setTypeMH(getIntent().getStringExtra(Constants.KEY_MH));
        presenter = new MakeupPresenter(getApplicationContext(), this, new MakeupInteractor());
        setupRecyclerView();
        setupToolbar();
        updateColors();
        swipeRefreshLayout.setOnRefreshListener(presenter.getOnRefreshListener());
        presenter.setAnalytics(MakeupActivity.this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(getLayoutManager());
    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );
    }

    private RecyclerViewBaseAdapter getAdapter(ArrayList<Makeup> data, String typeMH) {
        ArrayList<Object> list = new ArrayList<>(data);
        return new RecyclerViewBaseAdapter(list, this, typeMH);
        //return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getMakeups();
    }

    @Override
    public void setRefeshingToSwipe(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
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
    public void showLinearLayoutError() {
        llErrors.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLinearLayoutError() {
        llErrors.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void offline() {
        tvErrorTitle.setText(getString(R.string.offline));
        tvErrorDescription.setText(R.string.no_internet_connection_description);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void emptyList() {
        tvErrorTitle.setText(R.string.no_items);
        tvErrorDescription.setText(R.string.no_items_description);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void error(String error) {
        tvErrorTitle.setText(R.string.ups);
        tvErrorDescription.setText(error);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void setItems(ArrayList<Makeup> data, String typeMH) {
        adapter = getAdapter(data, typeMH);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public String getTypeMH() {
        return this.typeMH;
    }

    @Override
    public void setTypeMH(String typeMH) {
        this.typeMH = typeMH;
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
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
