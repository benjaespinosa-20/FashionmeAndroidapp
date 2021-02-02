package mx.app.fashionme.pojo;

import java.util.List;

public class ColorQuestionsViewModel {

    private String question;
    private List<ColorAnswerViewModel> answers;
    private int state = -1;
    private String userAnswer;

    public ColorQuestionsViewModel(String question, List<ColorAnswerViewModel> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ColorAnswerViewModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ColorAnswerViewModel> answers) {
        this.answers = answers;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public String toString() {
        return "ColorQuestionsViewModel{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                ", state=" + state +
                ", userAnswer='" + userAnswer + '\'' +
                '}';
    }
}
