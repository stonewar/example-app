package com.stonewar.appname.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.util.Factory;

import java.util.List;

/**
 * Created by yandypiedra on 04.12.15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private AbstractViewPagerFragment[] viewPagerFragmentList;

    public ViewPagerAdapter(FragmentManager fm, AbstractViewPagerFragment[] viewPagerFragmentList) {
        super(fm);
        this.viewPagerFragmentList = viewPagerFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return viewPagerFragmentList[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return viewPagerFragmentList[position].getTitle();
    }

    @Override
    public int getCount() {
        return viewPagerFragmentList.length;
    }
}
