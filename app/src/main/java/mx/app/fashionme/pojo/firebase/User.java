package mx.app.fashionme.pojo.firebase;

public class User {

    private String id;
    private String client;
    private String assessor;

    public User(){}

    public User(String client, String assessor) {
        this.client = client;
        this.assessor = assessor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClientl(String client) {
        this.client = client;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", client='" + client + '\'' +
                ", assessor='" + assessor + '\'' +
                '}';
    }
}
