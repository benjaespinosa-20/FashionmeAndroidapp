package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class Favorite {

    private Base<ArrayList<Trend>> trends;
    private Base<ArrayList<Tip>> tips;
    private Base<ArrayList<Journey>> journeys;
    private Base<ArrayList<Clothe>> clothes;
    private Base<ArrayList<WayDressing>> ways_dressing;
    private String type;

    public Favorite() {
    }

    public Base<ArrayList<Trend>> getTrends() {
        return trends;
    }

    public void setTrends(Base<ArrayList<Trend>> trends) {
        this.trends = trends;
    }

    public Base<ArrayList<Tip>> getTips() {
        return tips;
    }

    public void setTips(Base<ArrayList<Tip>> tips) {
        this.tips = tips;
    }

    public Base<ArrayList<Journey>> getJourneys() {
        return journeys;
    }

    public void setJourneys(Base<ArrayList<Journey>> journeys) {
        this.journeys = journeys;
    }

    public Base<ArrayList<Clothe>> getClothes() {
        return clothes;
    }

    public void setClothes(Base<ArrayList<Clothe>> clothes) {
        this.clothes = clothes;
    }

    public Base<ArrayList<WayDressing>> getWays_dressing() {
        return ways_dressing;
    }

    public void setWays_dressing(Base<ArrayList<WayDressing>> ways_dressing) {
        this.ways_dressing = ways_dressing;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "trends=" + trends +
                ", tips=" + tips +
                ", journeys=" + journeys +
                ", clothes=" + clothes +
                ", ways_dressing=" + ways_dressing +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
