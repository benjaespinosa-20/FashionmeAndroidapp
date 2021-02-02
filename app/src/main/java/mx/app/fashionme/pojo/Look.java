package mx.app.fashionme.pojo;

public class Look {
    private int id;
    private String uri;
    private String date;

    public Look() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Look{" +
                "id=" + id +
                ", uri='" + uri +
                ", date='" + date + '\'' +
                '}';
    }
}
