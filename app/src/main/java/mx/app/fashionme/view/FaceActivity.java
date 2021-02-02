package mx.app.fashionme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.contract.FaceContract;
import mx.app.fashionme.interactor.FaceInteractor;
import mx.app.fashionme.presenter.FacePresenter;
import mx.app.fashionme.utils.Utils;

public class FaceActivity extends AppCompatActivity implements FaceContract.FaceView, View.OnClickListener {

    private static final String TAG = FaceActivity.class.getSimpleName();

    public static final int REQUEST_PERMISSION_CODE = 100;
    public static final int CAMERA_REQUEST_CODE     = 200;
    public static final int GALLERY_REQUEST_CODE    = 300;

    @BindView(R.id.btnCamera)
    ImageView btnCamera;

    @BindView(R.id.btnGallery)
    ImageView btnGallery;

    @BindView(R.id.imgRostro)
    ImageView imgRostro;

    @BindView(R.id.btnSendPhoto)
    Button btnSend;

    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

    @BindView(R.id.miActionBar)
    Toolbar toolbar;

    private Context context;
    private Activity activity;

    private String currentPhotoPath;

    private boolean isCameraPetition;
    private boolean isGalleryPetition;

    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    private FaceContract.FacePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        ButterKnife.bind(this);

        context     = getApplicationContext();
        activity    = FaceActivity.this;

        presenter = new FacePresenter(this, new FaceInteractor(), context, activity);

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        buildDialog();
        presenter.setAnalytics();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        updateColors();
    }

    private void buildDialog() {
        builder = new MaterialDialog.Builder(this)
                .title(R.string.sending)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false);

        dialog = builder.build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                isCameraPetition = true;
                isGalleryPetition = false;
                presenter.checkPermissionStatus(true, false);
                break;
            case R.id.btnGallery:
                isCameraPetition = false;
                isGalleryPetition = true;
                presenter.checkPermissionStatus(false, true);
                break;
            case R.id.btnSendPhoto:
                presenter.sendPhoto(currentPhotoPath, rootView);
                break;
        }
    }

    @Override
    public void showExplicationPermissions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getString(R.string.allow_camera_storage);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setTitle(ssBuilder)
                .setMessage(R.string.message_allow_camera_storage)
                .setNegativeButton(R.string.button_deny_access, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.button_allow_access, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeBUtton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeBUtton.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void showNeverAskDialogPermissions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String titleText = "¿Permitir a Fashion Me?";
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage("Fashion Me podrá almacenar y acceder a cierta información, " +
                        "como las fotos del teléfono y la tarjeta SD." + "\n" +
                        "Asi como acceder a la camara del teléfono."+
                        "\nPara permitir el acceso, haz clic en \"Configuración de aplicaciones\" " +
                        "a continuación y activa \"Almacenamiento\" y \"Camara\"en el menú \"Permisos\".")
                .setNegativeButton("AHORA NO", null)
                .setPositiveButton("CONFIGURACIÓN DE APLICACIONES", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#000000"));

    }

    @Override
    public void setImageFaceThumbnail(Bitmap thumbnail) {
        imgRostro.setImageBitmap(thumbnail);
    }

    @Override
    public void setImageFaceThumbnail(Uri uri) {
        imgRostro.setImageURI(uri);
    }

    @Override
    public void showProgress(boolean show) {
        if (dialog != null) {
            if (show) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(FaceActivity.this, CameraActivity.class);
        takePictureIntent.putExtra("filter_type", "face");
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"), GALLERY_REQUEST_CODE);
    }

    @Override
    public String getCurrentPhotoFacePath() {
        return currentPhotoPath;
    }

    @Override
    public void setCurrentPhotoFacePath(String currentPhotoFacePath) {
        this.currentPhotoPath = currentPhotoFacePath;
    }

    @Override
    public void showError(View view, String error) {
        Snackbar.make(view,
                error,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        presenter.checkPermissionStatus(isCameraPetition, isGalleryPetition);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        presenter.showExplanationPermissions();
                    } else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        presenter.showExplanationPermissions();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null) {
                    Log.e("CameraPre", "data: " + data.getStringExtra("image"));
                    Uri uri = Uri.fromFile(new File(data.getStringExtra("image")));
                    presenter.setThumbnail(uri);
                }
                else {
                    setCurrentPhotoFacePath(null);
                    Picasso.get().load(R.drawable.imgrostro).into(imgRostro);
                }
            }

            if (requestCode == GALLERY_REQUEST_CODE) {
                presenter.setThumbnailFromGallery(data.getData(), rootView);
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            setCurrentPhotoFacePath(null);
            Picasso.get().load(R.drawable.imgrostro).into(imgRostro);
        } else if (resultCode == RESULT_CANCELED) {
            setCurrentPhotoFacePath(null);
            Picasso.get().load(R.drawable.imgrostro).into(imgRostro);
        } else {
            setCurrentPhotoFacePath(null);
            Picasso.get().load(R.drawable.imgrostro).into(imgRostro);
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
