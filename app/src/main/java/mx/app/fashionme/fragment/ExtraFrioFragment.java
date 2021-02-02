package mx.app.fashionme.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.AnswerColor;
import mx.app.fashionme.pojo.QuestionColor;
import mx.app.fashionme.view.ExtraTestActivity;
import mx.app.fashionme.view.interfaces.IExtraFrioView;


public class ExtraFrioFragment extends Fragment implements IExtraFrioView {


    @BindView(R.id.text_view_alt_question_cold)
    TextView textViewAltQuestionCold;
    @BindView(R.id.rg_cold)
    RadioGroup rgCold;
    Unbinder unbinder;
    private ExtraCalidoFragment.OnCallbackReceived mCallbackFrio;
    private List<QuestionColor> questions;

    public ExtraFrioFragment() {
        // Required empty public constructor
    }

    public static ExtraFrioFragment newInstance() {
        ExtraFrioFragment fragment = new ExtraFrioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        questions = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            questions = (List<QuestionColor>) bundle.getSerializable("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View uiRoot = inflater.inflate(R.layout.fragment_extra_frio, container, false);
        unbinder = ButterKnife.bind(this, uiRoot);

        for (QuestionColor questionColor: questions) {
            if (questionColor.getType().equals("blue_eyes")) {
                switch (getContext().getString(R.string.app_language)) {
                    case "spanish":
                        textViewAltQuestionCold.setText(questionColor.getSpanish().getQuestion());
                        setAnswers(questionColor.getAnswers().getData());
                        break;
                    case "english":
                        textViewAltQuestionCold.setText(questionColor.getEnglish().getQuestion());
                        setAnswers(questionColor.getAnswers().getData());
                        break;
                    default:
                        textViewAltQuestionCold.setText(questionColor.getSpanish().getQuestion());
                        setAnswers(questionColor.getAnswers().getData());
                        break;
                }
            }
        }

        rgCold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == 100) {
                    sendColor(ExtraTestActivity.WINTER);
                }

                if (checkedId == 200) {
                    sendColor(ExtraTestActivity.SUMMER);
                }

            }
        });

        return uiRoot;
    }

    private void setAnswers(ArrayList<AnswerColor> answers) {
        rgCold.removeAllViews();

        for (int i = 0; i < answers.size(); i++) {
            int id = (i + 1) * 100;
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(id);
            radioButton.setTag(answers.get(i));
            switch (getString(R.string.app_language)) {
                case "spanish":
                    radioButton.setText(answers.get(i).getSpanish().getAnswer());
                    break;
                case "english":
                    radioButton.setText(answers.get(i).getEnglish().getAnswer());
                    break;
                default:
                    radioButton.setText(answers.get(i).getSpanish().getAnswer());
                    break;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList colorStateList = new ColorStateList(
                        new int[][] {
                                new int[]{-android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_enabled}
                        },
                        new int[] {
                                Color.DKGRAY,
                                ContextCompat.getColor(getContext(), R.color.colorAccent)
                        });
                radioButton.setButtonTintList(colorStateList);
                radioButton.invalidate();
                radioButton.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_deep));

                rgCold.addView(radioButton);
            }
        }
    }

    public void sendColor(String color) {
        mCallbackFrio.update(color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbackFrio = (ExtraCalidoFragment.OnCallbackReceived) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
