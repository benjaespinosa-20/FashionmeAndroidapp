package mx.app.fashionme.interactor.interfaces;

import android.content.Context;

public interface IResultInteractor {

    void getResults(Context context, OnGetResultListener callback);

    void sendTokenAPIBody(String token, Context context, OnGetResultListener callback);

    interface OnGetResultListener {
        void onSuccess(String name, String body, String color, String imageUrl);
        void onFail(String err);
    }
}
