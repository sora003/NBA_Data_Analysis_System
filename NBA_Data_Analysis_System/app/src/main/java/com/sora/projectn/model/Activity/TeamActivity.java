package com.sora.projectn.model.Activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Fragment.MatchListFragment;
import com.sora.projectn.model.Fragment.TeamDataFragment;
import com.sora.projectn.model.Fragment.TeamPlayerFragment;
import com.sora.projectn.model.Fragment.TeamStaticsFragment;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.BitmapHelper;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.Adapter.FragAdapter;
import com.sora.projectn.utils.GetHttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sora on 2016/1/26.
 */
public class TeamActivity extends FragmentActivity implements MatchListFragment.OnFragmentInteractionListener{


    private Context mContext;
    /**
     * 球队编号
     */
    private int id;
    private Map<String,String> infoMap = new HashMap<>();

    //Fragment
    private TeamDataFragment teamDataFragment;
    private TeamStaticsFragment teamStaticsFragment;
    private TeamPlayerFragment teamPlayerFragment;
    private MatchListFragment teamScheduleFragment;

    //ViewPager
    private ViewPager viewPager;

    private Toolbar toolbar;

    //TextView
    private TextView tv_teamGuide1;
    private TextView tv_teamGuide2;
    private TextView tv_teamGuide3;
    private TextView tv_teamGuide4;
    private TextView tv_teamName;
    private TextView tv_season;
    private TextView tv_winlose;
    private TextView tv_rank;

    //ImageView
    private ImageView cursor;
    private ImageView iv_teamLogo;

    private List<Fragment> fragments;

    private FragAdapter adapter;

    //游标宽度
    private int bmpw = 0;
    //动画图片偏移量 滑块占据一个标签栏 offset设置为0
    private int offset = 0;
    //当前页卡编号  初始编号为0
    private int currIndex = 0;
    private Map<String, String> teamInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        initView();

        parseIntent();

        initViewPager();
        initListener();

