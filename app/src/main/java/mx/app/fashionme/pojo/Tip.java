package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

/**
 * Created by heriberto on 26/03/18.
 */

public class Tip {
    private int id;
    private Spanish spanish;
    private English english;
    private Base<ArrayList<ImageTrend>> images;
    private String date;
    private String type;
    private int favorite;

    public Tip() {}

    public Tip(int id, Spanish spanish, English english, Base<ArrayList<ImageTrend>> images, String date) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.images = images;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Spanish getSpanish() {
        return spanish;
    }

    public void setSpanish(Spanish spanish) {
        this.spanish = spanish;
    }

    public English getEnglish() {
        return english;
    }

    public void setEnglish(English english) {
        this.english = english;
    }

    public Base<ArrayList<ImageTrend>> getImages() {
        return images;
    }

    public void setImages(Base<ArrayList<ImageTrend>> images) {
        this.images = images;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", images=" + images +
                ", date='" + date + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
