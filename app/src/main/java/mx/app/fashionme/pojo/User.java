package mx.app.fashionme.pojo;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

/**
 * Created by heriberto on 12/03/18.
 */

public class User {

    private int id;
    private String name;
    private String genre;
    private String role;
    private String email;
    private String password;
    private String password_confirmation;
    private String country;
    private String birthday;
    private String token;
    private boolean trial;
    private boolean premium;
    private Base<Colorimetry> color;
    private Base<BodyType> body;
    private Base<ArrayList<Characteristic>> characteristics;
    private Base<Style> style;
    private Base<FaceType> face;

    public User(int id, String name, String genre, String email, String password, String password_confirmation, String token, Base<Colorimetry> color, Base<BodyType> body) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.token = token;
        this.color = color;
        this.body = body;
    }

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name,
                String genre, String password_confirmation,
                String country, String birthday) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.genre = genre;
        this.password_confirmation = password_confirmation;
        this.country = country;
        this.birthday = birthday;
        this.trial  = true;
    }

    public User(String email, String password, String name,
                String genre, String password_confirmation,
                String country) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.genre = genre;
        this.password_confirmation = password_confirmation;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Base<Colorimetry> getColor() {
        return color;
    }

    public void setColor(Base<Colorimetry> color) {
        this.color = color;
    }

    public Base<BodyType> getBody() {
        return body;
    }

    public void setBody(Base<BodyType> body) {
        this.body = body;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public Base<ArrayList<Characteristic>> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Base<ArrayList<Characteristic>> characteristics) {
        this.characteristics = characteristics;
    }


    public Base<Style> getStyle() {
        return style;
    }

    public void setStyle(Base<Style> style) {
        this.style = style;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Base<FaceType> getFace() {
        return face;
    }

    public void setFace(Base<FaceType> face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                ", country='" + country + '\'' +
                ", token='" + token + '\'' +
                ", trial=" + trial +
                ", premium=" + premium +
                ", color=" + color +
                ", body=" + body +
                ", characteristics=" + characteristics +
                ", style=" + style +
                ", face=" + face +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isTrial() {
        return trial;
    }

    public void setTrial(boolean trial) {
        this.trial = trial;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;

    }
}
