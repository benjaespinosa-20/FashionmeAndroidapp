package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by heriberto on 5/04/18.
 */

public class Spanish implements Parcelable {
    // Characteristics
    private String desc;

    // Categories
    private String name;

    // Clothes
    private String comment;

    // Trends
    private String title;

    // Questions Answers Style
    private String question;
    private String answer;

    // Style
    private String style;

    // Body
    private String body_type;

    // Color
    private String color_name;

    // Face
    private String face_name;

    public Spanish() {}

    public Spanish(String name) {
        this.name = name;
    }

    protected Spanish(Parcel in) {
        desc = in.readString();
        name = in.readString();
        comment = in.readString();
        title = in.readString();
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<Spanish> CREATOR = new Creator<Spanish>() {
        @Override
        public Spanish createFromParcel(Parcel in) {
            return new Spanish(in);
        }

        @Override
        public Spanish[] newArray(int size) {
            return new Spanish[size];
        }
    };

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(desc);
        parcel.writeString(name);
        parcel.writeString(comment);
        parcel.writeString(title);
        parcel.writeString(question);
        parcel.writeString(question);
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getFace_name() {
        return face_name;
    }

    public void setFace_name(String face_name) {
        this.face_name = face_name;
    }

    @Override
    public String toString() {
        return "Spanish{" +
                "desc='" + desc + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", style='" + style + '\'' +
                ", body_type='" + body_type + '\'' +
                ", color_name='" + color_name + '\'' +
                ", face_name='" + face_name + '\'' +
                '}';
    }
}
