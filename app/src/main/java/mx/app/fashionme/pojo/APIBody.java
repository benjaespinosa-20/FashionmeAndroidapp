package mx.app.fashionme.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIBody {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("cuerpo")
    private String cuerpo;

    public APIBody() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Override
    public String toString() {
        return "APIBody{" +
                "status='" + status + '\'' +
                ", token='" + token + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                '}';
    }
}
