package com.sora.projectn.model.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.model.Fragment.TeamListFragment;
import com.sora.projectn.utils.Adapter.FragAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016-04-25.
 */
public class TeamListActivity extends FragmentActivity {


    protected int isTeamFavActivity = 0;

    protected Toolbar toolbar;

    protected Context mContext;

    //Fragment
    protected TeamListFragment westFragment;
    protected TeamListFragment eastFragment;

    //ViewPager
    protected ViewPager viewPager;


    //TextView
    protected TextView tv_westConference;
    protected TextView tv_eastConference;

    //ImageView
    protected ImageView cursor;

    protected List<Fragment> fragments;

    protected FragAdapter adapter;

    //游标宽度
    protected int bmpw = 0;
    //动画图片偏移量 滑块占据一个标签栏 offset设置为0
    protected int offset = 0;
    //当前页卡编号  初始编号为0
    protected int currIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamlist);

        initView();

        parseIntent();

        initViewPager();
        initListener();


    }


    /**
     * 初始化View
     */
    protected void initView() {

        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("球队列表");
        //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TextView
        tv_westConference = (TextView) findViewById(R.id.tv_westConference);
        tv_eastConference = (TextView) findViewById(R.id.tv_eastConference);


        //ImageView
        cursor = (ImageView) findViewById(R.id.iv_teamListCursor);



        //Fragment
        westFragment = new TeamListFragment();
        eastFragment = new TeamListFragment();




    }


    /**
     * 获取Intent传递来的abbr值
     */
    protected void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            isTeamFavActivity = bundle.getInt("TeamFavActivity");
        }


        int west = 0;
        int east = 1;

        //需要统一Fragment实例化对象 采用全局变量定义
        Bundle bundle1 = new Bundle();
        bundle1.putInt("league", west);
        bundle1.putInt("TeamFavActivity", isTeamFavActivity);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("league", east);
        bundle2.putInt("TeamFavActivity", isTeamFavActivity);

        westFragment.setArguments(bundle1);

        eastFragment.setArguments(bundle2);


    }

    /**
     * 初始化ViewPager
     */
    protected void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(westFragment);
        fragments.add(eastFragment);


        //新建适配器对象
        adapter = new FragAdapter(getSupportFragmentManager(),fragments);


        //设定适配器
        viewPager.setAdapter(adapter);

        //初始化指示器位置
        initCursorPos();

        //初始化ViewPager在第一个模块
        viewPager.setCurrentItem(0);
        //对应的标签栏高亮显示
        tv_westConference.setAlpha(1);

    }


    protected void initCursorPos() {
        //初始化游标宽度为二分之一屏幕宽度
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        bmpw = width/2;

        ViewGroup.LayoutParams params = cursor.getLayoutParams();

        //动态设置cursor的宽度
        params.width = bmpw;

    }


    /**
     * 设置监听
     */
    protected void initListener() {
        //标签栏的监听
        tv_westConference.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_eastConference.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });


        //ViewPager的监听  解决用户通过手势滑动获取新的模块 此时标签栏没有同步改变的问题
        //同时完成滑块滑动的动画
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());


    }


    protected class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //跳转到新的fragment
            switch (v.getId()){
                case R.id.tv_westConference:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tv_eastConference:
                    viewPager.setCurrentItem(1);
                    break;
            }

        }
    }

    protected class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //相邻页面的偏移量
        private int one = bmpw;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //即将显示的页卡的index
        @Override
        public void onPageSelected(int position) {
            //初始化移动的动画 (从当前位置 平移到即将摇到的位置)
            Animation animation = new TranslateAnimation(currIndex * one , position * one , 0 , 0);
            //动画终止时停留在最后一帧  不然会回到没有执行前的状态
            animation.setFillAfter(true);
            //动画持续时间0.2秒
            animation.setDuration(200);
            //用游标来显示动画
            cursor.startAnimation(animation);

            //取消之前选中的标签的高亮显示
            switch (currIndex){
                case 0:
                    tv_westConference.setAlpha((float) 0.5);
                    break;
                case 1:
                    tv_eastConference.setAlpha((float) 0.5);
                    break;
            }
            //将即将切换到的标签的高亮显示
            switch (position){
                case 0:
                    tv_westConference.setAlpha(1);
                    break;
                case 1:
                    tv_eastConference.setAlpha(1);
                    break;
            }

            //当前页卡发生改变 重定义currIndex
            currIndex = position;
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
