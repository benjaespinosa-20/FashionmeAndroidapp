package mx.app.fashionme.pojo;

public class FaceType {
    private String id;
    private String name;
    private float value;
    private Spanish spanish;
    private English english;
    private String urlImage;

    public FaceType() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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
        return "FaceType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", spanish=" + spanish +
                ", english=" + english +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