        //获取球队基本信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                infoMap = getTeamInfo();
                if (infoMap == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Consts.SET_VIEW:
                    setTopView();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(mContext,Consts.ToastMessage01,Toast.LENGTH_SHORT).show();
            }
        }
    };

    //设置顶层视图
    private void setTopView() {
        iv_teamLogo.setImageBitmap(BitmapHelper.loadBitMap(mContext, BitmapHelper.getBitmap(infoMap.get("name"))));
        tv_teamName.setText(infoMap.get("city")+infoMap.get("name"));
        tv_season.setText(infoMap.get("season") + "赛季");
        tv_winlose.setText(infoMap.get("wins") + " 胜 " + infoMap.get("loses") + " 负");
        tv_rank.setText("排名 : "+infoMap.get("rank"));
    }


    /**
     * 获取Intent传递来的abbr值
     */
    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");

        //需要统一Fragment实例化对象 采用全局变量定义
        bundle = new Bundle();
        bundle.putInt("id", id);

        teamDataFragment.setArguments(bundle);

        teamStaticsFragment.setArguments(bundle);

        teamPlayerFragment.setArguments(bundle);

        teamScheduleFragment.setArguments(bundle);

    }

    /**
     * 初始化View
     */
    private void initView() {

        mContext = this;


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("球队详情");
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
        tv_teamGuide1 = (TextView) findViewById(R.id.tv_teamGuide1);
        tv_teamGuide2 = (TextView) findViewById(R.id.tv_teamGuide2);
        tv_teamGuide3 = (TextView) findViewById(R.id.tv_teamGuide3);
        tv_teamGuide4 = (TextView) findViewById(R.id.tv_teamGuide4);
        tv_teamName = (TextView) findViewById(R.id.tv_teamName);
        tv_season = (TextView) findViewById(R.id.tv_season);
        tv_winlose = (TextView) findViewById(R.id.tv_winlose);
        tv_rank = (TextView) findViewById(R.id.tv_rank);


        //ImageView
        cursor = (ImageView) findViewById(R.id.iv_teamCursor);
        iv_teamLogo = (ImageView) findViewById(R.id.iv_teamLogo);


        //Fragment
        teamDataFragment = new TeamDataFragment();
        teamStaticsFragment = new TeamStaticsFragment();
        teamPlayerFragment = new TeamPlayerFragment();
        teamScheduleFragment = new MatchListFragment();

    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(teamDataFragment);
        fragments.add(teamStaticsFragment);
        fragments.add(teamPlayerFragment);
        fragments.add(teamScheduleFragment);

        //新建适配器对象
        adapter = new FragAdapter(getSupportFragmentManager(),fragments);


        //设定适配器
        viewPager.setAdapter(adapter);

        //初始化指示器位置
        initCursorPos();

        //初始化ViewPager在第一个模块
        viewPager.setCurrentItem(0);
        //对应的标签栏高亮显示
        tv_teamGuide1.setAlpha(1);

    }


    private void initCursorPos() {
        //初始化游标宽度为四分之一屏幕宽度
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        bmpw = width/4;

        ViewGroup.LayoutParams params = cursor.getLayoutParams();

        //动态设置cursor的宽度
        params.width = bmpw;

    }


    /**
     * 设置监听
     */
    private void initListener() {
        //标签栏的监听
        tv_teamGuide1.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide2.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide3.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide4.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });

        //ViewPager的监听  解决用户通过手势滑动获取新的模块 此时标签栏没有同步改变的问题
        //同时完成滑块滑动的动画
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());


    }

    public Map<String, String> getTeamInfo() {
        Map<String ,String> map = new HashMap<>();

        String jsonString;

        /**
         * getTeamInfos
         */
        jsonString = ACache.get(mContext).getAsString("getTeamInfos - " + id);

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.getTeamInfos + "?teamId=" + id);

            if (jsonString == null){
                return null;
            }

            ACache.get(mContext).put("getTeamInfos - " + id ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONObject obj = new JSONObject(jsonString);

            map.put("name",obj.getString("name"));
            map.put("city",obj.getString("city"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        /**
         * getTeamSeasonStatistics
         */
        jsonString = ACache.get(mContext).getAsString("getTeamSeasonStatistics - " + id);

        if (jsonString == null){
            //从server获取数据

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamSeasonStatistics + "?teamId=" + id);

            ACache.get(mContext).put("getTeamSeasonStatistics - " + id ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONObject obj = new JSONObject(jsonString);

            map.put("season", obj.getString("season"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * getTeamSeasonRanks
         */
        jsonString = ACache.get(mContext).getAsString("getTeamSeasonRanks - " + (id));

        if (jsonString == null){
            //从server获取数据

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamSeasonRanks + "?teamId=" + (id-1));

            ACache.get(mContext).put("getTeamSeasonRanks - " + (id) ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", "server");
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONArray array = new JSONArray(jsonString);

            JSONObject obj = array.getJSONObject(id - 1);

            map.put("rank", String.valueOf(obj.getInt("rank")));

            map.put("wins", String.valueOf(obj.getInt("wins")));

            map.put("loses", String.valueOf(obj.getInt("loses")));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //跳转到新的fragment
            switch (v.getId()){
                case R.id.tv_teamGuide1:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tv_teamGuide2:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.tv_teamGuide3:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.tv_teamGuide4:
                    viewPager.setCurrentItem(3);
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
            cursor.startAnimation(animation);

            //取消之前选中的标签的高亮显示
            switch (currIndex){
                case 0:
                    tv_teamGuide1.setAlpha((float) 0.5);
                    break;
                case 1:
                    tv_teamGuide2.setAlpha((float) 0.5);
                    break;
                case 2:
                    tv_teamGuide3.setAlpha((float) 0.5);
                    break;
                case 3:
                    tv_teamGuide4.setAlpha((float) 0.5);
                    break;
            }
            //将即将切换到的标签的高亮显示
            switch (position){
                case 0:
                    tv_teamGuide1.setAlpha(1);
                    break;
                case 1:
                    tv_teamGuide2.setAlpha(1);
                    break;
                case 2:
                    tv_teamGuide3.setAlpha(1);
                    break;
                case 3:
                    tv_teamGuide4.setAlpha(1);
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
