package mx.app.fashionme.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.ICategoryInteractor;
import mx.app.fashionme.pojo.BodyType;
import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.restApi.model.CategoryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 19/03/18.
 */

public class CategoryInteractor implements ICategoryInteractor {
    private ArrayList<Category> categoriesList;

    public static final String TAG = CategoryInteractor.class.getSimpleName();

    @Override
    public void getDataFromAPI(final Context context, final OnGetCategoriesFishishedListener callback) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SessionPrefs.PREFS_FILE_NAME, 0);
        boolean isClosetIdeal = sharedPreferences.getBoolean(SessionPrefs.KEY_CLOSET_IDEAL, false);

        if (isClosetIdeal) {
            getConditions(context, callback);
        }

        final String genre = SessionPrefs.get(context).getGenre();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        final EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Category>>> categoryResponseCall = endpointsApi.getCategoriesBothGenre();
        categoryResponseCall.enqueue(new Callback<Base<ArrayList<Category>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Category>>> call, Response<Base<ArrayList<Category>>> response) {
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
                categoriesList = new ArrayList<>();
                for (Category cat:response.body().getData()) {
                    categoriesList.add(cat);
                }

                if (genre.equals("woman")){
                    Call<Base<ArrayList<Category>>> callGenre = endpointsApi.getCategoriesWomanGenre();
                    callGenre.clone().enqueue(new Callback<Base<ArrayList<Category>>>() {
                        @Override
                        public void onResponse(Call<Base<ArrayList<Category>>> call, Response<Base<ArrayList<Category>>> response) {
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

                            for (Category cat:response.body().getData()) {
                                categoriesList.add(cat);
                            }
                            callback.onSuccess(categoriesList);
                        }

                        @Override
                        public void onFailure(Call<Base<ArrayList<Category>>> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                            t.printStackTrace();
                            callback.onError(t.getMessage());
                        }
                    });
                } else {
                    Call<Base<ArrayList<Category>>> callGenre = endpointsApi.getCategoriesManGenre();
                    callGenre.clone().enqueue(new Callback<Base<ArrayList<Category>>>() {
                        @Override
                        public void onResponse(Call<Base<ArrayList<Category>>> call, Response<Base<ArrayList<Category>>> response) {
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

                            for (Category cat:response.body().getData()) {
                                categoriesList.add(cat);
                            }
                            callback.onSuccess(categoriesList);
                        }

                        @Override
                        public void onFailure(Call<Base<ArrayList<Category>>> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                            t.printStackTrace();
                            callback.onError(t.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<Base<ArrayList<Category>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }

    private void getConditions(Context context, OnGetCategoriesFishishedListener callback) {
        
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        Call<Base<User>> call = endpointsApi.getUserById(userId);

        call.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.w(TAG, "onResponse not successful: "  + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.w(TAG, "onResoonse not successful: " + apiError.getError());
                    }
                } else {
                    if (response.body().getData().getBody() == null) {
                        callback.finishAndBodyPage();
                        return;
                    }

                    if (response.body().getData().getCharacteristics().getData().size() == 0) {
                        callback.finishAndCharacteristicsPage();
                        return;
                    }

                    if (response.body().getData().getColor() == null) {
                        callback.finishAndColorPage();
                    }

                }
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.w(TAG, ">>>onFailure()", t);
                callback.onError("Error en la conexión");
            }
        }); 
    }
}
