package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.presenter.MakeLookPresenter;
import mx.app.fashionme.sticker.StickerImageView;
import mx.app.fashionme.sticker.StickerTextInputDialog;
import mx.app.fashionme.sticker.StickerTextView;
import mx.app.fashionme.sticker.StickerView;
import mx.app.fashionme.view.interfaces.IMakeLookView;

/**
 * Created by heriberto on 11/04/18.
 */

public interface IMakeLookInteractor {

    void getDataSuggestions(Context context, int clotheId, OnGetSuggestionsFinishedListener callback);
    void getDataSuggestions(Context context, OnGetSuggestionsFinishedListener callback);
    void createSticker(String urlImage, Context context, IMakeLookView view, OnMakeStickerFinishedListener callback);
    void createStickerText(String text, Context context, StickerTextInputDialog stickerTextInputDialog, IMakeLookView view, OnMakeStickerFinishedListener callback);

    void saveLook(View view, Context context, ArrayList<StickerView> stickerViews, boolean showCalendar, OnSavedFileListener callback);
    void save(File file, View view, Context context);
    void saveToDB(Context context, Look look);
    Bitmap createBitmap(View view);

    void filterSuggestions(ArrayList<Clothe> currentSuggestions, String filter, Context context, OnFilterFinishedListener listener);

    void saveDateToLook(Context context, Look look, String date, OnSavedDateFinishedListener listener);

    interface OnGetSuggestionsFinishedListener {
        void onSuccess(ArrayList<Clothe> clothes);
        void onError(String err);
    }

    interface OnMakeStickerFinishedListener {
        void onSuccessMake(StickerImageView stickerImageView);
        void onSuccessMake(StickerTextView stickerTextView);
        void onErrorMake(String error);
    }

    interface OnSavedFileListener {
        void onSuccessSave(String msj, boolean showCalendar, Look look);
        void onErrorSave(String err);
    }

    interface OnFilterFinishedListener {
        void onFilter(ArrayList<Clothe> filteredClothes);
    }

    interface OnSavedDateFinishedListener {
        void onSuccessSavedDate(String msj);
        void onErrorSavedDate(String error, Look look);
    }

}
