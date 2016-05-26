package com.sora.projectn.model.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Fragment.PlayerInfoDetailFragment;
import com.sora.projectn.model.Fragment.PlayerSeasonAdvancedStatisticsFragment;
import com.sora.projectn.model.Fragment.PlayerSeasonStatisticsFragment;
import com.sora.projectn.model.Fragment.ScoutSearchPlayerByCustomParameterFragment;
import com.sora.projectn.model.Fragment.ScoutSearchPlayerFragment;
import com.sora.projectn.utils.Adapter.FragAdapter;
import com.sora.projectn.utils.Consts;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoutActivity extends FragmentActivity {

    private Context mContext;


    //Fragment
    private ScoutSearchPlayerFragment scoutSearchPlayerFragment;
    private ScoutSearchPlayerByCustomParameterFragment scoutSearchPlayerByCustomParameterFragment;

    //ViewPager
    private ViewPager viewPager;

    private Toolbar toolbar;

    //TextView
    private TextView tv_scoutGuide1;
    private TextView tv_scoutGuide2;
    private TextView tv_season_scout;

    //ImageView
    private ImageView scoutCursor;

    private List<Fragment> fragments;

    private FragAdapter adapter;

    //游标宽度
    private int bmpw = 0;
    //动画图片偏移量 滑块占据一个标签栏 offset设置为0
    private int offset = 0;
    //当前页卡编号  初始编号为0
    private int currIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        initView();

        initViewPager();
        initListener();
        setTopView();

    }




    //设置顶层视图
    private void setTopView() {
        tv_season_scout.setText("2015-2016赛季");
    }


    /**
     * 初始化View
     */
    private void initView() {

        mContext = this;


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("球探功能");
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
        tv_scoutGuide1 = (TextView) findViewById(R.id.tv_scoutGuide1);
        tv_scoutGuide2 = (TextView) findViewById(R.id.tv_scoutGuide2);
        tv_season_scout = (TextView) findViewById(R.id.tv_season_scout);


        //ImageView
        scoutCursor = (ImageView) findViewById(R.id.iv_scoutCursor);


        //Fragment
        scoutSearchPlayerFragment = new ScoutSearchPlayerFragment();
        scoutSearchPlayerByCustomParameterFragment = new ScoutSearchPlayerByCustomParameterFragment();

    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(scoutSearchPlayerFragment);
        fragments.add(scoutSearchPlayerByCustomParameterFragment);

        //新建适配器对象
        adapter = new FragAdapter(getSupportFragmentManager(),fragments);


        //设定适配器
        viewPager.setAdapter(adapter);

        //初始化指示器位置
        initCursorPos();

        //初始化ViewPager在第一个模块
        viewPager.setCurrentItem(0);
        //对应的标签栏高亮显示
        tv_scoutGuide1.setAlpha(1);

    }


    private void initCursorPos() {
        //初始化游标宽度为二分之一屏幕宽度
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        bmpw = width/2;

        ViewGroup.LayoutParams params = scoutCursor.getLayoutParams();

        //动态设置cursor的宽度
        params.width = bmpw;

    }


    /**
     * 设置监听
     */
    private void initListener() {
        //标签栏的监听
        tv_scoutGuide1.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_scoutGuide2.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });


        //ViewPager的监听  解决用户通过手势滑动获取新的模块 此时标签栏没有同步改变的问题
        //同时完成滑块滑动的动画
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());


    }




    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //跳转到新的fragment
            switch (v.getId()){
                case R.id.tv_scoutGuide1:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tv_scoutGuide2:
                    viewPager.setCurrentItem(1);
                    break;

            }

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

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
            scoutCursor.startAnimation(animation);

            //取消之前选中的标签的高亮显示
            switch (currIndex){
                case 0:
                    tv_scoutGuide1.setAlpha((float) 0.5);
                    break;
                case 1:
                    tv_scoutGuide2.setAlpha((float) 0.5);
                    break;

            }
            //将即将切换到的标签的高亮显示
            switch (position){
                case 0:
                    tv_scoutGuide1.setAlpha(1);
                    break;
                case 1:
                    tv_scoutGuide2.setAlpha(1);
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
