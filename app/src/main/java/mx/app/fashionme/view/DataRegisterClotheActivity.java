package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.DataRegisterClotheAdapter;
import mx.app.fashionme.interactor.DataRegisterClotheInteractor;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.presenter.DataRegisterClothePresenter;
import mx.app.fashionme.presenter.interfaces.IDataRegisterClothePresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IDataRegisterClotheView;

public class DataRegisterClotheActivity extends AppCompatActivity implements IDataRegisterClotheView {

    @BindView(R.id.action_bar_data_register_clothe)
    Toolbar toolbar;
    @BindView(R.id.rv_data_options)
    RecyclerView rvDataOptions;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private DataRegisterClotheAdapter listAdapter;
    private List<DataRegisterClotheViewModel> resultList = new ArrayList<>();
    private IDataRegisterClothePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_register_clothe);

        ButterKnife.bind(this);

        presenter = new DataRegisterClothePresenter(new DataRegisterClotheInteractor(), getApplicationContext(), this);

        setupToolbar();
        updateColors();

        listAdapter = new DataRegisterClotheAdapter(resultList);
        rvDataOptions.setAdapter(listAdapter);
        rvDataOptions.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        rvDataOptions.setItemAnimator(new DefaultItemAnimator());
        rvDataOptions.setHasFixedSize(true);
        rvDataOptions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        presenter.setView(this);
        presenter.loadDataToRegister();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick({R.id.button_accept, R.id.button_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_accept:
                presenter.selectData(resultList);
                break;
            case R.id.button_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
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
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateData(List<DataRegisterClotheViewModel> data) {
        resultList.addAll(data);
        Intent i = getIntent();
        List<DataRegisterClotheViewModel> lastData = i.getParcelableArrayListExtra(RegisterClosetActivity.EXTRA_LIST);
        listAdapter.previousData(lastData);
        listAdapter.notifyDataSetChanged();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
        }
    }

    private void updateColors() {
        List<String> colorsPrimary = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        List<String> colorsPrimaryDark = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(this));
            getWindow().setStatusBarColor((Color.parseColor(colorsPrimaryDark.get(colorsPrimary.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorsPrimaryDark.get(colorsPrimary.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }
}
