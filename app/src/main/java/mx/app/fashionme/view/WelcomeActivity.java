package mx.app.fashionme.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.WelcomeInteractor;
import mx.app.fashionme.presenter.WelcomePresenter;
import mx.app.fashionme.presenter.interfaces.IWelcomePresenter;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.view.interfaces.IWelcomeView;

public class WelcomeActivity extends AppCompatActivity implements IWelcomeView {

    private Toolbar toolbar;
    private Button btnWelcomeContinue;

    private IWelcomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        toolbar             = findViewById(R.id.toolbarWelcome);
        btnWelcomeContinue  = findViewById(R.id.btnWelcomeContinue);

        setUpPresenter();
        setUpToolbar();

        btnWelcomeContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           presenter.nextPage();
            }
        });

    }

    @Override
    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setUpPresenter() {
        presenter = new WelcomePresenter(this, new WelcomeInteractor(), getApplicationContext());
    }

    @Override
    public void goToNextPage() {
        startActivity(new Intent(this, HomeController.class));
        this.finish();
    }
}
