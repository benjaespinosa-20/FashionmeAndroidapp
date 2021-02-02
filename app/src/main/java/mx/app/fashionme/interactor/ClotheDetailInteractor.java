package mx.app.fashionme.interactor;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.db.FavConstructor;
import mx.app.fashionme.interactor.interfaces.IClotheDetailInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.English;
import mx.app.fashionme.pojo.Spanish;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClotheDetailInteractor implements IClotheDetailInteractor {

    public static final String TAG = ClotheDetailInteractor.class.getSimpleName();

    @Override
    public void checkFav(Context context, final int idClothe, final ClotheDetailListener listener) {

        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        Call<Base<Clothe>> call = endpointsApi.getFavoriteByUserByIdClothe(userId, idClothe, "clothe");

        call.enqueue(new Callback<Base<Clothe>>() {
            @Override
            public void onResponse(Call<Base<Clothe>> call, Response<Base<Clothe>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(response.errorBody().string());
                            listener.onCheckFav(false);
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            listener.onCheckFav(false);
                            return;
                        }
                    } else {
                        if (response.code() != 404){
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                            listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        }
                        listener.onCheckFav(false);
                        return;
                    }
                }

                if (response.body().getData().getId() != idClothe){
                    listener.onCheckFav(false);
                    return;
                }
                listener.onCheckFav(true);
            }

            @Override
            public void onFailure(Call<Base<Clothe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
                listener.onCheckFav(false);
            }
        });

    }

    @Override
    public void checkShopping(Context context, int idClothe, ClotheDetailListener listener) {
        FavConstructor constructor = new FavConstructor(context);
        listener.onCheckShopping(constructor.shoppingExist(idClothe));
    }

    @Override
    public void addToFav(Context context, final int idClothe, final ClotheDetailListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Clothe clothe = new Clothe();
        clothe.setType("clothe");
        clothe.setFavorite(idClothe);

        Call<Base<Clothe>> call = endpointsApi.setFavoriteClotheByUser(userId, clothe);

        call.enqueue(new Callback<Base<Clothe>>() {
            @Override
            public void onResponse(Call<Base<Clothe>> call, Response<Base<Clothe>> response) {
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
                if (response.body().getData().getId() != idClothe){
                    listener.onError("No se pudo agregar a favoritos");
                    return;
                }

                listener.onAddClotheToFav();
            }

            @Override
            public void onFailure(Call<Base<Clothe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });

    }

    @Override
    public void removeFavByIdClothe(Context context, final int idClothe, final ClotheDetailListener listener) {
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<Clothe>> call = endpointsApi.removeFavoriteByUserByClothe(userId, idClothe, "clothe");

        call.enqueue(new Callback<Base<Clothe>>() {
            @Override
            public void onResponse(Call<Base<Clothe>> call, Response<Base<Clothe>> response) {
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

                if (response.body().getData().getId() != idClothe){
                    listener.onError("No se pudo quitar el favorito");
                    return;
                }

                listener.onRemoveClotheFav();
            }

            @Override
            public void onFailure(Call<Base<Clothe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void addToCart(Context context, int idClothe, String name, String englishName, String link, String url, ClotheDetailListener listener) {
        Clothe clothe = new Clothe();
        clothe.setId(idClothe);

        Spanish spanish = new Spanish();
        spanish.setName(name);
        clothe.setSpanish(spanish);

        English english = new English();
        english.setName(englishName);
        clothe.setEnglish(english);

        clothe.setClotheLink(link);
        clothe.setUrlImage(url);

        FavConstructor constructor = new FavConstructor(context);
        constructor.addToShoppingCart(clothe);

        listener.onAddClotheToCart();
    }

    @Override
    public void removeCartByIdClothe(Context context, int idClothe, ClotheDetailListener listener) {
        FavConstructor constructor = new FavConstructor(context);
        if (constructor.removeCartShoppingByIdClothe(idClothe)) {
            listener.onRemoveClotheCart();
        }
    }

    @Override
    public void showZoomImage(Activity activity, String url) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_image_zoom, null);

        final PhotoView photoView = mView.findViewById(R.id.imageZoom);

        Picasso.get()
                .load(url)
                .into(photoView);

        mBuilder.setView(mView);

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        mDialog.getWindow().setAttributes(lp);
    }
}
