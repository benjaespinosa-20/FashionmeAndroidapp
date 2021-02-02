package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import mx.app.fashionme.db.LooksConstructor;
import mx.app.fashionme.interactor.interfaces.ILooksInteractor;
import mx.app.fashionme.pojo.Look;

public class LooksInteractor implements ILooksInteractor {

    private static final String TAG = LooksInteractor.class.getSimpleName();

    @Override
    public void getDataLooksFromDB(Context context, OnGetLooksFinishedListener listener) {
        LooksConstructor constructor = new LooksConstructor(context);
        if (constructor.getDataLooks() == null){
            listener.onError("Ocurrio un error al obtener los looks");
            return;
        }

        listener.onSuccess(constructor.getDataLooks());
    }

    @Override
    public void saveDateToLook(Context context, Look look, String date, OnSaveDateFinishedListener listener) {

        if (date == null) {
            listener.onErrorSavedDate("Debes elegir una fecha.");
            return;
        }

        look.setDate(date);
        LooksConstructor constructor = new LooksConstructor(context);
        try {
            constructor.updateDateLook(look);
            listener.onSuccessSavedDate();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onErrorSavedDate(e.getMessage());
        }
    }
}
