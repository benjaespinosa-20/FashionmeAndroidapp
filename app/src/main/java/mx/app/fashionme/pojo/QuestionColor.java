package mx.app.fashionme.pojo;

import java.io.Serializable;
import java.util.ArrayList;

import mx.app.fashionme.restApi.model.Base;

public class QuestionColor implements Serializable {
    private int id;
    private Spanish spanish;
    private English english;
    private String type;
    private String path;
    private Base<ArrayList<AnswerColor>> answers;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Base<ArrayList<AnswerColor>> getAnswers() {
        return answers;
    }

    public void setAnswers(Base<ArrayList<AnswerColor>> answers) {
        this.answers = answers;
    }
}
