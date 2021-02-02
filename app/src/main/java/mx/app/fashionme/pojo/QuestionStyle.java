package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

/**
 * Created by heriberto on 4/04/18.
 */

public class QuestionStyle implements Parcelable {
    private int id;
    private Spanish spanish;
    private English english;
    private String genre;
    private Base<ArrayList<AnswerStyle>> answers;

    public QuestionStyle(int id, Spanish spanish, English english, String genre, Base<ArrayList<AnswerStyle>> answers) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.genre = genre;
        this.answers = answers;
    }

    protected QuestionStyle(Parcel in) {
        id = in.readInt();
        spanish = in.readParcelable(Spanish.class.getClassLoader());
        english = in.readParcelable(English.class.getClassLoader());
        genre = in.readString();
    }

    public static final Creator<QuestionStyle> CREATOR = new Creator<QuestionStyle>() {
        @Override
        public QuestionStyle createFromParcel(Parcel in) {
            return new QuestionStyle(in);
        }

        @Override
        public QuestionStyle[] newArray(int size) {
            return new QuestionStyle[size];
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Base<ArrayList<AnswerStyle>> getAnswers() {
        return answers;
    }

    public void setAnswers(Base<ArrayList<AnswerStyle>> answers) {
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(spanish, 0);
        parcel.writeParcelable(english , 0);
        //parcel.writeParcelableArray(answers, 0);
    }
}
