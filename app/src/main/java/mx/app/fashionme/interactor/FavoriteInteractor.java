package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.db.FavConstructor;
import mx.app.fashionme.interactor.interfaces.IFavoriteInteractor;
import mx.app.fashionme.pojo.Favorite;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteInteractor implements IFavoriteInteractor {

    public static final String TAG = FavoriteInteractor.class.getSimpleName();

    @Override
    public void getDataFromDB(Context context, String type, ClotheFavoritesListener listener) {
        FavConstructor constructor = new FavConstructor(context);

        switch (type) {
            case "favorites":
                //listener.onGetFavorites(constructor.getClothesFavorites());
                break;
            case "cart":
                //listener.onGetCart(constructor.getClothesOnCart());
                break;
            default:
                listener.onError("Error al obtener informacion de favoritos/shopping");
                break;
        }
    }

    @Override
    public void getDataFromAPI(final Context context, final ClotheFavoritesListener listener) {

        int userId = SessionPrefs.get(context).getUserId();
        RestApiAdapter adapter = new RestApiAdapter();
        EndpointsApi endpointsApi = adapter.establecerConexionRestApi();

        Call<Base<Favorite>> call = endpointsApi.getFavoritesByUser(userId);

        call.enqueue(new Callback<Base<Favorite>>() {
            @Override
            public void onResponse(Call<Base<Favorite>> call, Response<Base<Favorite>> response) {
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
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                //Log.i(TAG, response.body().getData().toString());
                ArrayList<Favorite> favorites = new ArrayList<>();

                if (response.body().getData().getTrends().getData().size() > 0){
                    Favorite trendFavorite = new Favorite();
                    trendFavorite.setTrends(response.body().getData().getTrends());
                    trendFavorite.setType(context.getString(R.string.trends_title_favorites));
                    favorites.add(trendFavorite);
                }

                if (response.body().getData().getTips().getData().size() > 0){
                    Favorite tipFavorite = new Favorite();
                    tipFavorite.setTips(response.body().getData().getTips());
                    tipFavorite.setType(context.getString(R.string.tips_title_favorites));
                    favorites.add(tipFavorite);
                }

                if (response.body().getData().getJourneys().getData().size() > 0){
                    Favorite journeyFavorite = new Favorite();
                    journeyFavorite.setJourneys(response.body().getData().getJourneys());
                    journeyFavorite.setType(context.getString(R.string.trips_title_favorites));
                    favorites.add(journeyFavorite);
                }

                if (response.body().getData().getClothes().getData().size() > 0){
                    Favorite clotheFavorite = new Favorite();
                    clotheFavorite.setClothes(response.body().getData().getClothes());
                    clotheFavorite.setType(context.getString(R.string.clothes_title_favorites));
                    favorites.add(clotheFavorite);
                }

                if (response.body().getData().getWays_dressing().getData().size() > 0){
                    Favorite wayFavorite = new Favorite();
                    wayFavorite.setWays_dressing(response.body().getData().getWays_dressing());
                    wayFavorite.setType(context.getString(R.string.ways_title_favorites));
                    favorites.add(wayFavorite);
                }

                listener.onGetFavorites(favorites);
            }

            @Override
            public void onFailure(Call<Base<Favorite>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }
}
