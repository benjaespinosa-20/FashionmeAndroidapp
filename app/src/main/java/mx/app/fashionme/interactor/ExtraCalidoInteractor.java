package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IExtraCalidoInteractor;
import mx.app.fashionme.pojo.Answer;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtraCalidoInteractor implements IExtraCalidoInteractor {

    public static final String TAG = ExtraCalidoInteractor.class.getSimpleName();
}
