package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.SuggestionAdapter;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.sticker.StickerImageView;
import mx.app.fashionme.sticker.StickerTextView;
import mx.app.fashionme.sticker.StickerView;

/**
 * Created by heriberto on 11/04/18.
 */

public interface IMakeLookView {

    void addImageSelected(final String urlImg);
    void generateGridLayout(int columns);
    SuggestionAdapter createAdapterSuggestClothe(ArrayList<Clothe> clothes);
    void initializeAdapterSuggestions(SuggestionAdapter adapter);
    void setListSubcategories(ArrayList<Subcategory> subcategories);
    void setUpPresenterMakeLook();
    void getSuggestionsByClotheId(int id);
    void getSuggestionsGeneral();
    void showError(String err);
    void setStickerImage(String urlImage);
    void addImageToBoard(StickerImageView imageView);

    void setCurrentSuggestions(ArrayList<Clothe> clothes);

    void setStickerText(String text);
    void addStickerTextToBoard(StickerTextView stickerTextView);

    void stickerInEdit(int id);

    void showFilterDialog();
    void updateSuggestions(String select);
    void updateAdapterSuggestions(ArrayList<Clothe> clothesSort);

    void showSnackbar(boolean button, String text);

    void checkPermissions();

    void hideSheetBottom();

    void deletedSticker(StickerView stickerView);

    void showCalendar(Look look);

    void addShareMenu(String urlPathLook);
}
