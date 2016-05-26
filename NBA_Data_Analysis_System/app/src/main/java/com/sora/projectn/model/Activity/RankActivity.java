package com.sora.projectn.model.Activity;

import android.content.Context;
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
import com.sora.projectn.model.Fragment.DayRankFragment;
import com.sora.projectn.model.Fragment.PlayerRankFragment;
import com.sora.projectn.model.Fragment.TeamRankFragment;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Adapter.FragAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.beans.DayRankInfo;
import com.sora.projectn.utils.beans.PlayerRankInfo;
import com.sora.projectn.utils.beans.TeamRankInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by qhy on 2016/4/18.
 * RankActivity
 */
public class RankActivity extends FragmentActivity {

    private Toolbar toolbar;
    private Context mContext;
    private ViewPager pager;

    private TeamRankFragment teamRankFragment;
    private PlayerRankFragment playerRankFragment;
    private DayRankFragment dayRankFragment;

    private TextView tv_rankGuide1,tv_rankGuide2,tv_rankGuide3,tv_rankGuide4;

    private ImageView cursor;

    private List<Fragment> fragments ;

    private FragAdapter adapter;

    //游标宽度
    private int bmpw = 0;
    private int offset = 0;
    private int currIndex = 0;
    private String currentDate = "";

    List<TeamRankInfo> eastRanks = new ArrayList<>(), westRanks = new ArrayList<>();
    List<PlayerRankInfo> playerranks = new ArrayList<>();
    List<DayRankInfo> dayranks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                playerranks =getPlayerRanks();
                dayranks = getDayRanks();
                List<TeamRankInfo> ranks = getTeamRanks();
                initData(ranks);
                handler.sendEmptyMessage(Consts.SET_VIEW);
            }
        }).start();

        initListener();

    }

    private void initView(){
        setContentView(R.layout.activity_rank);
        mContext = this;

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("排行榜");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_rankGuide2 = (TextView)findViewById(R.id.tv_rankGuide2);
        tv_rankGuide3 = (TextView)findViewById(R.id.tv_rankGuide3);
        tv_rankGuide4 = (TextView)findViewById(R.id.tv_rankGuide4);

        cursor = (ImageView)findViewById(R.id.iv_rankCursor);

        pager = (ViewPager)findViewById(R.id.viewpager);

    }

    private void initData(List<TeamRankInfo> ranks){
        for(TeamRankInfo team:ranks){
            if(team.getLeague().equals("0")){
                eastRanks.add(team);
            }
            else if(team.getLeague().equals("1")){
                westRanks.add(team);
            }
        }
        Collections.sort(eastRanks, new Comparator<TeamRankInfo>() {
            @Override
            public int compare(TeamRankInfo lhs, TeamRankInfo rhs) {
                if (Integer.parseInt(lhs.getRank()) < Integer.parseInt(rhs.getRank())) {
                    return -1;
                } else return 1;
            }
        });
        Collections.sort(westRanks, new Comparator<TeamRankInfo>() {
            @Override
            public int compare(TeamRankInfo lhs, TeamRankInfo rhs) {
                if (Integer.parseInt(lhs.getRank()) < Integer.parseInt(rhs.getRank())) {
                    return -1;
                } else return 1;
            }
        });
    }

    private void initViewPager(){
        fragments = new ArrayList<>();
        teamRankFragment = new TeamRankFragment(eastRanks,westRanks);
        playerRankFragment = new PlayerRankFragment(playerranks);
        dayRankFragment = new DayRankFragment(dayranks,currentDate);
        fragments.add(teamRankFragment);
        fragments.add(playerRankFragment);
        fragments.add(dayRankFragment);

        adapter = new FragAdapter(getSupportFragmentManager(),fragments);

        initCursorPos();
        pager.setAdapter(adapter);


        pager.setCurrentItem(0);
        tv_rankGuide2.setAlpha(1);

    }

    private void initCursorPos() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        bmpw = width/3;

        ViewGroup.LayoutParams params = cursor.getLayoutParams();

        params.width = bmpw;

    }



    private List<TeamRankInfo> getTeamRanks(){

        String jsonString;

        jsonString = ACache.get(mContext).getAsString("getTeamSeasonRanks" );

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.teamrank);

            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(mContext).put("getTeamSeasonRanks" ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        List<TeamRankInfo> ranks = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                TeamRankInfo temp = new TeamRankInfo();
                JSONObject obj = array.getJSONObject(i);
                String teamID = obj.getString("teamId");
                String rank = obj.getString("rank");
                String name = obj.getString("name");
                String league = obj.getString("league");
                String wins = obj.getString("wins");
                String loses = obj.getString("loses");
                String winRate = obj.getString("winRate");
                String gamesBehind = obj.getString("gamesBehind");
                String pspg = obj.getString("pspg");
                String papg = obj.getString("papg");

                temp.setTeamId(teamID);
                temp.setRank(rank);
                temp.setName(name);
                temp.setLeague(league);
                temp.setWins(wins);
                temp.setLoses(loses);
                temp.setWinRate(winRate);
                temp.setGamesBehind(gamesBehind);
                temp.setPspg(pspg);
                temp.setPapg(papg);
                ranks.add(temp);
            }



        } catch(NullPointerException e2){
            e2.printStackTrace();
        }
        catch (JSONException e2) {
            e2.printStackTrace();
        }
        finally{
            return ranks;
        }
    }

    public List<PlayerRankInfo> getPlayerRanks(){

        String jsonString;

        jsonString = ACache.get(mContext).getAsString("getPlayerRanks" );

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.playerrank);

            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(mContext).put("getPlayerRanks" ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        List<PlayerRankInfo> playerRanks = new ArrayList<>();
        try{
            JSONArray array2 = new JSONArray(jsonString);
            for (int i = 0; i < array2.length(); i++) {
                PlayerRankInfo temp = new PlayerRankInfo();
                JSONObject obj = array2.getJSONObject(i);
                String name = obj.getString("name");
                String id = obj.getString("id");
                String teamName = obj.getString("teamName");
                String data = obj.getString("data");
                String seasonData = obj.getString("seasonData");
                String type = obj.getString("type");


                temp.setName(name);
                temp.setId(id);
                temp.setTeamName(teamName);
                temp.setData(data);
                temp.setSeasonData(seasonData);
                temp.setType(type);
                playerRanks.add(temp);
            }

        }catch(NullPointerException e2){
            handler.sendEmptyMessage(Consts.SET_VIEW);
            e2.printStackTrace();
        }
        catch (JSONException e2) {
            e2.printStackTrace();
        }
        finally{
            return playerRanks;
        }
    }

    public List<DayRankInfo> getDayRanks(){

        String jsonString;

        jsonString = ACache.get(mContext).getAsString("getDayRanks" );

        if (jsonString == null){

            String jsonString2 = ACache.get(mContext).getAsString("LatestMatchDate");
            if(jsonString2 == null){
                jsonString2 = GetHttpResponse.getHttpResponse(Consts.getLatestMatchList);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString2);
                    Map<String,String> temp = new TreeMap<>();
                    for(int i = 0; i<jsonArray.length(); i++){
                        temp = new TreeMap<>();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        temp.put(obj.getString("date"),obj.getString("year"));
                    }
                    ACache a = ACache.get(mContext);
                    Map.Entry<String,String> entry = ((TreeMap<String,String>)temp).lastEntry();
                    int year = Integer.parseInt(entry.getValue());
                    int monthOfYear = Integer.parseInt(entry.getKey().split("月")[0]);
                    int dayOfMonth = Integer.parseInt(entry.getKey().split("月")[1].split("日")[0]);
                    String date = String.valueOf(year) + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth);
                    jsonString2 = date;
                    a.put("LatestMatchDate", date, ACache.TEST_TIME);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            jsonString = GetHttpResponse.getHttpResponse(Consts.dayrank + "?date=" + jsonString2);
            this.currentDate = jsonString2;

            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(mContext).put("getDayRanks" ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            String jsonString2 = ACache.get(mContext).getAsString("LatestMatchDate");
            if(jsonString2 == null){
                jsonString2 = GetHttpResponse.getHttpResponse(Consts.getLatestMatchList);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString2);
                    Map<String,String> temp = new TreeMap<>();
                    for(int i = 0; i<jsonArray.length(); i++){
                        temp = new TreeMap<>();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        temp.put(obj.getString("date"),obj.getString("year"));
                    }
                    ACache a = ACache.get(mContext);
                    Map.Entry<String,String> entry = ((TreeMap<String,String>)temp).lastEntry();
                    int year = Integer.parseInt(entry.getValue());
                    int monthOfYear = Integer.parseInt(entry.getKey().split("月")[0]);
                    int dayOfMonth = Integer.parseInt(entry.getKey().split("月")[1].split("日")[0]);
                    String date = String.valueOf(year) + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth);
                    jsonString2 = date;
                    a.put("LatestMatchDate", date, ACache.TEST_TIME);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            jsonString = GetHttpResponse.getHttpResponse(Consts.dayrank + "?date=" + jsonString2);
            this.currentDate = jsonString2;
            Log.i("Resource",Consts.resourceFromCache);
        }

        List<DayRankInfo> dayranks = new ArrayList<>();
        try{
            JSONArray array2 = new JSONArray(jsonString);
            for (int i = 0; i < array2.length(); i++) {
                DayRankInfo temp = new DayRankInfo();
                JSONObject obj = array2.getJSONObject(i);
                String name = obj.getString("name");
                String id = obj.getString("id");
                String teamName = obj.getString("teamName");
                String data = obj.getString("data");
                String seasonData = obj.getString("seasonData");
                String type = obj.getString("type");

                temp.setName(name);
                temp.setId(id);
                temp.setTeamName(teamName);
                temp.setData(data);
                temp.setSeasonData(seasonData);
                temp.setType(type);
                dayranks.add(temp);
            }

        }catch(NullPointerException e2){
            e2.printStackTrace();
        }
        catch (JSONException e2) {
            e2.printStackTrace();
        }
        finally{
            return dayranks;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Consts.SET_VIEW:
                    initViewPager();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(mContext, Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initListener(){
        tv_rankGuide2.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_rankGuide3.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_rankGuide4.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });


        //ViewPager的监听  解决用户通过手势滑动获取新的模块 此时标签栏没有同步改变的问题
        //同时完成滑块滑动的动画
        pager.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //跳转到新的fragment
            switch (v.getId()){
                case R.id.tv_rankGuide2:
                    pager.setCurrentItem(0);
                    break;
                case R.id.tv_rankGuide3:
                    pager.setCurrentItem(1);
                    break;
                case R.id.tv_rankGuide4:
                    pager.setCurrentItem(2);
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
            one = bmpw;
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
                    tv_rankGuide2.setAlpha((float) 0.5);
                    break;
                case 1:
                    tv_rankGuide3.setAlpha((float) 0.5);
                    break;
                case 2:
                    tv_rankGuide4.setAlpha((float) 0.5);
                    break;
            }
            //将即将切换到的标签的高亮显示
            switch (position){
                case 0:
                    tv_rankGuide2.setAlpha(1);
                    break;
                case 1:
                    tv_rankGuide3.setAlpha(1);
                    break;
                case 2:
                    tv_rankGuide4.setAlpha(1);
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
