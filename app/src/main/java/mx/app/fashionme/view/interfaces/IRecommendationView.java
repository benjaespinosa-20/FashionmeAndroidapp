package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.adapter.ClotheAdapter;
import mx.app.fashionme.adapter.RecommendationAdapter;
import mx.app.fashionme.pojo.Clothe;

public interface IRecommendationView {
    void generateGridLayout(int columns);

    RecommendationAdapter createAdapter(ArrayList<Clothe> clothes);

    void initializeAdapter(RecommendationAdapter adapter);

    void showEmptyList();

    void showOffline();

    void setUpToolbar();

    boolean isOnline();

    void setUpPresenterRecommendations();

    void getRecommendations();

    void showError(String err);

    void removeFabCart();

    void showProgressBar(boolean show);

    void showExplication();

    void showNeverAskDialog();

    void showAlertGPS();
}
