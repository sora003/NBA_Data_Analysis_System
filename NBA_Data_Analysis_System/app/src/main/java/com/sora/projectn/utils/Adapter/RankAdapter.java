package com.sora.projectn.utils.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by qhy on 2016/4/18.
 */
public class RankAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    ArrayList<String> titlelist;
    public RankAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titlelist) {
        super(fm);
        this.list = list;
        this.titlelist = titlelist;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }


}