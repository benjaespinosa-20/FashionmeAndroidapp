package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IRegisterClosetInteractor;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.presenter.interfaces.IRegisterClosetPresenter;
import mx.app.fashionme.view.CameraActivity;
import mx.app.fashionme.view.RegisterClosetActivity;
import mx.app.fashionme.view.interfaces.IRegisterClosetView;

public class RegisterClosetPresenter implements IRegisterClosetPresenter, IRegisterClosetInteractor.RegisterClosetListener {

    /*
    UI Dialog
     */
    @BindView(R.id.btnGallery)
    ImageView btnGallery;
    @BindView(R.id.tvGallery)
    TextView tvGallery;
    @BindView(R.id.btnCamera)
    ImageView btnCamera;
    @BindView(R.id.tvCamera)
    TextView tvCamera;
    @BindView(R.id.btnDelete)
    ImageView btnDelete;
    @BindView(R.id.tvDelete)
    TextView tvDelete;
    @BindView(R.id.dialog_button_text_title)
    TextView dialogButtonTextTitle;

    private BottomSheetDialog bottomSheetDialog;

    private Context context;
    private Activity activity;
    private IRegisterClosetView view;
    private IRegisterClosetInteractor interactor;

    public RegisterClosetPresenter(IRegisterClosetInteractor interactor, Context context, Activity activity) {
        this.interactor = interactor;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void setView(IRegisterClosetView view) {
        this.view = view;
    }

    @Override
    public void openBottomDialog() {
        bottomSheetDialog = new BottomSheetDialog(activity);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);

        ButterKnife.bind(this, dialogView);

        dialogButtonTextTitle.setText(R.string.option_select);
        btnDelete.setVisibility(View.INVISIBLE);
        tvDelete.setVisibility(View.INVISIBLE);

        bottomSheetDialog.setContentView(dialogView);

        bottomSheetDialog.show();
    }

    @Override
    public void setThumbnailFromGallery(Uri data) {
        interactor.setThumbnailFromGallery(activity, data, this);
    }

    @Override
    public void setThumbnail(Uri uri) {
        interactor.setThumbnail(context, uri, this);
    }

    @Override
    public void sendData(String name, String photo,
                         ArrayList<DataRegisterClotheViewModel> subcategories, ArrayList<DataRegisterClotheViewModel> colors,
                         ArrayList<DataRegisterClotheViewModel> dressCodes, ArrayList<DataRegisterClotheViewModel> climates) {
        if (view != null) {
            view.uploadProgress(true);
        }
        interactor.register(
                context,
                name, photo,
                subcategories, colors, dressCodes,
                climates ,this);
    }

    @OnClick({R.id.btnGallery, R.id.btnCamera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGallery:

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                activity.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_photo)), RegisterClosetActivity.GALLERY);
                break;
            case R.id.btnCamera:
                Intent intentCamera = new Intent(activity, CameraActivity.class);
                intentCamera.putExtra("filter_type", "other");
                activity.startActivityForResult(intentCamera, RegisterClosetActivity.PHOTO);
                break;
        }

        if (bottomSheetDialog != null)
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.uploadProgress(false);
            view.showError(error);
        }
    }

    @Override
    public void onGetThumbnail(Uri data) {
        if (view != null) {
            view.setImageThumbnail(data);
            if (view.getCurrentPhotoPath() == null) {
                view.setCurrentPhotoPath(interactor.getRealPathFromURI(context, data));
            }
        }
    }

    @Override
    public void onErrorName(String error) {
        if (view != null) {
            view.uploadProgress(false);
            view.showErrorName(error);
        }
    }

    @Override
    public void onSuccessUpload() {
        if (view != null) {
            view.uploadProgress(false);
            view.goToCloset();
        }
    }
}
