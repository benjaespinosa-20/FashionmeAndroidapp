package mx.app.fashionme.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.view.ChecklistActivity;
import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IJourneyInteractor;
import mx.app.fashionme.interactor.interfaces.IShowJourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mx.app.fashionme.adapter.JourneyListAdapter.ID_JOURNEY;

public class ShowJourneyInteractor implements IShowJourneyInteractor {

    private static final String TAG = ShowJourneyInteractor.class.getSimpleName();

    @Override
    public void getJourney(final Context context, int idJourney, final IJourneyInteractor.JourneyListener listener) {
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<Journey>> journeyResponseCall = endpointsApi.getJourneyById(idJourney);

        journeyResponseCall.enqueue(new Callback<Base<Journey>>() {
            @Override
            public void onResponse(Call<Base<Journey>> call, Response<Base<Journey>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                listener.onGetJourneyById(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<Journey>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(context.getResources().getString(R.string.error_network));
            }
        });
    }

    @Override
    public void checkFav(Context context, final int idJourney, final IJourneyInteractor.JourneyListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<Journey>> call = endpointsApi.getFavoriteByUSerByIdJourney(userId, idJourney, "journey");

        call.enqueue(new Callback<Base<Journey>>() {
            @Override
            public void onResponse(Call<Base<Journey>> call, Response<Base<Journey>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.showToast(response.errorBody().string());
                            listener.onCheckFav(false);
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.showToast("Algo salió mal al checar favoritos, intenta más tarde");
                            listener.onCheckFav(false);
                            return;
                        }
                    } else {
                        if (response.code() != 404){
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            listener.showToast(apiError != null ? apiError.getError(): "Algo salió mal al checar favoritos, intenta más tarde");
                        }
                        listener.onCheckFav(false);
                        return;
                    }
                }

                if (response.body().getData().getId() != idJourney){
                    listener.onCheckFav(false);
                    return;
                }
                listener.onCheckFav(true);
            }

            @Override
            public void onFailure(Call<Base<Journey>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.showToast(t.getMessage());
                listener.onCheckFav(false);
            }
        });
    }

    @Override
    public void removeFav(Context context, final int idJourney, final IJourneyInteractor.JourneyListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<Journey>> call = endpointsApi.removeFavoriteByUserByJourney(userId, idJourney, "journey");

        call.enqueue(new Callback<Base<Journey>>() {
            @Override
            public void onResponse(Call<Base<Journey>> call, Response<Base<Journey>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.showToast(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.showToast("Algo salió mal al obtener favoritos, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal al obtener favoritos, intenta más tarde");
                        return;
                    }
                }

                if (response.body().getData().getId() != idJourney){
                    listener.showToast("No se pudo quitar el favorito");
                    return;
                }

                listener.onRemoveJourneyFav();
            }

            @Override
            public void onFailure(Call<Base<Journey>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void addToFav(Context context, final int idJourney, final IJourneyInteractor.JourneyListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Journey journey = new Journey();
        journey.setType("journey");
        journey.setFavorite(idJourney);

        Call<Base<Journey>> call = endpointsApi.setFavoriteJourneyByUser(userId, journey);
        call.enqueue(new Callback<Base<Journey>>() {
            @Override
            public void onResponse(Call<Base<Journey>> call, Response<Base<Journey>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.e(TAG, response.errorBody().string());
                            listener.showToast(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.showToast("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.showToast(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                if (response.body().getData().getId() != idJourney){
                    listener.showToast("No se pudo agregar a favoritos");
                    return;
                }

                listener.onAddJourneyFav();
            }

            @Override
            public void onFailure(Call<Base<Journey>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void openDialog(Context context, Journey journey, MaterialDialog.Builder dialog, IJourneyInteractor.JourneyListener listener) {
        if (journey.getSubcategories().getData() == null){
            listener.showToast("Error al obtener checklist");
            return;
        }

        String lang = context.getResources().getString(R.string.app_language);
        String genre = SessionPrefs.get(context).getGenre();

        ArrayList<String> names = new ArrayList<>();

        switch (lang) {
            case "spanish":
                for (Subcategory subcategory:journey.getSubcategories().getData()) {
                    if (genre.equals("woman")){
                        if (!subcategory.getGenre().equals("m"))
                            names.add(subcategory.getSpanish().getName());
                    } else if (genre.equals("man")){
                        if (!subcategory.getGenre().equals("w"))
                            names.add(subcategory.getSpanish().getName());
                    }
                }
                break;
            case "english":
                for (Subcategory subcategory:journey.getSubcategories().getData()) {
                    if (genre.equals("woman")){
                        if (!subcategory.getGenre().equals("m"))
                            names.add(subcategory.getEnglish().getName());
                    } else if (genre.equals("man")) {
                        if (!subcategory.getGenre().equals("w"))
                            names.add(subcategory.getEnglish().getName());
                    }
                }
                break;
                default:
                    for (Subcategory subcategory:journey.getSubcategories().getData()) {
                        if (genre.equals("woman")){
                            if (!subcategory.getGenre().equals("m"))
                                names.add(subcategory.getSpanish().getName());
                        } else if (genre.equals("man")){
                            if (!subcategory.getGenre().equals("w"))
                                names.add(subcategory.getSpanish().getName());
                        }
                    }
                    break;
        }

        listener.onSetDialog(dialog, names);
    }

    @Override
    public void saveChecklist(ArrayList<String> checklistSelected) {
        for (int i = 0; i < checklistSelected.size(); i++) {
            Log.d(TAG, checklistSelected.get(i));
        }
    }

    @Override
    public void openActivity(Context context, Journey journey, IJourneyInteractor.JourneyListener listener) {
        if (journey.getSubcategories().getData().size() == 0){
            listener.showToast("No se puede mostrar checklist");
            return;
        }

        Intent intent = new Intent(context, ChecklistActivity.class);
        intent.putExtra(ID_JOURNEY, journey.getId());
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}