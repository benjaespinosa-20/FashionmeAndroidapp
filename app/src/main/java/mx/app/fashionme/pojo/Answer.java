package mx.app.fashionme.pojo;

/**
 * Created by heriberto on 22/03/18.
 */

public class Answer {

    private String question_1;
    private String question_2;
    private String question_3;
    private String question_4;
    private String question_5;
    private String question_6;
    private String question_7;
    private String question_8;
    private String question_9;

    // Respuestas a preguntas extra
    private int spring;
    private int summer;
    private int fall;
    private int winter;

    public Answer() {
    }

    public String getQuestion_1() {
        return question_1;
    }

    public void setQuestion_1(String question_1) {
        this.question_1 = question_1;
    }

    public String getQuestion_2() {
        return question_2;
    }

    public void setQuestion_2(String question_2) {
        this.question_2 = question_2;
    }

    public String getQuestion_3() {
        return question_3;
    }

    public void setQuestion_3(String question_3) {
        this.question_3 = question_3;
    }

    public String getQuestion_4() {
        return question_4;
    }

    public void setQuestion_4(String question_4) {
        this.question_4 = question_4;
    }

    public String getQuestion_5() {
        return question_5;
    }

    public void setQuestion_5(String question_5) {
        this.question_5 = question_5;
    }

    public String getQuestion_6() {
        return question_6;
    }

    public void setQuestion_6(String question_6) {
        this.question_6 = question_6;
    }

    public String getQuestion_7() {
        return question_7;
    }

    public void setQuestion_7(String question_7) {
        this.question_7 = question_7;
    }

    public String getQuestion_8() {
        return question_8;
    }

    public void setQuestion_8(String question_8) {
        this.question_8 = question_8;
    }

    public String getQuestion_9() {
        return question_9;
    }

    public void setQuestion_9(String question_9) {
        this.question_9 = question_9;
    }

    public int getSpring() {
        return spring;
    }

    public void setSpring() {
        this.spring++;
    }

    public int getSummer() {
        return summer;
    }

    public void setSummer() {
        this.summer++;
    }

    public int getFall() {
        return fall;
    }

    public void setFall() {
        this.fall++;
    }

    public int getWinter() {
        return winter;
    }

    public void setWinter() {
        this.winter++;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question_1='" + question_1 + '\'' +
                ", question_2='" + question_2 + '\'' +
                ", question_3='" + question_3 + '\'' +
                ", question_4='" + question_4 + '\'' +
                ", question_5='" + question_5 + '\'' +
                ", question_6='" + question_6 + '\'' +
                ", question_7='" + question_7 + '\'' +
                ", question_8='" + question_8 + '\'' +
                ", question_9='" + question_9 + '\'' +
                ", spring=" + spring +
                ", summer=" + summer +
                ", fall=" + fall +
                ", winter=" + winter +
                '}';
    }
}
