package mx.app.fashionme.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.ColorQuestionAdapter;
import mx.app.fashionme.interactor.ColorTestInteractor;
import mx.app.fashionme.pojo.ColorQuestionsViewModel;
import mx.app.fashionme.presenter.ColorTestPresenter;
import mx.app.fashionme.presenter.interfaces.IColorTestPresenter;
import mx.app.fashionme.view.interfaces.IColorTestView;

public class ColorTestActivity extends AppCompatActivity implements IColorTestView {

    public static final String TAG = ColorTestActivity.class.getSimpleName();

    @BindView(R.id.action_bar_color_test)
    Toolbar toolbar;
    @BindView(R.id.rv_color_questions)
    RecyclerView rvColorQuestions;
    @BindView(R.id.root)
    RelativeLayout root;

    private ColorQuestionAdapter listAdapter;
    private List<ColorQuestionsViewModel> resultList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private IColorTestPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_test);

        ButterKnife.bind(this);

        presenter = new ColorTestPresenter(new ColorTestInteractor(), getApplicationContext(), this);

        setupToolbar();
        setupDialog();

        listAdapter = new ColorQuestionAdapter(getApplicationContext(), resultList);
        rvColorQuestions.setAdapter(listAdapter);
        rvColorQuestions.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        rvColorQuestions.setItemAnimator(new DefaultItemAnimator());
        rvColorQuestions.setHasFixedSize(true);
        rvColorQuestions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        presenter.setView(this);
        presenter.loadData();
    }


    @Override
    public void showError(String error) {
        Snackbar.make(root, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.setMessage("Enviando...");
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void navigateToExtraTest() {
        startActivity(new Intent(this, ExtraTestActivity.class));
        this.finish();
    }

    @Override
    public void updateData(List<ColorQuestionsViewModel> data) {
        resultList.addAll(data);
        listAdapter.notifyDataSetChanged();
    }
    
    @OnClick(R.id.btnSendTest)
    public void sendAnswers() {
        presenter.sendAnswers(resultList);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
}
