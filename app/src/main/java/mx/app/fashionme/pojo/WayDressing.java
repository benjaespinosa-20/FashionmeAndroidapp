package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class WayDressing {

    private int id;
    private Spanish spanish;
    private English english;
    private String urlImage;
    private Base<ArrayList<DressCode>> dreesCodes;
    private String date;
    private String type;
    private int favorite;

    public WayDressing() {}

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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Base<ArrayList<DressCode>> getDreesCodes() {
        return dreesCodes;
    }

    public void setDreesCodes(Base<ArrayList<DressCode>> dreesCodes) {
        this.dreesCodes = dreesCodes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
