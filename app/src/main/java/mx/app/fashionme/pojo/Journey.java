package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class Journey {

    private int id;
    private Spanish spanish;
    private English english;
    private Base<ArrayList<ImageTrend>> images;
    private Base<ArrayList<Subcategory>> subcategories;

    private String type;
    private int favorite;

    public Journey() {
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

    public Base<ArrayList<Subcategory>> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Base<ArrayList<Subcategory>> subcategories) {
        this.subcategories = subcategories;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", images=" + images +
                ", subcategories=" + subcategories +
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
