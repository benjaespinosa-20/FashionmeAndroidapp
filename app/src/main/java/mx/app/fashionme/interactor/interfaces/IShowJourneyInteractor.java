package mx.app.fashionme.interactor.interfaces;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Journey;

public interface IShowJourneyInteractor {
    void getJourney(Context context, int idJourney, IJourneyInteractor.JourneyListener listener);

    void checkFav(Context context, int idJourney, IJourneyInteractor.JourneyListener listener);

    void removeFav(Context context, int idJourney, IJourneyInteractor.JourneyListener listener);

    void addToFav(Context context, int idJourney, IJourneyInteractor.JourneyListener listener);

    void openDialog(Context context, Journey journey, MaterialDialog.Builder dialog, IJourneyInteractor.JourneyListener listener);

    void saveChecklist(ArrayList<String> checklistSelected);

    void openActivity(Context context, Journey journey, IJourneyInteractor.JourneyListener listener);
}
