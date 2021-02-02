package mx.app.fashionme.restApi.model;


import mx.app.fashionme.pojo.User;

/**
 * Created by heriberto on 15/03/18.
 */

public class UserResponse {
    private Base<User> user;
//    private String access_token;

    public Base<User> getUser() {
        return user;
    }

    public void setUser(Base<User> user) {
        this.user = user;
    }

//    public String getAccess_token() {
//        return access_token;
//    }
//
//    public void setAccess_token(String access_token) {
//        this.access_token = access_token;
//    }
}
