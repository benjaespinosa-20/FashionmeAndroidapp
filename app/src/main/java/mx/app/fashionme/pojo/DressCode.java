package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class DressCode {
    private int id;
    private Spanish spanish;
    private English english;
    private String genre;
    private Base<ArrayList<ImageTrend>> images;

    public DressCode() {}

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Base<ArrayList<ImageTrend>> getImages() {
        return images;
    }

    public void setImages(Base<ArrayList<ImageTrend>> images) {
        this.images = images;
    }
}
