package mx.app.fashionme.pojo.firebase;

public class Message {

    private String uid;
    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;
    private String created_at;
    private String email;

    public Message(){}

    public Message(String text, String name, String photoUrl, String imageUrl, String created_at, String email){
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
        this.created_at = created_at;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    @Override
    public String toString() {
        return "Message{" +
                "uid='" + uid + '\'' +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", created_at='" + created_at + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
