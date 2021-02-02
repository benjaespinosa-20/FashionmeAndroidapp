package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageTrend implements Parcelable {
    private int id;
    private String urlImage;

    public ImageTrend(int id, String urlImage) {
        this.id = id;
        this.urlImage = urlImage;
    }

    protected ImageTrend(Parcel in) {
        id = in.readInt();
        urlImage = in.readString();
    }

    public static final Creator<ImageTrend> CREATOR = new Creator<ImageTrend>() {
        @Override
        public ImageTrend createFromParcel(Parcel in) {
            return new ImageTrend(in);
        }

        @Override
        public ImageTrend[] newArray(int size) {
            return new ImageTrend[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "ImageTrend{" +
                "id=" + id +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(urlImage);
    }
}
