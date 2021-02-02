package mx.app.fashionme.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;

public class CongratsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
        ButterKnife.bind(this);
        setupToolbar();
    }


    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

}
