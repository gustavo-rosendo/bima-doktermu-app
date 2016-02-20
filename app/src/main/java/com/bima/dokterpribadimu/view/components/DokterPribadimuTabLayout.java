package com.bima.dokterpribadimu.view.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bima.dokterpribadimu.R;

/**
 * Created by apradanas on 2/20/16.
 */
public class DokterPribadimuTabLayout extends TabLayout {

    public DokterPribadimuTabLayout(Context context) {
        super(context);
    }

    public DokterPribadimuTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DokterPribadimuTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
        this.removeAllTabs();

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            Tab tab = this.newTab();
            TextView tabView = (TextView) View.inflate(getContext(), R.layout.sign_in_tab_view, null);
            tabView.setText(adapter.getPageTitle(i));
            tab.setCustomView(tabView);
            this.addTab(tab);
        }
    }
}
