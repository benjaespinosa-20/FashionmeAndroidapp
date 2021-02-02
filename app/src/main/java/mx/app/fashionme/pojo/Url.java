package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Url implements Parcelable {
    private int id;
    private String type;
    private String urlImage;
    private String mimeType;

    public Url() {
    }

    protected Url(Parcel in) {
        id = in.readInt();
        type = in.readString();
        urlImage = in.readString();
        mimeType = in.readString();
    }

    public static final Creator<Url> CREATOR = new Creator<Url>() {
        @Override
        public Url createFromParcel(Parcel in) {
            return new Url(in);
        }

        @Override
        public Url[] newArray(int size) {
            return new Url[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(urlImage);
        dest.writeString(mimeType);
    }
}
