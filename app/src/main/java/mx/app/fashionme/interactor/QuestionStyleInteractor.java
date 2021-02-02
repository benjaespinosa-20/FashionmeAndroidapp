package mx.app.fashionme.interactor;

import android.content.Context;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IQuestionStyleInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;

public class QuestionStyleInteractor implements IQuestionStyleInteractor {

    public static final String TAG = IQuestionStyleInteractor.class.getSimpleName();
    private AnswerStyle toAnswer;

    public QuestionStyleInteractor(AnswerStyle toAnswer) {
        this.toAnswer = toAnswer;
    }

    @Override
    public void getAnswers(Context context, QuestionStyle questionStyle, OnGetQuestionListener callback) {
        if (questionStyle == null){
            callback.onError("Error al cargar las preguntas");
            return;
        }

        String lang = context.getString(R.string.app_language);
        ArrayList<RadioButton> radioButtons = new ArrayList<>();

        switch (lang.toLowerCase()) {
            case "spanish":
                for (AnswerStyle answer : questionStyle.getAnswers().getData()){
                    final RadioButton nuevoRadio = crearRadioButton(answer.getSpanish().getAnswer(), answer.getIndent(), context);
                    radioButtons.add(nuevoRadio);
                    nuevoRadio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAnswer(nuevoRadio);
                        }
                    });
                }
                callback.onSuccess(questionStyle.getSpanish().getQuestion(), radioButtons);
                break;

            case "english":
                for (AnswerStyle answerStyle : questionStyle.getAnswers().getData()) {
                    final RadioButton nuevoRadio = crearRadioButton(answerStyle.getEnglish().getAnswer(), answerStyle.getIndent(), context);
                    radioButtons.add(nuevoRadio);
                    nuevoRadio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAnswer(nuevoRadio);
                        }
                    });
                }
                callback.onSuccess(questionStyle.getEnglish().getQuestion(), radioButtons);
                break;

            default:
                for (AnswerStyle answer : questionStyle.getAnswers().getData()){
                    final RadioButton nuevoRadio = crearRadioButton(answer.getSpanish().getAnswer(), answer.getIndent(), context);
                    radioButtons.add(nuevoRadio);
                    nuevoRadio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAnswer(nuevoRadio);
                        }
                    });
                }
                callback.onSuccess(questionStyle.getSpanish().getQuestion(), radioButtons);
                break;
        }
    }

    @Override
    public void setAnswer(RadioButton radioButton) {
        String indent = radioButton.getTag().toString();
        switch (indent) {
            case "a":
                toAnswer.setA();
                break;
            case "b":
                toAnswer.setB();
                break;
            case "c":
                toAnswer.setC();
                break;
            case "d":
                toAnswer.setD();
                break;
            case "e":
                toAnswer.setE();
                break;
            case "f":
                toAnswer.setF();
                break;
            case "g":
                toAnswer.setG();
                break;
        }
    }

    private RadioButton crearRadioButton(String answer, String indent, Context context) {
        RadioButton nuevoRadio = new RadioButton(context);
        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        nuevoRadio.setLayoutParams(params);
        nuevoRadio.setText(answer);
        nuevoRadio.setTag(indent);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.arial_bold);
        nuevoRadio.setTypeface(typeface);
        return nuevoRadio;
    }

}
