package mx.app.fashionme.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class AnswerStyle implements Parcelable {
    private int id;
    private Spanish spanish;
    private English english;
    private String indent;
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;

    public AnswerStyle(int id, Spanish spanish, English english, String indent) {
        this.id = id;
        this.spanish = spanish;
        this.english = english;
        this.indent = indent;
    }

    public AnswerStyle(int a, int b, int c, int d, int e, int f, int g) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }

    protected AnswerStyle(Parcel in) {
        id = in.readInt();
        spanish = in.readParcelable(Spanish.class.getClassLoader());
        english = in.readParcelable(English.class.getClassLoader());
        indent = in.readString();
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();
        e = in.readInt();
        f = in.readInt();
        g = in.readInt();
    }

    public static final Creator<AnswerStyle> CREATOR = new Creator<AnswerStyle>() {
        @Override
        public AnswerStyle createFromParcel(Parcel in) {
            return new AnswerStyle(in);
        }

        @Override
        public AnswerStyle[] newArray(int size) {
            return new AnswerStyle[size];
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

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public int getA() {
        return a;
    }

    public void setA() {
        this.a++;
    }

    public int getB() {
        return b;
    }

    public void setB() {
        this.b++;
    }

    public int getC() {
        return c;
    }

    public void setC() {
        this.c++;
    }

    public int getD() {
        return d;
    }

    public void setD() {
        this.d++;
    }

    public int getE() {
        return e;
    }

    public void setE() {
        this.e++;
    }

    public int getF() {
        return f;
    }

    public void setF() {
        this.f++;
    }

    public int getG() {
        return g;
    }

    public void setG() {
        this.g++;
    }

    @Override
    public String toString() {
        return "AnswerStyle{" +
                "indent='" + indent + '\'' +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                ", g=" + g +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(spanish, 0);
        parcel.writeParcelable(english, 0);
        parcel.writeString(indent);
        parcel.writeInt(a);
        parcel.writeInt(b);
        parcel.writeInt(c);
        parcel.writeInt(d);
        parcel.writeInt(e);
        parcel.writeInt(f);
        parcel.writeInt(g);
    }
}
