package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class DataRegisterClotheViewModel implements Parcelable {

    private int id;
    private String name;
    private boolean isSelected;

    public DataRegisterClotheViewModel() {}

    public DataRegisterClotheViewModel(int id, String name) {
        this.id     = id;
        this.name   = name;
    }

    protected DataRegisterClotheViewModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<DataRegisterClotheViewModel> CREATOR = new Creator<DataRegisterClotheViewModel>() {
        @Override
        public DataRegisterClotheViewModel createFromParcel(Parcel in) {
            return new DataRegisterClotheViewModel(in);
        }

        @Override
        public DataRegisterClotheViewModel[] newArray(int size) {
            return new DataRegisterClotheViewModel[size];
        }
    };

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "DataRegisterClotheViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
