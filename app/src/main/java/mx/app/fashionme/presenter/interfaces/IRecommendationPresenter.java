package mx.app.fashionme.presenter.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Clothe;

public interface IRecommendationPresenter {
//    void getRecommendations();

    void saveAllToCart(ArrayList<Clothe> clothesAll);

    void checkConditions();

    void showExplanation();

    void getWeather(double latitude, double longitude);

    void stopLocationUpdates();

    void setSAnalytics();
}
