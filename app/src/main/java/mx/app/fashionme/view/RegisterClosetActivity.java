package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.RegisterClosetInteractor;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.presenter.RegisterClosetPresenter;
import mx.app.fashionme.presenter.interfaces.IRegisterClosetPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IRegisterClosetView;

public class RegisterClosetActivity extends AppCompatActivity implements IRegisterClosetView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.register_clothe_image)
    ImageView registerClotheImage;
    @BindView(R.id.register_clothe_ti_et)
    TextInputEditText registerClotheTiEt;
    @BindView(R.id.register_clothe_tv_dress_code_selected_value)
    TextView registerClotheTvDressCodeSelectedValue;
    @BindView(R.id.register_clothe_tv_climate_selected_value)
    TextView registerClotheTvClimateSelectedValue;
    @BindView(R.id.register_clothe_tv_subcategory_selected_value)
    TextView registerClotheTvSubcategorySelectedValue;
    @BindView(R.id.register_clothe_tv_color_selected_value)
    TextView registerClotheTvColorSelectedValue;
    @BindView(R.id.root_layout_register)
    CoordinatorLayout rootLayoutRegister;

    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    private static final int DRESS_CODE = 100;
    private static final int CLIMATE = 101;
    private static final int SUBCATEGORY = 102;
    private static final int COLOR = 103;
    public static final int GALLERY = 104;
    public static final int PHOTO = 105;

    public static final String EXTRA_DATA_KEY = "extra-data";
    public static final String EXTRA_LIST = "extra-list";

    private ArrayList<DataRegisterClotheViewModel> dataDressCode = new ArrayList<>();
    private ArrayList<DataRegisterClotheViewModel> dataClimate = new ArrayList<>();
    private ArrayList<DataRegisterClotheViewModel> dataSubcategory = new ArrayList<>();
    private ArrayList<DataRegisterClotheViewModel> dataColor = new ArrayList<>();

    private IRegisterClosetPresenter presenter;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_closet);
        ButterKnife.bind(this);

        presenter = new RegisterClosetPresenter(new RegisterClosetInteractor(), getApplicationContext(), this);

        setupToolbar();
        updateColors();

        builder = new MaterialDialog.Builder(this)
                .title(R.string.sending)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false);

        dialog = builder.build();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mContinue:
                presenter.sendData(
                        registerClotheTiEt.getText().toString().trim(),
                        currentPhotoPath,
                        dataSubcategory,
                        dataColor,
                        dataDressCode,
                        dataClimate
                );
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case DRESS_CODE:
                    if (dataDressCode.size() == 1) {
                        registerClotheTvDressCodeSelectedValue.setText(dataDressCode.get(0).getName());
                    } else if (dataDressCode.size() > 0) {
                        registerClotheTvDressCodeSelectedValue.setText(String.format("%d %s", dataDressCode.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvDressCodeSelectedValue.setText("");
                    }
                    break;
                case CLIMATE:
                    if (dataClimate.size() == 1) {
                        registerClotheTvClimateSelectedValue.setText(dataClimate.get(0).getName());
                    } else if (dataClimate.size() > 0) {
                        registerClotheTvClimateSelectedValue.setText(String.format("%d %s", dataClimate.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvClimateSelectedValue.setText("");
                    }
                    break;
                case SUBCATEGORY:
                    if (dataSubcategory.size() == 1) {
                        registerClotheTvSubcategorySelectedValue.setText(dataSubcategory.get(0).getName());
                    } else if (dataSubcategory.size() > 0) {
                        registerClotheTvSubcategorySelectedValue.setText(String.format("%d %s", dataSubcategory.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvSubcategorySelectedValue.setText("");
                    }
                    break;
                case COLOR:
                    if (dataColor.size() == 1) {
                        registerClotheTvColorSelectedValue.setText(dataColor.get(0).getName());
                    } else if (dataColor.size() > 0) {
                        registerClotheTvColorSelectedValue.setText(String.format("%d %s", dataColor.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvColorSelectedValue.setText("");
                    }
                    break;
                case GALLERY:
                    setCurrentPhotoPath(null);
                    Picasso.get().load(R.drawable.ic_photo_camera).into(registerClotheImage);
                    break;
                case PHOTO:
                    setCurrentPhotoPath(null);
                    Picasso.get().load(R.drawable.ic_photo_camera).into(registerClotheImage);
                    break;

            }

        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DRESS_CODE:

                    if (data != null) {
                        dataDressCode.clear();
                        dataDressCode.addAll(data.getParcelableArrayListExtra("RESULTADO"));
                    }

                    if (dataDressCode.size() == 1) {
                        registerClotheTvDressCodeSelectedValue.setText(dataDressCode.get(0).getName());
                    } else if (dataDressCode.size() > 0) {
                        registerClotheTvDressCodeSelectedValue.setText(String.format("%d %s", dataDressCode.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvDressCodeSelectedValue.setText("");
                    }

                    break;
                case CLIMATE:

                    if (data != null) {
                        dataClimate.clear();
                        dataClimate.addAll(data.getParcelableArrayListExtra("RESULTADO"));
                    }

                    if (dataClimate.size() == 1) {
                        registerClotheTvClimateSelectedValue.setText(dataClimate.get(0).getName());
                    } else if (dataClimate.size() > 0) {
                        registerClotheTvClimateSelectedValue.setText(String.format("%d %s", dataClimate.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvClimateSelectedValue.setText("");
                    }

                    break;
                case SUBCATEGORY:

                    if (data != null) {
                        dataSubcategory.clear();
                        dataSubcategory.addAll(data.getParcelableArrayListExtra("RESULTADO"));
                    }

                    if (dataSubcategory.size() == 1) {
                        registerClotheTvSubcategorySelectedValue.setText(dataSubcategory.get(0).getName());
                    } else if (dataSubcategory.size() > 0) {
                        registerClotheTvSubcategorySelectedValue.setText(String.format("%d %s", dataSubcategory.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvSubcategorySelectedValue.setText("");
                    }

                    break;
                case COLOR:

                    if (data != null) {
                        dataColor.clear();
                        dataColor.addAll(data.getParcelableArrayListExtra("RESULTADO"));
                    }

                    if (dataColor.size() == 1) {
                        registerClotheTvColorSelectedValue.setText(dataColor.get(0).getName());
                    } else if (dataColor.size() > 0) {
                        registerClotheTvColorSelectedValue.setText(String.format("%d %s", dataColor.size(), getString(R.string.register_closet_tv_value_name)));
                    } else {
                        registerClotheTvColorSelectedValue.setText("");
                    }

                    break;

                case GALLERY:
                    presenter.setThumbnailFromGallery(data.getData());
                    break;

                case PHOTO:
                    if (data != null) {
                        Uri uri = Uri.fromFile(new File(data.getStringExtra("image")));
                        presenter.setThumbnail(uri);
                    } else {
                        setCurrentPhotoPath(null);
                        Picasso.get().load(R.drawable.ic_photo_camera).into(registerClotheImage);
                    }
            }
        }
    }

    @OnClick({
            R.id.register_clothe_button_dress_code,
            R.id.register_clothe_button_climate,
            R.id.register_clothe_button_subcategories,
            R.id.register_clothe_button_color,
            R.id.register_clothe_image})
    public void onViewClicked(View view) {
        Intent i = new Intent(this, DataRegisterClotheActivity.class);

        switch (view.getId()) {
            case R.id.register_clothe_button_dress_code:
                i.putExtra(EXTRA_DATA_KEY, "dress-code");
                i.putParcelableArrayListExtra(EXTRA_LIST, dataDressCode);
                startActivityForResult(i, DRESS_CODE);
                break;
            case R.id.register_clothe_button_climate:
                i.putExtra(EXTRA_DATA_KEY, "climate");
                i.putParcelableArrayListExtra(EXTRA_LIST, dataClimate);
                startActivityForResult(i, CLIMATE);
                break;
            case R.id.register_clothe_button_subcategories:
                i.putExtra(EXTRA_DATA_KEY, "subcategories");
                i.putParcelableArrayListExtra(EXTRA_LIST, dataSubcategory);
                startActivityForResult(i, SUBCATEGORY);
                break;
            case R.id.register_clothe_button_color:
                i.putExtra(EXTRA_DATA_KEY, "colors");
                i.putParcelableArrayListExtra(EXTRA_LIST, dataColor);
                startActivityForResult(i, COLOR);
                break;
            case R.id.register_clothe_image:
                presenter.openBottomDialog();
                break;
        }
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

    @Override
    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(rootLayoutRegister, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setImageThumbnail(Uri data) {
        registerClotheImage.setImageURI(data);
    }

    @Override
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    @Override
    public void uploadProgress(boolean show) {
        if (dialog != null){
            if (show) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void showErrorName(String error) {
        registerClotheTiEt.setError(error);
    }

    @Override
    public void goToCloset() {
        startActivity(new Intent(RegisterClosetActivity.this, MyClosetActivity.class));
        finish();
    }
}
