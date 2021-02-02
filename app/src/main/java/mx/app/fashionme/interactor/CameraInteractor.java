package mx.app.fashionme.interactor;

import android.content.Context;

import mx.app.fashionme.interactor.interfaces.ICameraInteractor;

public class CameraInteractor implements ICameraInteractor {

    private static final String TAG = "CameraInteractor";
    private final Context context;

    public CameraInteractor(Context context) {
        this.context    = context;
    }
}
