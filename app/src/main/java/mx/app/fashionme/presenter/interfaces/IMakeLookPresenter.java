package mx.app.fashionme.presenter.interfaces;

import android.view.View;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.sticker.StickerTextInputDialog;
import mx.app.fashionme.sticker.StickerView;

/**
 * Created by heriberto on 11/04/18.
 */

public interface IMakeLookPresenter {

    void getSuggestionsByIdClothe(int clotheId);
    void getSuggestionsGeneral();
    void setStickerSuggestion(String urlImage);
    void setStickerText(String text, StickerTextInputDialog stickerTextInputDialog);

    void updateSuggestions(ArrayList<Clothe> currentClothes, String filter);

    void saveLook(View view, ArrayList<StickerView> stickerViews);
    void addToCalendar(View view, ArrayList<StickerView> stickerViews);

    void saveDateLook(Look look, String date);

    void setAnalytics();
}
