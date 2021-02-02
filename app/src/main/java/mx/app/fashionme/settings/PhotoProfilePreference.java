package mx.app.fashionme.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.Preference;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.SettingsInteractor;
import mx.app.fashionme.presenter.SettingsPresenter;
import mx.app.fashionme.presenter.interfaces.ISettingsPresenter;
import mx.app.fashionme.view.interfaces.ISettingsView;

public class PhotoProfilePreference extends Preference implements View.OnClickListener,
        ISettingsView {

    private static final String TAG = PhotoProfilePreference.class.getSimpleName();
    public static final int REQUEST_PERMISSION_CODE = 101;
    public static final int REQUEST_GALLERY_CODE = 110;

    /*
    Imagen por defecto
     */
    private static final String DEFAULT_IMAGE = null;
    private static ISettingsPresenter presenterStatic;

    /*
    Imagen actual
     */
    private String currentUriPhotoProfile;

    /*
    UI Dialog
     */
    private ImageView btnGallery;
    private ImageView btnCamera;
    private ImageView btnDelete;
    private TextView tvGallery;
    private TextView tvCamera;
    private TextView tvDelete;

    /*
    Variable de contexto
     */
    private Context context;
    private Activity activity;
    private ISettingsPresenter presenter;

    private BottomSheetDialog bottomSheetDialog;
    private CircleImageView imageView;

    public PhotoProfilePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context            = getContext();
        this.activity           = (Activity) getContext();
        currentUriPhotoProfile  = DEFAULT_IMAGE;
        setUpPresenterSettings();
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.preference_photo, parent, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        imageView = view.findViewById(R.id.imgCircleProfile);
        FloatingActionButton fabPhoto = view.findViewById(R.id.fab_photo);

        if (currentUriPhotoProfile != null) {
            imageView.setImageURI(Uri.parse(currentUriPhotoProfile));
        }

        fabPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomDialog();
            }
        });
    }

    private void openBottomDialog() {

        bottomSheetDialog = new BottomSheetDialog(getContext());
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);

        initUIDialog(dialogView);
        bottomSheetDialog.setContentView(dialogView);

        btnGallery.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        if (currentUriPhotoProfile == null) {
            btnDelete.setVisibility(View.INVISIBLE);
            tvDelete.setVisibility(View.INVISIBLE);
        }

        // TODO Implementar funcionalidad para elegir foto mediante la camara
        btnCamera.setVisibility(View.INVISIBLE);
        tvCamera.setVisibility(View.INVISIBLE);

        bottomSheetDialog.show();
    }

    private void initUIDialog(View dialogView) {

        btnGallery  = dialogView.findViewById(R.id.btnGallery);
        btnCamera   = dialogView.findViewById(R.id.btnCamera);
        btnDelete   = dialogView.findViewById(R.id.btnDelete);

        tvGallery   = dialogView.findViewById(R.id.tvGallery);
        tvCamera    = dialogView.findViewById(R.id.tvCamera);
        tvDelete    = dialogView.findViewById(R.id.tvDelete);

    }


    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Obtener la imagen actual
            currentUriPhotoProfile = this.getPersistedString(DEFAULT_IMAGE);
        } else {
            // Reiniciar el valor por defecto
            currentUriPhotoProfile = (String) defaultValue;
            persistString(currentUriPhotoProfile);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGallery:
                presenter.checkPermissionConditions();
                break;
            case R.id.btnCamera:
                Toast.makeText(context, "Open camera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnDelete:
                Toast.makeText(context, "Delete image", Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }

    }

    @Override
    public void setUpPresenterSettings() {
        presenter = new SettingsPresenter( this, new SettingsInteractor(), context, activity);
        presenterStatic = presenter;
    }

    @Override
    public void showExplication() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String title = activity.getString(R.string.allow_read_storage);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00BCD4"));
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        builder.setTitle(ssBuilder)
                .setMessage(R.string.message_allow_read_storage)
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
                        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void showNeverAskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String titleText = "¿Permitir que Fashion Me use el almacenamiento del teléfono?";
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
                        "\nPara permitir el acceso, haz clic en \"Configuración de aplicaciones\" " +
                        "a continuación y activa \"Almacenamiento\" en el menú \"Permisos\".")
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
    public void openGallery() {
        bottomSheetDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_GALLERY_CODE);
    }

    @Override
    public void setPreferenceImageProfile(String filepath) {
        //Log.i(TAG, "FILEPATH=" + filepath);
        currentUriPhotoProfile = filepath;
        persistString(filepath);
        imageView.setImageURI(Uri.parse(filepath));
    }

    public static ISettingsPresenter getPresenter() {
        return presenterStatic;
    }
}
