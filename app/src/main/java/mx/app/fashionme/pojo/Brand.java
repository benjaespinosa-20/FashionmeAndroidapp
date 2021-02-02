package mx.app.fashionme.pojo;

/**
 * Created by heriberto on 5/04/18.
 */

public class Brand {
    private int id;
    private String brand;

    public Brand(int id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
