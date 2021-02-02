package mx.app.fashionme.pojo;

public class Forgot {

    private String email;
    private String data;

    public Forgot(String email) {
        this.email = email;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
