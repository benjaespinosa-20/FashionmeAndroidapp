package mx.app.fashionme.view;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.app.fashionme.R;
import mx.app.fashionme.fragment.ExtraCalidoFragment;
import mx.app.fashionme.interactor.ExtraTestInteractor;
import mx.app.fashionme.pojo.AnswerColor;
import mx.app.fashionme.presenter.ExtraTestPresenter;
import mx.app.fashionme.presenter.interfaces.IExtraTestPresenter;
import mx.app.fashionme.view.interfaces.IExtraTestView;

public class ExtraTestActivity extends AppCompatActivity implements IExtraTestView, ExtraCalidoFragment.OnCallbackReceived {

    private static final String TAG = ExtraTestActivity.class.getSimpleName();

    @BindView(R.id.text_view_alt_question)
    TextView textViewAltQuestion;
    @BindView(R.id.radio_group_alt_answers)
    RadioGroup radioGroupAltAnswers;
    @BindView(R.id.baseLayoutExtraTest)
    FrameLayout baseLayoutExtraTest;
    @BindView(R.id.btnSendExtraColor)
    Button btnSendExtraColor;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;

    private ProgressDialog progressDialog;
    private IExtraTestPresenter presenter;
    private String color;

    public static final String SPRING   = "spring";
    public static final String SUMMER   = "summer";
    public static final String FALL     = "fall";
    public static final String WINTER   = "winter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_test);
        ButterKnife.bind(this);

        presenter = new ExtraTestPresenter(this, new ExtraTestInteractor(), getApplicationContext(), this);
        setupDialog();

        presenter.loadData();

        setListenerRG();
    }

    @Override
    public void setListenerRG() {
        radioGroupAltAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, checkedId +" ");

                if (checkedId == 100) {
                    getFragmentTest("calido");
                } else if (checkedId == 200) {
                    getFragmentTest("frio");
                }
            }
        });
    }

    @Override
    public void setFragment(Fragment fragment, String tag) {

        if (btnSendExtraColor.getVisibility() != View.VISIBLE) {
          btnSendExtraColor.setVisibility(View.VISIBLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.baseLayoutExtraTest, fragment, tag)
                .commit();

        this.color = null;
    }

    @Override
    public void getFragmentTest(String color) {
        presenter.getFragment(color);
    }

    @Override
    public void showError(String err) {
          Snackbar.make(rootView, err, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSendingResult(boolean show) {
        if (show){
            progressDialog.setMessage("Enviando...");
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setQuestion(String title) {
        textViewAltQuestion.setText(title);
    }

    @Override
    public void setAnswers(ArrayList<AnswerColor> data) {

        radioGroupAltAnswers.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            int id = (i + 1) * 100;
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(id);
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
                                ContextCompat.getColor(this, R.color.colorAccent)
                        });
                radioButton.setButtonTintList(colorStateList);
                radioButton.invalidate();
                radioButton.setTextColor(ContextCompat.getColor(this, R.color.gray_deep));

                radioGroupAltAnswers.addView(radioButton);
            }
        }
    }

    @Override
    public void update(String color) {
        this.color = color;
    }

    @OnClick(R.id.btnSendExtraColor)
    public void onViewClicked() {
        presenter.sendResultExtra(this.color);
    }

    private void setupDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
}
