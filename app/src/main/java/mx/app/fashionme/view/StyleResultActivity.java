package mx.app.fashionme.view;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.interactor.StyleResultInteractor;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.presenter.StyleResultPresenter;
import mx.app.fashionme.presenter.interfaces.IStyleResultPresenter;
import mx.app.fashionme.utils.PicassoImageLoadingService;
import mx.app.fashionme.view.interfaces.IStyleResultView;
import ss.com.bannerslider.Slider;

public class StyleResultActivity extends AppCompatActivity implements IStyleResultView {

    private Slider sliderStyleResult;
    private TextView tvStyleResultName;

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private CoordinatorLayout rootView;

    private IStyleResultPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_result);

        sliderStyleResult   = findViewById(R.id.sliderStyleResult);
        tvStyleResultName   = findViewById(R.id.tvStyleResultName);

        toolbar             = findViewById(R.id.action_bar_style_result);
        progressBar         = findViewById(R.id.progressBarStyleResult);
        rootView            = findViewById(R.id.rootViewStyleResult);

        setUpToolbarStyleResult();
        setUpPresenterStyleResult();
        getResult();
        presenter.setAnalytics();

    }

    @Override
    public void setUpToolbarStyleResult() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setUpPresenterStyleResult() {
        presenter = new StyleResultPresenter(this, new StyleResultInteractor(), getApplicationContext());
    }

    @Override
    public void getResult() {
        presenter.getResult();
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showError(String err) {
        Snackbar.make(rootView, err, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getResult();
                    }
                })
                .show();
    }

    @Override
    public void setTvResult(String styleResult) {
        tvStyleResultName.setText(styleResult);
    }

    @Override
    public void setStyleImagesSlider(ArrayList<ImageTrend> images) {
        Slider.init(new PicassoImageLoadingService(getApplicationContext()));
        sliderStyleResult.setAdapter(new DressCodeSliderAdapter(images));
    }
}
