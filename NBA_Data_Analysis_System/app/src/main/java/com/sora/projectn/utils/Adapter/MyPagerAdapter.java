package com.sora.projectn.utils.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sora on 2016/1/28.
 *
 * ViewPager适配器
 */
public class MyPagerAdapter extends PagerAdapter{

    public List<View> mListViews;

    /**
     * 构造函数
     *
     * @param mListViews
     */
    public MyPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position));

        //将对应的View作为Key返回
        return mListViews.get(position);
    }
}
