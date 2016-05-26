package com.sora.projectn.model.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Adapter.MatchPlayerAdapter;
import com.sora.projectn.utils.Adapter.MatchTeamAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.beans.PlayerMatchInfo;
import com.sora.projectn.utils.beans.TeamMatchInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016/2/6.
 */
public class MatchActivity extends AppCompatActivity{

    private int no;
    private int matchId = 2015102701;
    private int homeTeamId = 0 , customTeamId = 0;
    private static final int GET_DATA = 0x01;
    private List<TeamMatchInfo> matchTeamInfo = new ArrayList<>();
    private List<PlayerMatchInfo> matchHomeTeamPlayerInfo = new ArrayList<>();
    private List<PlayerMatchInfo> matchCustomTeamPlayerInfos = new ArrayList<>();

    private Toolbar toolbar;

    private ListView lv_matchTeamInfo, lv_matchHomeTeamPlayerInfo, lv_matchCustomTeamPlayerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        initView();
        parseIntent();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();


    }

    /**
     * 获取数据
     */
    private void getData() {
        matchTeamInfo = getMatchTeamInfo();
        List<PlayerMatchInfo> matchPlayerInfo = getMatchPlayerInfo();

        for(PlayerMatchInfo t: matchPlayerInfo){
            if(Integer.parseInt(t.getTeamId()) == homeTeamId){
                matchHomeTeamPlayerInfo.add(t);
            }
            else if(Integer.parseInt(t.getTeamId()) == customTeamId){
                matchCustomTeamPlayerInfos.add(t);
            }
        }
        handler.sendEmptyMessage(GET_DATA);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_DATA:
                    //TODO 让界面显示比赛信息
                    MatchTeamAdapter matchTeamAdapter = new MatchTeamAdapter(matchTeamInfo,MatchActivity.this);
                    lv_matchTeamInfo.setAdapter(matchTeamAdapter);
                    MatchPlayerAdapter matchPlayerAdapter1 = new MatchPlayerAdapter(matchHomeTeamPlayerInfo,MatchActivity.this);
                    lv_matchHomeTeamPlayerInfo.setAdapter(matchPlayerAdapter1);
                    lv_matchCustomTeamPlayerInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //TODO 添加到球员的跳转.
//                            Intent intent = new Intent(MatchActivity.this, TeamActivity.class);
//                            intent.putExtra("id", Integer.parseInt(eastRanks.get(position).get("teamID")));
//                            startActivity(intent);
                        }
                    });
                    MatchPlayerAdapter matchPlayerAdapter2 = new MatchPlayerAdapter(matchCustomTeamPlayerInfos,MatchActivity.this);
                    lv_matchCustomTeamPlayerInfo.setAdapter(matchPlayerAdapter2);
            }
        }
    };



    private List<TeamMatchInfo> getMatchTeamInfo(){
        List<TeamMatchInfo> matchTeamInfo = new ArrayList<>();
        String jsonString1;
        jsonString1 = ACache.get(this).getAsString("getTeamMatchStatistics - " + matchId);
        if(jsonString1 == null) {
            String url = Consts.getTeamMatchStatistics + "?" + "matchId=" + matchId;
            jsonString1 = GetHttpResponse.getHttpResponse(url);
        }

        try {
            JSONArray array = new JSONArray(jsonString1);
            for (int i = 0; i < array.length(); i++) {
                TeamMatchInfo temp = new TeamMatchInfo();
                JSONObject obj = array.getJSONObject(i);
                String name = null;
                String jsonString;

                /**
                 * getTeamInfos
                 */
                jsonString = ACache.get(this).getAsString("getTeamInfos - " + obj.get("teamId"));

                if (jsonString == null){

                    jsonString = GetHttpResponse.getHttpResponse(Consts.getTeamInfos + "?teamId=" + obj.get("teamId"));



                    ACache.get(this).put("getTeamInfos - " + obj.get("teamId") ,jsonString,ACache.TEST_TIME);
                    Log.i("Resource", Consts.resourceFromServer);
                }
                else
                {
                    Log.i("Resource",Consts.resourceFromCache);
                }

                //解析jsonString 构造Map
                try {

                    JSONObject obj2 = new JSONObject(jsonString);

                    name = obj2.getString("name");


                }


                catch(NullPointerException e){
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                String matchId = obj.getString("matchId");
                String teamId = obj.getString("teamId");
                String ifHome = obj.getString("ifHome");
                String time = obj.getString("time");
                String twoHit = obj.getString("twoHit");
                String twoShot = obj.getString("twoShot");
                String threeHit = obj.getString("threeHit");
                String threeShot = obj.getString("threeShot");
                String freeThrowHit = obj.getString("freeThrowHit");
                String freeThrowShot = obj.getString("freeThrowShot");
                String offReb = obj.getString("offReb");
                String defReb = obj.getString("defReb");
                String totReb = obj.getString("totReb");
                String ass = obj.getString("ass");
                String steal = obj.getString("steal");
                String blockShot = obj.getString("blockShot");
                String turnOver = obj.getString("turnOver");
                String foul = obj.getString("foul");
                String score = obj.getString("score");

                temp.setMatchId(matchId);
                temp.setTeamId(teamId);
                temp.setName(name);
                temp.setIfHome(ifHome);
                if(customTeamId == 0 && Integer.parseInt(ifHome) == 0){
                    customTeamId = Integer.parseInt(teamId);
                }
                else if(homeTeamId == 0 && Integer.parseInt(ifHome) == 1){
                    homeTeamId = Integer.parseInt(teamId);
                }
                temp.setTime(time);
                temp.setTwoHit(twoHit);
                temp.setTwoShot(twoShot);
                temp.setThreeHit(threeHit);
                temp.setThreeShot(threeShot);
                temp.setFreeThrowHit(freeThrowHit);
                temp.setFreeThrowShot(freeThrowShot);
                temp.setOffReb(offReb);
                temp.setDefReb(defReb);
                temp.setTotReb(totReb);
                temp.setAss(ass);
                temp.setSteal(steal);
                temp.setBlockShot(blockShot);
                temp.setTurnOver(turnOver);
                temp.setFoul(foul);
                temp.setScore(score);



                matchTeamInfo.add(temp);
            }



        }

        catch(NullPointerException e){
            handler.sendEmptyMessage(Consts.SET_VIEW);
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
            matchTeamInfo = null;
        }finally{
            return matchTeamInfo;
        }
    }

    private List<PlayerMatchInfo> getMatchPlayerInfo(){

        List<PlayerMatchInfo> matchPlayerInfo = new ArrayList<>();
        String jsonString;
        jsonString = ACache.get(this).getAsString("getPlayerMatchStatistics - "+matchId );
        if(jsonString == null) {
            String url = Consts.getPlayerMatchStatistics + "?" + "matchId=" + matchId;
            jsonString = GetHttpResponse.getHttpResponse(url);
            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(this).put("getPlayerMatchStatistics - "+matchId ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                PlayerMatchInfo temp = new PlayerMatchInfo();
                JSONObject obj = array.getJSONObject(i);
                String matchId = obj.getString("matchId");
                String playerId = obj.getString("playerId");
                String teamId = obj.getString("teamId");
                String playerName = obj.getString("playerName");
                String isFirst = obj.getString("isFirst");
                String time = obj.getString("time");
                String twoHit = obj.getString("twoHit");
                String twoShot = obj.getString("twoShot");
                String threeHit = obj.getString("threeHit");
                String threeShot = obj.getString("threeShot");
                String freeThrowHit = obj.getString("freeThrowHit");
                String freeThrowShot = obj.getString("freeThrowShot");
                String offReb = obj.getString("offReb");
                String defReb = obj.getString("defReb");
                String totReb = obj.getString("totReb");
                String ass = obj.getString("ass");
                String steal = obj.getString("steal");
                String blockShot = obj.getString("blockShot");
                String turnOver = obj.getString("turnOver");
                String foul = obj.getString("foul");
                String score = obj.getString("score");

                temp.setMatchId(matchId);
                temp.setPlayerId(playerId);
                temp.setTeamId(teamId);
                temp.setPlayerName(playerName);
                temp.setIsFirst(isFirst);
                temp.setTime(time);
                temp.setTwoHit(twoHit);
                temp.setTwoShot(twoShot);
                temp.setThreeHit(threeHit);
                temp.setThreeShot(threeShot);
                temp.setFreeThrowHit(freeThrowHit);
                temp.setFreeThrowShot(freeThrowShot);
                temp.setOffReb(offReb);
                temp.setDefReb(defReb);
                temp.setTotReb(totReb);
                temp.setAss(ass);
                temp.setSteal(steal);
                temp.setBlockShot(blockShot);
                temp.setTurnOver(turnOver);
                temp.setFoul(foul);
                temp.setScore(score);



                 matchPlayerInfo.add(temp);
            }



        }
        catch(NullPointerException e){
            handler.sendEmptyMessage(Consts.SET_VIEW);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            matchPlayerInfo = null;
        }
        return matchPlayerInfo;
    }

    private void initView(){
        lv_matchTeamInfo = (ListView)findViewById(R.id.match_pts);
        lv_matchHomeTeamPlayerInfo = (ListView)findViewById(R.id.match_2);
        lv_matchCustomTeamPlayerInfo = (ListView)findViewById(R.id.match_3);

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("比赛详情");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    /**
     * 读取Intent传递的no 获取比赛对应编号
     */
    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        no = bundle.getInt("no");
        matchId = no;
    }
}
