package mx.app.fashionme.pojo;

public class ColorAnswerViewModel {

    private String answer;
    private String indent;

    public ColorAnswerViewModel(String answer, String indent) {
        this.answer = answer;
        this.indent = indent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }
}
