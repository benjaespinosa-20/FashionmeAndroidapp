package mx.app.fashionme.restApi.model;

public class BaseLogin<T> {

    private T data;
    private String access_token;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
