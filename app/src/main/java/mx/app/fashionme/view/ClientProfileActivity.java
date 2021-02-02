package mx.app.fashionme.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.ClientProfileInteractor;
import mx.app.fashionme.presenter.ClientProfilePresenter;
import mx.app.fashionme.presenter.interfaces.IClientProfilePresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.interfaces.IClientProfileView;

public class ClientProfileActivity extends AppCompatActivity implements IClientProfileView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvGenreProfile)
    TextView tvGenreProfile;

    @BindView(R.id.tvColorProfile)
    TextView tvColorProfile;

    @BindView(R.id.tvBodyProfile)
    TextView tvBodyProfile;

    @BindView(R.id.tvStyleProfile)
    TextView tvStyleProfile;

    @BindView(R.id.imgPalette)
    ImageView imgPalette;

    @BindView(R.id.btnPhotoProfile)
    RelativeLayout btnPhotoProfile;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.contentProfile)
    View content;

    private IClientProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        ButterKnife.bind(this);
        btnPhotoProfile.setVisibility(View.INVISIBLE);
        setupToolbar();
        presenter = new ClientProfilePresenter(this, this, getApplicationContext(), new ClientProfileInteractor());
        presenter.getProfileUser(getIntent().getStringExtra(Constants.CLIENT_EMAIL));
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
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
    public void showLayoutProfile() {
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutProfile() {
        content.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void notEmail() {
        this.finish();
    }

    @Override
    public void setName(String name) {
        tvName.setText(name);
    }

    @Override
    public void setEmail(String email) {
        tvEmail.setText(email);
    }

    @Override
    public void setGenre(String genre) {
        tvGenreProfile.setText(genre);
    }

    @Override
    public void setColorName(String colorName) {
        tvColorProfile.setText(colorName);
    }

    @Override
    public void setImagePalette(String palette) {
        if (palette != null && !palette.equals("")){
            Picasso.get().load(palette).into(imgPalette);
        }
    }

    @Override
    public void setBodyType(String bodyType) {
        tvBodyProfile.setText(bodyType);
    }

    @Override
    public void setStyle(String style) {
        tvStyleProfile.setText(style);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
