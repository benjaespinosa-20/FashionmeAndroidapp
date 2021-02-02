package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

import mx.app.fashionme.pojo.Style;

public interface IStyleResultInteractor {

    void getResult(Context context, OnGetStyleFinished callback);

    interface OnGetStyleFinished {
        void onSuccess(Style style);
        void onError(String err);
    }
}
