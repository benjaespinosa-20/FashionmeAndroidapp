package mx.app.fashionme.pojo;

public class Colorimetry {

    private int id;
    private Spanish spanish;
    private English english;
    private String urlImage;

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

    @Override
    public String toString() {
        return "Colorimetry{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
