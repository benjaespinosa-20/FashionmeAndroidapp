package mx.app.fashionme.data.models.acquire;

public class SkuDetailsModel {

    private  String sku;
    private String type;
    private String price;
    private String title;
    private String description;
    private String originalJson;

    public SkuDetailsModel(String sku, String type, String price, String title, String description, String originalJson) {
        this.sku = sku;
        this.type = type;
        this.price = price;
        this.title = title;
        this.description = description;
        this.originalJson = originalJson;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

}
