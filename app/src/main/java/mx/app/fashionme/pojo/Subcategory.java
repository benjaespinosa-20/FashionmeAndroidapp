package mx.app.fashionme.pojo;

/**
 * Created by heriberto on 12/03/18.
 */

public class Subcategory {

    private int id;
    private Spanish spanish;
    private English english;
    private String genre;
    private boolean selection;

    public Subcategory(int id, Spanish spanish, English english) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
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

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", genre='" + genre + '\'' +
                '}';
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
