package mx.app.fashionme.view;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.ResultInteractor;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.ResultPresenter;
import mx.app.fashionme.presenter.interfaces.IResultPresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.interfaces.IResultView;

public class ResultActivity extends AppCompatActivity implements IResultView {

    private TextView tvFelicidades, tvBodyValue, tvColorValue;
    private ImageView imgPalette;
    private Button btnContinue;

    private IResultPresenter presenter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvFelicidades   = findViewById(R.id.tvFelicidades);
        tvBodyValue     = findViewById(R.id.tvFelicidadesValueBodyType);
        tvColorValue    = findViewById(R.id.tvFelicidadesValueEstacion);
        imgPalette      = findViewById(R.id.imgPaletteResult);
        btnContinue     = findViewById(R.id.btnResultContinue);
        progressBar     = findViewById(R.id.progressBar);

        setUpPresenter();
        getResults();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHomePage();
            }
        });
    }

    @Override
    public void setUpPresenter() {
        presenter = new ResultPresenter(this, new ResultInteractor(), getApplicationContext());

    }

    @Override
    public void getResults() {
        presenter.getResults();
    }

    @Override
    public void setResults(String name, String body, String color, String imageUrl) {
        tvFelicidades.setText(getString(R.string.felicidades_text, name));
        tvBodyValue.setText(body);
        tvColorValue.setText(color);
        if (imageUrl != null){
            Picasso.get().load(imageUrl).into(imgPalette);
        }
    }

    @Override
    public void navigateToHomePage() {
        SessionPrefs.get(getApplicationContext()).saveResult(true);
        String activity = SessionPrefs.get(this).getActivity();
        if (activity != null && activity.equals(Constants.CATEGORY)) {
            startActivity(new Intent(this, CategoryActivity.class));
            this.finish();
        } else if (activity != null && activity.equals(Constants.RECOMMENDATIONS)){
            startActivity(new Intent(this, RecommendationActivity.class));
            this.finish();
        }
    }

    @Override
    public void goToBodyActivity() {
        startActivity(new Intent(this, BodyActivity.class));
        this.finish();
    }

    @Override
    public void goToTestColorActivity() {
        startActivity(new Intent(this, ColorTestActivity.class));
        this.finish();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
