package mx.app.fashionme.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IMakeLookInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.IMakeLookPresenter;
import mx.app.fashionme.sticker.StickerImageView;
import mx.app.fashionme.sticker.StickerTextInputDialog;
import mx.app.fashionme.sticker.StickerTextView;
import mx.app.fashionme.sticker.StickerView;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IMakeLookView;

/**
 * Created by heriberto on 11/04/18.
 */

public class MakeLookPresenter implements IMakeLookPresenter, IMakeLookInteractor.OnGetSuggestionsFinishedListener, IMakeLookInteractor.OnMakeStickerFinishedListener, IMakeLookInteractor.OnSavedFileListener, IMakeLookInteractor.OnSavedDateFinishedListener {

    private static final String TAG = MakeLookPresenter.class.getSimpleName();

    private IMakeLookView view;
    private IMakeLookInteractor interactor;
    private Context context;

    public MakeLookPresenter(IMakeLookView view, IMakeLookInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getSuggestionsByIdClothe(int clotheId) {
        interactor.getDataSuggestions(context, clotheId,this);
    }

    @Override
    public void getSuggestionsGeneral() {
        interactor.getDataSuggestions(context, this);
    }

    @Override
    public void setStickerSuggestion(String urlImage) {
        interactor.createSticker(urlImage, context, view, this);
    }

    @Override
    public void setStickerText(String text, StickerTextInputDialog stickerTextInputDialog) {
        Log.d(TAG, "To add sticker with text");
        interactor.createStickerText(text, context, stickerTextInputDialog,view, this);
    }

    @Override
    public void updateSuggestions(ArrayList<Clothe> currentClothes, String filter) {
        interactor.filterSuggestions(currentClothes, filter, context, new IMakeLookInteractor.OnFilterFinishedListener() {
            @Override
            public void onFilter(ArrayList<Clothe> filteredClothes) {
                if (view != null) {
                    view.updateAdapterSuggestions(filteredClothes);
                }
            }
        });
    }

    @Override
    public void saveLook(View view, ArrayList<StickerView> stickerViews) {
        interactor.saveLook(view, context, stickerViews, false,this);
    }

    @Override
    public void addToCalendar(View view, ArrayList<StickerView> stickerViews) {
        interactor.saveLook(view, context, stickerViews, true,this);
    }

    @Override
    public void saveDateLook(Look look, String date) {
        interactor.saveDateToLook(context, look, date, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Crear look/Android");
        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onSuccess(ArrayList<Clothe> clothes) {
        if (view != null) {
            if (clothes.size() == 0) {
                view.showError("No se encontraron sugerencias");
                return;
            }

            view.generateGridLayout(Utils.COLUMNS_MAKE_LOOK);
            view.initializeAdapterSuggestions(view.createAdapterSuggestClothe(clothes));

            view.setCurrentSuggestions(clothes);

            for (Clothe clothe : clothes) {
                view.setListSubcategories(clothe.getSubcategories().getData());
            }
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }

    @Override
    public void onSuccessMake(StickerImageView stickerImageView) {
        if (view != null) {
            view.hideSheetBottom();
            view.addImageToBoard(stickerImageView);
        }
    }

    @Override
    public void onErrorMake(String error) {
        if (view != null){
            view.showError(error);
        }
    }

    @Override
    public void onSuccessMake(StickerTextView stickerTextView) {
        if (view != null) {
            view.addStickerTextToBoard(stickerTextView);
        }
    }

    @Override
    public void onSuccessSave(String msj, boolean showCalendar, Look look) {
        if (view != null){
            if (showCalendar) {
                view.showCalendar(look);
                return;
            }
            view.showSnackbar(true, msj);
            view.addShareMenu(look.getUri());
        }
    }

    @Override
    public void onErrorSave(String err) {
        if (view != null){
            view.showSnackbar(false, err);
        }
    }

    @Override
    public void onSuccessSavedDate(String msj) {
        if (view != null) {
            view.showSnackbar(true, msj);
        }

    }

    @Override
    public void onErrorSavedDate(String error, Look look) {
        if (view != null) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            view.showCalendar(look);
        }
    }
}
