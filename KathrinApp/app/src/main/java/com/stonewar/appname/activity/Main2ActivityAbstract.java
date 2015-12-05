package com.stonewar.appname.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.adapter.ViewPagerAdapter;
import com.stonewar.appname.googlecode.SlidingTabLayout;
import com.stonewar.appname.util.Factory;

public class Main2ActivityAbstract extends AbstractBaseActivity {

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    CharSequence[] titles = {"Artists", "Titles", "Artists", "Titles", "Artists"};
    private static final int NUMBER_OF_TABS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AbstractViewPagerFragment[] viewPagerFragments = new AbstractViewPagerFragment[3];
        for(int i = 0; i < viewPagerFragments.length; i++)
            viewPagerFragments[i] = Factory.createViewPagerFragment(i + 1);

        for(int i = 0; i < viewPagerFragments.length; i++)
            Log.d("TaTTTTg", ""+viewPagerFragments[i].getTitle());

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),viewPagerFragments);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public int contentView() {
        return R.layout.activity_main2;
    }

}
