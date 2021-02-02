package mx.app.fashionme.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import mx.app.fashionme.interactor.ShoppingInteractor;
import mx.app.fashionme.interactor.interfaces.IShoppingInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.presenter.interfaces.IShoppingPresenter;
import mx.app.fashionme.view.ShoppingActivity;
import mx.app.fashionme.view.interfaces.IShoppingView;

public class ShoppingPresenter implements IShoppingPresenter, IShoppingInteractor.ClotheShoppingListener {
    public static final String TAG = ShoppingPresenter.class.getSimpleName();

    private IShoppingView view;
    private IShoppingInteractor interactor;
    private Context context;

    public ShoppingPresenter(ShoppingActivity view, ShoppingInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void onGetCart(ArrayList<Clothe> clothes) {
        if (view != null){
            if (clothes.size() == 0) {
                view.showEmptyList();
                return;
            }

            view.generateLinearLayoutVertical();
            view.initializeAdapter(view.createAdapter(clothes));
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }

    @Override
    public void getClothes() {
        interactor.getDataFromDB(context, this);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Compras/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }
}
