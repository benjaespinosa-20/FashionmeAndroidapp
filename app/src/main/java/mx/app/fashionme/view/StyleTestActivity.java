package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.PageAdapter;
import mx.app.fashionme.interactor.StyleTestInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.presenter.StyleTestPresenter;
import mx.app.fashionme.presenter.interfaces.IStyleTestPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IStyleTestView;

public class StyleTestActivity extends AppCompatActivity implements IStyleTestView {

    public static final String TAG = StyleTestActivity.class.getSimpleName();

    private ViewPager viewPager;
    private Button btnSend;
    private ImageView imgNext, imgPrevious;

    private RelativeLayout rlMainStyleTest;
    private LinearLayout llProgressStyleTest;
    private CoordinatorLayout rootView;
    private Toolbar toolbar;

    private AnswerStyle answerStyle;

    private IStyleTestPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_test);

        viewPager = findViewById(R.id.viewPagerStyleTest);

        imgNext = findViewById(R.id.img_next);
        imgPrevious = findViewById(R.id.img_previous);

        btnSend = findViewById(R.id.btnSendTestStyle);

        rlMainStyleTest = findViewById(R.id.rlMainStyleTest);
        llProgressStyleTest = findViewById(R.id.llTestStyleProgress);
        rootView = findViewById(R.id.rootViewStyleTest);

        toolbar = findViewById(R.id.actionBarStyleTest);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        answerStyle = new AnswerStyle(0, 0,0,0,0,0,0);

        setUpPresenterStyleTest();
        getQuestions();
        updateColors();

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(+1), true);
                if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1)){
                    btnSend.setVisibility(View.VISIBLE);
                    imgNext.setVisibility(View.INVISIBLE);
                }

                if (viewPager.getCurrentItem() > 0){
                    imgPrevious.setVisibility(View.VISIBLE);
                }
            }
        });

        imgNext.setBackgroundColor(Utils.getTabColor(this));

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(-1), true);

                if (viewPager.getCurrentItem() == 0){
                    imgPrevious.setVisibility(View.INVISIBLE);
                }

                if (viewPager.getCurrentItem() < (viewPager.getAdapter().getCount() - 1)){
                    btnSend.setVisibility(View.INVISIBLE);
                    imgNext.setVisibility(View.VISIBLE);
                }

            }
        });

        imgPrevious.setBackgroundColor(Utils.getTabColor(this));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1)){
                    btnSend.setVisibility(View.VISIBLE);
                    imgNext.setVisibility(View.INVISIBLE);
                }

                if (viewPager.getCurrentItem() > 0){
                    imgPrevious.setVisibility(View.VISIBLE);
                }

                if (viewPager.getCurrentItem() == 0){
                    imgPrevious.setVisibility(View.INVISIBLE);
                }

                if (viewPager.getCurrentItem() < (viewPager.getAdapter().getCount() - 1)){
                    btnSend.setVisibility(View.INVISIBLE);
                    imgNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTestStyle();
            }
        });

        presenter.setAnalytics();
    }

    @Override
    public void getQuestions() {
        presenter.getQuestions();
    }

    @Override
    public void setAdapterViewPager(ArrayList<Fragment> fragments) {
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    public void showErrors(String error) {
        Snackbar.make(rootView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.getQuestions();
                    }
                })
                .show();
    }

    @Override
    public void showLoading(boolean show) {
        llProgressStyleTest.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        rlMainStyleTest.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showPrevious(boolean show) {
        imgPrevious.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showNext(boolean show) {
        imgNext.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showSendButton(boolean show) {
        btnSend.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void sendTestStyle() {
        presenter.sendAnswers(answerStyle, viewPager);
    }

    @Override
    public void goToHomePage(String style) {
        startActivity(new Intent(this, StyleResultActivity.class));
        //Toast.makeText(this, "Tu estilo es: " + style, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void setUpPresenterStyleTest() {
        presenter = new StyleTestPresenter(this, new StyleTestInteractor(), this, answerStyle);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public void updateColors() {

        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(this));
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }

}
