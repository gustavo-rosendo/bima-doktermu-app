package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityNewsBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.NewsFragment;

public class NewsActivity extends BaseActivity {

    private ActivityNewsBinding binding;
    private DrawerFragment drawerFragment;

    private static final int[] TITLE_STRING_IDS = {
            R.string.news_diseases,
            R.string.news_healthy_living,
            R.string.news_tips
    };

    private static final String[] NEWS_CATEGORIES = {
            "penyakit", "kesehatan", "tips"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initViews();
        setupDrawerFragment();
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return NewsFragment.newInstance(NEWS_CATEGORIES[position]);
            }

            @Override
            public int getCount() {
                return TITLE_STRING_IDS.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(TITLE_STRING_IDS[position]);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        binding.newsViewpager.setAdapter(adapter);
        binding.newsViewpagerTab.setupWithViewPager(binding.newsViewpager);
    }

    private void setupDrawerFragment() {
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_NEWS);
        drawerFragment.setOnDrawerItemPressedListener(new DrawerFragment.OnDrawerItemPressed() {
            @Override
            public void onDrawerItemPressed(int selectedDrawerType) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_drawer, drawerFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        drawerFragment.setOnDrawerItemPressedListener(null);

        super.onDestroy();
    }
}
