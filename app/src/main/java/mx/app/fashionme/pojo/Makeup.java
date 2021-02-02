package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class Makeup implements Parcelable {
    private int id;
    private Spanish spanish;
    private English english;
    private Base<ArrayList<ImageTrend>> images;
    private Base<ArrayList<Video>> videos;
    private Base<ArrayList<Url>> urls;

    public Makeup(){}

    protected Makeup(Parcel in) {
        id = in.readInt();
        spanish = in.readParcelable(Spanish.class.getClassLoader());
        english = in.readParcelable(English.class.getClassLoader());
        images = in.readParcelable(Base.class.getClassLoader());
        videos = in.readParcelable(Base.class.getClassLoader());
        urls = in.readParcelable(Base.class.getClassLoader());
    }

    public static final Creator<Makeup> CREATOR = new Creator<Makeup>() {
        @Override
        public Makeup createFromParcel(Parcel in) {
            return new Makeup(in);
        }

        @Override
        public Makeup[] newArray(int size) {
            return new Makeup[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Spanish getSpanish() {
        return spanish;
    }

    public void setSpanish(Spanish spanish) {
        this.spanish = spanish;
    }

    public English getEnglish() {
        return english;
    }

    public void setEnglish(English english) {
        this.english = english;
    }

    public Base<ArrayList<ImageTrend>> getImages() {
        return images;
    }

    public void setImages(Base<ArrayList<ImageTrend>> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Makeup{" +
                "id=" + id +
                ", spanish=" + spanish +
                ", english=" + english +
                ", images=" + images +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(spanish, i);
        parcel.writeParcelable(english, i);
    }

    public Base<ArrayList<Video>> getVideos() {
        return videos;
    }

    public void setVideos(Base<ArrayList<Video>> videos) {
        this.videos = videos;
    }

    public Base<ArrayList<Url>> getUrls() {
        return urls;
    }

    public void setUrls(Base<ArrayList<Url>> urls) {
        this.urls = urls;
    }
}
