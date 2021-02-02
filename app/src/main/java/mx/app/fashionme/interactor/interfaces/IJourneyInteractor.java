package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Journey;

public interface IJourneyInteractor {
    void getDataJourneys(Context context, JourneyListener listener);

    interface JourneyListener {
        void onGetJourneys(ArrayList<Journey> journeys);
        void onError(String err);
        void onGetJourneyById(Journey journey);
        void onCheckFav(boolean isFav);
        void onAddJourneyFav();
        void onRemoveJourneyFav();
        void showToast(String message);
        void onSetDialog(MaterialDialog.Builder dialog, ArrayList<String> names);
    }
}
