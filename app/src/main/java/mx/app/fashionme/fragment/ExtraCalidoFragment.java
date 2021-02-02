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


public class ExtraCalidoFragment extends Fragment {

    @BindView(R.id.text_view_alt_question_hot)
    TextView textViewAltQuestionHot;
    @BindView(R.id.rg_hot)
    RadioGroup rgHot;
    Unbinder unbinder;
    private OnCallbackReceived mCallback;
    private List<QuestionColor> questions;

    public ExtraCalidoFragment() {
        // Required empty public constructor
    }

    public static ExtraCalidoFragment newInstance() {
        return new ExtraCalidoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        questions = new ArrayList<>();
        if (bundle != null) {
            questions = (List<QuestionColor>) bundle.getSerializable("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View uiRoot = inflater.inflate(R.layout.fragment_extra_calido, container, false);
        unbinder = ButterKnife.bind(this, uiRoot);

        for (QuestionColor question:questions) {
            if (question.getType().equals("green_eyes")) {
                switch (getContext().getString(R.string.app_language)) {
                    case "spanish":
                        textViewAltQuestionHot.setText(question.getSpanish().getQuestion());
                        setAnswers(question.getAnswers().getData());
                        break;
                    case "english":
                        textViewAltQuestionHot.setText(question.getEnglish().getQuestion());
                        setAnswers(question.getAnswers().getData());
                        break;
                    default:
                        textViewAltQuestionHot.setText(question.getSpanish().getQuestion());
                        setAnswers(question.getAnswers().getData());
                        break;
                }
            }
        }

         rgHot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == 100) {
                    sendColor(ExtraTestActivity.FALL);
                }

                if (checkedId == 200) {
                    sendColor(ExtraTestActivity.SPRING);
                }
            }
        });

        return uiRoot;
    }

    private void setAnswers(ArrayList<AnswerColor> data) {
        rgHot.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            int id = (i + 1) * 100;
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(id);
            radioButton.setTag(data.get(i));
            switch (getString(R.string.app_language)) {
                case "spanish":
                    radioButton.setText(data.get(i).getSpanish().getAnswer());
                    break;
                case "english":
                    radioButton.setText(data.get(i).getEnglish().getAnswer());
                    break;
                default:
                    radioButton.setText(data.get(i).getSpanish().getAnswer());
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

                rgHot.addView(radioButton);
            }
        }
    }

    public void sendColor(String color) {
        mCallback.update(color);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnCallbackReceived {
        void update(String color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnCallbackReceived) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
