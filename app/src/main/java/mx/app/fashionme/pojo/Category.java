package mx.app.fashionme.pojo;

/**
 * Created by heriberto on 12/03/18.
 */

public class Category {
    private int id;
    private Spanish spanish;
    private English english;
    private String genre;
    private String urlImage;

    public Category(){}

    public Category(int id, Spanish spanish, English english, String genre, String urlImage) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.genre = genre;
        this.urlImage = urlImage;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
