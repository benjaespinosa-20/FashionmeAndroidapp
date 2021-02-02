package mx.app.fashionme.presenter.interfaces;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Journey;

public interface IShowJourneyPresenter {
    void getJourney(int idJourney);
    void checkFav(int idJourney);
    void removeFav(int idJourney);
    void addFav(int idJourney);
    void openDialogChecklist(Journey journey, MaterialDialog.Builder dialog);
    void checkListSelected(ArrayList<String> checklistSelected);

    void openActivityChecklist(Journey journey);

    void setAnalytics();
}
