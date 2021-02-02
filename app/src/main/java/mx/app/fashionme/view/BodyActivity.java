package mx.app.fashionme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.BodyInteractor;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.BodyPresenter;
import mx.app.fashionme.presenter.interfaces.IBodyPresenter;
import mx.app.fashionme.view.interfaces.IBodyView;


public class BodyActivity extends AppCompatActivity implements IBodyView, View.OnClickListener {

    private static final String TAG = BodyActivity.class.getSimpleName();

    public static final int REQUEST_PERMISSION_CODE = 100;
    public static final int CAMERA_REQUEST_CODE     = 200;
    public static final int GALLERY_REQUEST_CODE    = 300;

    private ImageView btnCamera, btnGallery, imgPhotoBody;
    private Button btnSend;
    private CoordinatorLayout rootView;

    private Context context;
    private Activity activity;

    private String currentPhotoPath;
    private Uri photoURI;
    private File photoFile;

    private boolean cameraPetition;
    private boolean galleryPetition;

    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    private IBodyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        btnCamera       = findViewById(R.id.btnCamera);
        btnGallery      = findViewById(R.id.btnGallery);
        btnSend         = findViewById(R.id.btnSendPhoto);
        imgPhotoBody    = findViewById(R.id.imgPhotoBody);

        rootView        = findViewById(R.id.rootView);

        context         = getApplicationContext();
        activity        = BodyActivity.this;

        setUpPresenter();

        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnSend.setOnClickListener(this);

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
        String token = SessionPrefs.get(this).getTokenBodyApi();

        if (token != null) {
            startActivity(new Intent(BodyActivity.this, ResultActivity.class));
            this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                cameraPetition = true;
                galleryPetition = false;
                presenter.checkPermissionsStatus(cameraPetition, galleryPetition);
                break;
                
            case R.id.btnGallery:
                cameraPetition = false;
                galleryPetition = true;
                presenter.checkPermissionsStatus(cameraPetition, galleryPetition);
                break;

            case R.id.btnSendPhoto:
                presenter.sendPhoto(currentPhotoPath, rootView);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if ( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        presenter.checkPermissionsStatus(cameraPetition, galleryPetition);
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        presenter.showExplanation();
                    } else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                        presenter.showExplanation();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null){
                    Log.e("CameraPre", "data: " + data.getStringExtra("image"));
                    Uri uri = Uri.fromFile(new File(data.getStringExtra("image")));
                    presenter.setThumbnail(uri);
                } else{
                    setCurrentPhotoPath(null);
                    Picasso.get().load(R.drawable.body_human).into(imgPhotoBody);
                }
            }

            if (requestCode == GALLERY_REQUEST_CODE) {
                presenter.setThumbnailFromGallery(data.getData(), rootView);
            }
        } else if (resultCode == RESULT_CANCELED){
            setCurrentPhotoPath(null);
            Picasso.get().load(R.drawable.body_human).into(imgPhotoBody);
        } else {
            setCurrentPhotoPath(null);
            Picasso.get().load(R.drawable.body_human).into(imgPhotoBody);
        }
    }

    @Override
    public void showError(View view, String error) {
        Snackbar.make(view,
                error,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void navigateToTest(View view) {

        Snackbar.make(view,
                getResources().getString(R.string.mensaje_go_test),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.go), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), ColorTestActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void setUpPresenter() {
        presenter = new BodyPresenter(this, new BodyInteractor(), getApplicationContext(), activity);
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(BodyActivity.this, CameraActivity.class);
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
    public void showExplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getString(R.string.allow_camera_storage);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage(R.string.message_allow_camera_storage)
                .setNegativeButton(R.string.button_deny_access, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.button_allow_access, new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#000000"));

    }

    @Override
    public void showNeverAskDialog() {
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
    public void setImageBodyThumbnail(Bitmap thumbnail) {
        imgPhotoBody.setImageBitmap(thumbnail);
    }

    @Override
    public void setImageBodyThumbnail(Uri uri) {
        imgPhotoBody.setImageURI(uri);
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (dialog != null){
            if (show) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
    }

    @Override
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    @Override
    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
}
