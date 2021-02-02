package mx.app.fashionme.view.interfaces;

import android.app.Dialog;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import mx.app.fashionme.adapter.JourneyAdapter;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.pojo.Subcategory;

public interface IShowJourneyView {

    void setUpPresenter();

    void getDataJourney();

    void setUpToolbar();

    void showLoading(boolean show);

    void generateLinearLayout();

    JourneyAdapter createAdapter(Journey journey);

    void initializeAdapter(JourneyAdapter adapter);

    void showError(String error);

    void setButtonFav(boolean isFav);

    void favAdded();

    void favRemoved();

    void showDialogChecklist(MaterialDialog.Builder dialog, ArrayList<String> names);
}
