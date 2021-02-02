package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

/**
 * Created by desarrollo1 on 13/03/18.
 */

public class Clothe {
    private int id;
    private Spanish spanish;
    private English english;
    private String clothePrice;
    private String clotheLink;
    private String urlImage;
    private String genre;
    private Base<ArrayList<Brand>> brands;
    private Base<ArrayList<Subcategory>> subcategories;
    private boolean isInCar;
    private int favorite;
    private String type;

    public Clothe(int id, Spanish spanish, English english, String clothePrice, String clotheLink, String urlImage, String genre, Base<ArrayList<Brand>> brands) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.clothePrice = clothePrice;
        this.clotheLink = clotheLink;
        this.urlImage = urlImage;
        this.genre = genre;
        this.brands = brands;
    }

    public Clothe() {}

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

    public String getClothePrice() {
        return clothePrice;
    }

    public void setClothePrice(String clothePrice) {
        this.clothePrice = clothePrice;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Base<ArrayList<Brand>> getBrands() {
        return brands;
    }

    public void setBrands(Base<ArrayList<Brand>> brands) {
        this.brands = brands;
    }

    public Base<ArrayList<Subcategory>> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Base<ArrayList<Subcategory>> subcategories) {
        this.subcategories = subcategories;
    }

    public String getClotheLink() {
        return clotheLink;
    }

    public void setClotheLink(String clotheLink) {
        this.clotheLink = clotheLink;
    }

    public boolean isInCar() {
        return isInCar;
    }

    public void setInCar(boolean inCar) {
        isInCar = inCar;
    }

    @Override
    public String toString() {
        return "Clothe{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", clothePrice='" + clothePrice + '\'' +
                ", clotheLink='" + clotheLink + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", genre='" + genre + '\'' +
                ", brands=" + brands +
                ", subcategories=" + subcategories +
                ", isInCar=" + isInCar + '\'' +
                ", favorite=" + favorite + '\'' +
                ", type=" + type +
                '}';
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
