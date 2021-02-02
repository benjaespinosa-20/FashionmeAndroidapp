package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatAssessorClient implements Parcelable {

    private int id;
    private String fuid;
    private String client;
    private String assessor;

    public ChatAssessorClient() {}

    protected ChatAssessorClient(Parcel in) {
        id = in.readInt();
        fuid = in.readString();
        client = in.readString();
        assessor = in.readString();
    }

    public static final Creator<ChatAssessorClient> CREATOR = new Creator<ChatAssessorClient>() {
        @Override
        public ChatAssessorClient createFromParcel(Parcel in) {
            return new ChatAssessorClient(in);
        }

        @Override
        public ChatAssessorClient[] newArray(int size) {
            return new ChatAssessorClient[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
    }

    @Override
    public String toString() {
        return "ChatAssessorClient{" +
                "id=" + id +
                ", fuid='" + fuid + '\'' +
                ", client='" + client + '\'' +
                ", assessor='" + assessor + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fuid);
        parcel.writeString(client);
        parcel.writeString(assessor);
    }
}
