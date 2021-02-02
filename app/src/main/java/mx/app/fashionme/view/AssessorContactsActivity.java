package mx.app.fashionme.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.ChatPageAdapter;
import mx.app.fashionme.fragment.ChatsFragment;
import mx.app.fashionme.fragment.ContactsFragment;
import mx.app.fashionme.utils.Utils;

public class AssessorContactsActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.app_bar_assessor_contacts)
    AppBarLayout appBarAssessorContacts;

    private static final String TAG = ">>FirebaseAssessorAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessor_contacts);

        ButterKnife.bind(this);

        // Setup viewPager
        viewPager.setAdapter(new ChatPageAdapter(getSupportFragmentManager(), addFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Chats");
        tabLayout.getTabAt(1).setText("Solicitudes");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        if (appBarAssessorContacts != null) {
            appBarAssessorContacts.setBackgroundColor(Utils.getTabColor(this));
        }

        if (getIntent().getStringExtra("notification") != null) {
            viewPager.setCurrentItem(1);
        }

        updateColors();
    }

    private ArrayList<Fragment> addFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ContactsFragment());
        fragments.add(new ChatsFragment());

        return fragments;
    }

    public void setupViewPager(int pageNumber) {
        viewPager.setCurrentItem(pageNumber);
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
