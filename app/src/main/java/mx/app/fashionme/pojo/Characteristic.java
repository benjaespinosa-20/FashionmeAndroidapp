package mx.app.fashionme.pojo;

/**
 * Created by desarrollo1 on 14/03/18.
 */

public class Characteristic {
    private int id;
    private Spanish spanish;
    private English english;
    private boolean selection;

    public Characteristic(int id, Spanish spanish, English english, boolean selection) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.selection = selection;
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

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
