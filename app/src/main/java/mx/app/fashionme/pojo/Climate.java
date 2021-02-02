package mx.app.fashionme.pojo;

public class Climate {
    private int id;
    private Spanish spanish;
    private English english;
    private String type;

    public Climate() {}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Climate{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", type='" + type + '\'' +
                '}';
    }
}
