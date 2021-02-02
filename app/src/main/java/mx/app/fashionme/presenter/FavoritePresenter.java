package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.FavoriteInteractor;
import mx.app.fashionme.interactor.interfaces.IFavoriteInteractor;
import mx.app.fashionme.pojo.Favorite;
import mx.app.fashionme.presenter.interfaces.IFavoritePresenter;
import mx.app.fashionme.view.FavoriteActivity;
import mx.app.fashionme.view.interfaces.IFavoriteView;

public class FavoritePresenter implements IFavoritePresenter, IFavoriteInteractor.ClotheFavoritesListener {

    public static final String TAG = FavoritePresenter.class.getSimpleName();

    private IFavoriteView view;
    private IFavoriteInteractor interactor;
    private Context context;

    public FavoritePresenter(FavoriteActivity view, FavoriteInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getClothes() {
        interactor.getDataFromAPI(context, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Favoritos/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onGetFavorites(ArrayList<Favorite> favorites) {
        if (view != null) {
            if (favorites.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(favorites));
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }
}
