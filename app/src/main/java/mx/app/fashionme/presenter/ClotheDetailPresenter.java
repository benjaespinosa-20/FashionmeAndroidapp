package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IClotheDetailInteractor;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.IClotheDetailPresenter;
import mx.app.fashionme.view.interfaces.IClotheDetailView;

/**
 * Created by heriberto on 14/03/18.
 */

public class ClotheDetailPresenter implements IClotheDetailPresenter, IClotheDetailInteractor.ClotheDetailListener {

    private static final String TAG = ClotheDetailPresenter.class.getSimpleName();

    private IClotheDetailView view;
    private IClotheDetailInteractor interactor;
    private Context context;
    private Bundle bundle;
    private Activity activity;

    public ClotheDetailPresenter(IClotheDetailView view, Context context, Bundle bundle, Activity activity, IClotheDetailInteractor interactor) {
        this.view       = view;
        this.context    = context;
        this.bundle     = bundle;
        this.activity   = activity;
        this.interactor = interactor;
        getDetails();
    }

    @Override
    public void getDetails() {

        String urlImage = bundle.getString("urlImage");
        String name;
        String brand    = bundle.getString("brand");
        String price    = bundle.getString("price");
        int id          = bundle.getInt("id");
        String link     = bundle.getString("link");

        String language = context.getResources().getString(R.string.app_language);

        switch (language){
            case "spanish":
                name = bundle.getString("spanishName");
                break;
            case "english":
                name = bundle.getString("englishName");
                break;
            default:
                name = bundle.getString("spanishName");
                break;
        }

        showDetails(urlImage, name, brand, price, id, link);
    }

    @Override
    public void showDetails(String url, String name, String brand, String price, int id, String link) {
        view.initializeViews();
        view.setViews(url, name, brand, price, id, link);
    }

    @Override
    public void shareOnFacebook(String urlImage) {
        Log.d(TAG, urlImage);
//        Toast.makeText(context, "Share on Facebook", Toast.LENGTH_SHORT).show();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Message";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_with)));
    }

    @Override
    public void checkFav(int idClothe) {
        interactor.checkFav(context, idClothe,this);

    }

    @Override
    public void checkShopping(int idClothe) {
        interactor.checkShopping(context, idClothe, this);
    }

    @Override
    public void addClotheToFav(int idClothe) {
        interactor.addToFav(context, idClothe, this);
    }

    @Override
    public void removeFav(int idClothe) {
        interactor.removeFavByIdClothe(context, idClothe, this);
    }

    @Override
    public void addToShopping(int idClothe, String name, String englishName, String link, String url) {
        interactor.addToCart(context, idClothe, name, englishName, link, url, this);
    }

    @Override
    public void removeShopping(int idClothe) {
        interactor.removeCartByIdClothe(context, idClothe, this);

    }

//    @Override
//    public void showZoom(Activity activity, String url) {
//        interactor.showZoomImage(activity, url);
//    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SessionPrefs.PREFS_FILE_NAME, 0);
        Bundle bundle = new Bundle();

        boolean isClosetIdeal = sharedPreferences.getBoolean(SessionPrefs.KEY_CLOSET_IDEAL, false);
        if (isClosetIdeal){
            bundle.putString("screen_name","Closet ideal/Ropa/Android");
        } else {
            bundle.putString("screen_name","Closet FME/Ropa/Android");
        }

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void onCheckFav(boolean isFav) {
        if (view != null){
            view.setFabFav(isFav);
        }
    }

    @Override
    public void onCheckShopping(boolean isInCar) {
        if (view != null) {
            view.setFabShopping(isInCar);
        }
    }

    @Override
    public void onAddClotheToFav() {
        if (view != null) {
            view.favAdded();
            Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveClotheFav() {
        if (view != null) {
            view.favRemoved();
            Toast.makeText(context, "Removido de favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddClotheToCart() {
        if (view != null) {
            view.shoppingAdded();
            Toast.makeText(context, "Agregado a compras", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveClotheCart() {
        if (view != null) {
            view.shoppingRemoved();
            Toast.makeText(context, "Removido de compras", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String err) {
        if (view != null) {
            view.showError(err);
        }
    }
}
