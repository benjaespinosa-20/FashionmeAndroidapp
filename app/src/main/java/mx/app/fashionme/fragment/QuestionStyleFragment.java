package mx.app.fashionme.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.QuestionStyleInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.QuestionStyle;
import mx.app.fashionme.presenter.QuestionStylePresenter;
import mx.app.fashionme.presenter.StyleTestPresenter;
import mx.app.fashionme.presenter.interfaces.IQuestionStylePresenter;
import mx.app.fashionme.view.interfaces.IQuestionStyleView;

/**
 * Created by heriberto on 3/04/18.
 */

public class QuestionStyleFragment extends Fragment implements IQuestionStyleView {

    public static final String TAG = QuestionStyleFragment.class.getSimpleName();

    // Miembros UI
    private TextView tvQuestionStyle;
    private RadioGroup rgQuestionStyle;

    private AnswerStyle toAnswer;
    private QuestionStyle question;

    private IQuestionStylePresenter presenter;

    public QuestionStyleFragment() {
        // Required empty public constructor
    }

    public static QuestionStyleFragment newInstance() {
        QuestionStyleFragment fragment = new QuestionStyleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View uiRoot = inflater.inflate(R.layout.fragment_test_style, container, false);

        tvQuestionStyle = uiRoot.findViewById(R.id.tvQuestionStyle);
        rgQuestionStyle = uiRoot.findViewById(R.id.rgQuestionStyle);

        Bundle bundle = this.getArguments();
        question = bundle.getParcelable(StyleTestPresenter.QUESTION);
        toAnswer = bundle.getParcelable(StyleTestPresenter.ANSWERS);

        setUpPresenterQuestionsStyle();
        presenter.getAnswers(question);

        return uiRoot;
    }

    @Override
    public void setQuestionWithAnswers(String question, ArrayList<RadioButton> answers) {
        tvQuestionStyle.setText(question);
        for (RadioButton radioButton : answers){
            rgQuestionStyle.addView(radioButton);
        }
    }

    @Override
    public void clickAnswer(RadioButton radioButton) {

    }

    @Override
    public void setError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUpPresenterQuestionsStyle() {
        presenter = new QuestionStylePresenter(this, new QuestionStyleInteractor(toAnswer), getContext());
    }
}
