package mx.app.fashionme.view;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.fragment.RegisterFragment;
import mx.app.fashionme.adapter.PageAdapter;
import mx.app.fashionme.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout   = findViewById(R.id.tabLayout);
        viewPager   = findViewById(R.id.viewPager);

        // Setup viewPager
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), addFragments()));
        tabLayout.setupWithViewPager(viewPager);

        Bundle params = getIntent().getExtras();
        int currentItem = params.getInt(StartActivity.TAB_ACTION, 0);

        viewPager.setCurrentItem(currentItem);

        // Icons
        tabLayout.getTabAt(0).setText(getResources().getString(R.string.title_activity_login));
        tabLayout.getTabAt(1).setText(getResources().getString(R.string.title_activity_register));

    }

    private ArrayList<Fragment> addFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LoginFragment());
        fragments.add(new RegisterFragment());

        return fragments;
    }

}
