package com.sora.projectn.model.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.Adapter.TeamCombatAdapter;
import com.sora.projectn.utils.beans.TeamCombatStaticsVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016-05-03.
 */
public class TeamCombatActivity extends AppCompatActivity{


    private ListView listView;
    Context mContext;

    private Toolbar toolbar;

    /**
     * 参与对比队伍的Id
     */
    private int teamA;
    private int teamB;

    private List<TeamCombatStaticsVo> list = new ArrayList<>();

    private String season;
    private int totalMatches;

    private TextView tv_combatSeason;
    private TextView tv_combatNum;
    private TextView tv_teamCombat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teamcombat);

        initView();

        parseIntent();


        new Thread(new Runnable() {
            @Override
            public void run() {
                list = getTeamCombatStatistics(teamA, teamB);

                if (list == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }
            }
        }).start();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        teamA = bundle.getInt("teamA");
        teamB = bundle.getInt("teamB");


    }

    private void initView() {
        mContext = this;

        listView = (ListView) findViewById(R.id.lv_teamCombat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("数据对比");
        //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_combatSeason = (TextView) findViewById(R.id.tv_combatSeason);
        tv_combatNum = (TextView) findViewById(R.id.tv_combatNum);
        tv_teamCombat = (TextView) findViewById(R.id.tv_teamCombat);
    }

    /**
     * Handler
     */
    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Consts.SET_VIEW:
                    setView();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(mContext, Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
            }

        }
    };

    /**
     * 更新界面
     */
    private void setView() {
        tv_combatSeason.setText(season + "赛季");
        tv_combatNum.setText("两队交手次数 : " + totalMatches);

        tv_teamCombat.setText("赛季场均数据对比");

        TeamCombatAdapter adapter = new TeamCombatAdapter(mContext,list);

        listView.setAdapter(adapter);



    }

    private List<TeamCombatStaticsVo> getTeamCombatStatistics(int teamA, int teamB) {
        List<TeamCombatStaticsVo> list = new ArrayList<>();

        String jsonString = ACache.get(mContext).getAsString("getTeamCombatStatistics - " + teamA + " - " + teamB);

        if (jsonString == null){

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamVsStatistics + "?" +"teamId=" + teamA + "&" + "vsTeamId=" +teamB);

            if (jsonString == null){
                return null;
            }
            ACache.get(mContext).put("getTeamCombatStatistics - " + teamA + " - " + teamB,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }
     
        
        try {
            JSONArray array = new JSONArray(jsonString);

            JSONObject objA = array.getJSONObject(0);
            JSONObject objB = array.getJSONObject(1);

            DecimalFormat decimalFormat = new DecimalFormat("######0.00");

            season = objA.getString("season");

            totalMatches = objA.getInt("totalMatches");

            /**
             * 球队名称
             */
            list.add(new TeamCombatStaticsVo("",String.valueOf(objA.getString("teamName")),String.valueOf(objB.getString("teamName"))));

            /**
             * 两分命中
             */
            list.add(new TeamCombatStaticsVo("两分命中",String.valueOf(objA.getDouble("twoHit")),String.valueOf(objB.getDouble("twoHit"))));

            /**
             * 两分出手
             */
            list.add(new TeamCombatStaticsVo("两分出手",String.valueOf(objA.getDouble("twoShot")),String.valueOf(objB.getDouble("twoShot"))));

            /**
             * 两分命中率
             */
            list.add(new TeamCombatStaticsVo("两分命中率",decimalFormat.format(objA.getDouble("twoHit") / objA.getDouble("twoShot") * 100.0) + "%",decimalFormat.format(objB.getDouble("twoHit") / objB.getDouble("twoShot") * 100.0) + "%"));

            /**
             * 三分命中
             */
            list.add(new TeamCombatStaticsVo("三分命中",String.valueOf(objA.getDouble("threeHit")),String.valueOf(objB.getDouble("threeHit"))));

            /**
             * 三分出手
             */
            list.add(new TeamCombatStaticsVo("三分出手",String.valueOf(objA.getDouble("threeShot")),String.valueOf(objB.getDouble("threeShot"))));

            /**
             * 三分命中率
             */
            list.add(new TeamCombatStaticsVo("三分命中率",decimalFormat.format(objA.getDouble("threeHit") / objA.getDouble("threeShot") * 100.0) + "%",decimalFormat.format(objB.getDouble("threeHit") / objB.getDouble("threeShot") * 100.0) + "%"));

            /**
             * 罚球命中
             */
            list.add(new TeamCombatStaticsVo("罚球命中",String.valueOf(objA.getDouble("freeThrowHit")),String.valueOf(objB.getDouble("freeThrowHit"))));

            /**
             * 罚球出手
             */
            list.add(new TeamCombatStaticsVo("罚球出手",String.valueOf(objA.getDouble("freeThrowShot")),String.valueOf(objB.getDouble("freeThrowShot"))));

            /**
             * 罚球命中率
             */
            list.add(new TeamCombatStaticsVo("罚球命中率",decimalFormat.format(objA.getDouble("freeThrowHit") / objA.getDouble("freeThrowShot") * 100.0) + "%",decimalFormat.format(objB.getDouble("freeThrowHit") / objB.getDouble("freeThrowShot") * 100.0) + "%"));

            /**
             * 前场篮板
             */
            list.add(new TeamCombatStaticsVo("前场篮板",String.valueOf(objA.getDouble("offReb")),String.valueOf(objB.getDouble("offReb"))));

            /**
             * 后场篮板
             */
            list.add(new TeamCombatStaticsVo("后场篮板",String.valueOf(objA.getDouble("defReb")),String.valueOf(objB.getDouble("defReb"))));

            /**
             * 总篮板
             */
            list.add(new TeamCombatStaticsVo("总篮板",String.valueOf(objA.getDouble("totReb")),String.valueOf(objB.getDouble("totReb"))));

            /**
             * 助攻
             */
            list.add(new TeamCombatStaticsVo("助攻",String.valueOf(objA.getDouble("ass")),String.valueOf(objB.getDouble("ass"))));

            /**
             * 抢断
             */
            list.add(new TeamCombatStaticsVo("抢断",String.valueOf(objA.getDouble("steal")),String.valueOf(objB.getDouble("steal"))));

            /**
             * 盖帽
             */
            list.add(new TeamCombatStaticsVo("盖帽",String.valueOf(objA.getDouble("blockShot")),String.valueOf(objB.getDouble("blockShot"))));

            /**
             * 失误
             */
            list.add(new TeamCombatStaticsVo("失误",String.valueOf(objA.getDouble("turnOver")),String.valueOf(objB.getDouble("turnOver"))));

            /**
             * 犯规
             */
            list.add(new TeamCombatStaticsVo("犯规",String.valueOf(objA.getDouble("foul")),String.valueOf(objB.getDouble("foul"))));

            /**
             * 得分
             */
            list.add(new TeamCombatStaticsVo("得分",String.valueOf(objA.getDouble("score")),String.valueOf(objB.getDouble("score"))));





        } catch (JSONException e) {
            e.printStackTrace();
        }
        


        return list;
    }



}
