package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.ISubcategoryInteractor;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 5/04/18.
 */

public class SubcategoryInteractor implements ISubcategoryInteractor {

    public static final String TAG = SubcategoryInteractor.class.getSimpleName();

    @Override
    public void getDataFromAPI(Context context, int categoryId, final OnGetSubcategoriesFinishedListener callback) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Subcategory>>> subcategoryResponseCall = endpointsApi.getSubcategories(categoryId);
        subcategoryResponseCall.enqueue(new Callback<Base<ArrayList<Subcategory>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Subcategory>>> call, Response<Base<ArrayList<Subcategory>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                callback.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Subcategory>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }
}
