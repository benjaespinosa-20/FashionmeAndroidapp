package mx.app.fashionme.pojo;

/**
 * Created by heriberto on 21/03/18.
 */

public class Question {
    private String question;
    private boolean yes;
    private boolean no;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isYes() {
        return yes;
    }

    public void setYes(boolean yes) {
        this.yes = yes;
    }

    public boolean isNo() {
        return no;
    }

    public void setNo(boolean no) {
        this.no = no;
    }
}
