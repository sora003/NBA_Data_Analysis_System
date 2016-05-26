package com.sora.projectn.utils.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sora on 2016/1/28.
 *
 * FragmentPager适配器
 */
public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public FragAdapter(FragmentManager fm , List<Fragment> fragments) {
        super(fm);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
