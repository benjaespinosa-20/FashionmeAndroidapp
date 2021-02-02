package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class Visage implements Parcelable {
    private int id;
    private Spanish spanish;
    private English english;
    private Base<ArrayList<ImageTrend>> images;

    public Visage() {
    }

    protected Visage(Parcel in) {
        id = in.readInt();
        spanish = in.readParcelable(Spanish.class.getClassLoader());
        english = in.readParcelable(English.class.getClassLoader());
        images = in.readParcelable(Base.class.getClassLoader());
    }

    public static final Creator<Visage> CREATOR = new Creator<Visage>() {
        @Override
        public Visage createFromParcel(Parcel in) {
            return new Visage(in);
        }

        @Override
        public Visage[] newArray(int size) {
            return new Visage[size];
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
        return "Visage{" +
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
}
