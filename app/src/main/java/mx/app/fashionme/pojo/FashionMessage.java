package mx.app.fashionme.pojo;

public class FashionMessage {

    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;
    private String created_at;
    private String email;

    public FashionMessage() {

    }

    public FashionMessage(String text, String name, String photoUrl, String imageUrl, String created_at, String email) {
        this.text       = text;
        this.name       = name;
        this.photoUrl   = photoUrl;
        this.imageUrl   = imageUrl;
        this.created_at = created_at;
        this.email      = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
