package com.sora.projectn.model.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerSeasonStatisticsFragment extends Fragment {

    /**
     * 记录球员赛季基础数据
     */
    private Map<String ,String> infoMap = new HashMap<>();


    private int id;
    private Context mContext;
    private View fView;

    private TextView tv_season_player;
    private TextView tv_team_name;
    private TextView tv_is_first;
    private TextView tv_total_matches;
    private TextView tv_time;
    private TextView tv_two_shot;
    private TextView tv_two_hit;
    private TextView tv_three_shot;
    private TextView tv_three_hit;
    private TextView tv_free_throw_shot;
    private TextView tv_free_throw_hit;
    private TextView tv_off_reb;
    private TextView tv_def_reb;
    private TextView tv_tot_reb;
    private TextView tv_ass;
    private TextView tv_steal;
    private TextView tv_block_shot;
    private TextView tv_turn_over;
    private TextView tv_foul;
    private TextView tv_score;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_season_statistics,container,false);

        fView = view;

        initView();

        parseIntent();

        new Thread(new Runnable() {
            @Override
            public void run() {
                infoMap = getPlayerInfo();

                //测试数据
//            test();

                if (infoMap == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }



            }
        }).start();


        return view;

    }

    private void initView() {
        mContext = this.getActivity();

        tv_season_player  = (TextView) fView.findViewById(R.id.tv_season_player);
        tv_team_name = (TextView) fView.findViewById(R.id.tv_team_name);;
        tv_is_first = (TextView) fView.findViewById(R.id.tv_is_first);;
        tv_total_matches = (TextView) fView.findViewById(R.id.tv_total_matches);;
        tv_time = (TextView) fView.findViewById(R.id.tv_time);;
        tv_two_shot = (TextView) fView.findViewById(R.id.tv_two_shot);;
        tv_two_hit = (TextView) fView.findViewById(R.id.tv_two_hit);;
        tv_three_shot = (TextView) fView.findViewById(R.id.tv_three_shot);;
        tv_three_hit = (TextView) fView.findViewById(R.id.tv_three_hit);;
        tv_free_throw_shot = (TextView) fView.findViewById(R.id.tv_free_throw_shot);;
        tv_free_throw_hit = (TextView) fView.findViewById(R.id.tv_free_throw_hit);;
        tv_off_reb = (TextView) fView.findViewById(R.id.tv_off_reb);;
        tv_def_reb = (TextView) fView.findViewById(R.id.tv_def_reb);;
        tv_tot_reb = (TextView) fView.findViewById(R.id.tv_tot_reb);;
        tv_ass = (TextView) fView.findViewById(R.id.tv_ass);;
        tv_steal = (TextView) fView.findViewById(R.id.tv_steal);;
        tv_block_shot = (TextView) fView.findViewById(R.id.tv_block_shot);;
        tv_turn_over = (TextView) fView.findViewById(R.id.tv_turn_over);;
        tv_foul = (TextView) fView.findViewById(R.id.tv_foul);;
        tv_score = (TextView) fView.findViewById(R.id.tv_score);

    }

    /**
     * 接收Activity传递的参数
     */
    private void parseIntent(){
        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");

    }


    /**
     * Handler
     */
    Handler handler = new Handler(){
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

    private void setView(){
        tv_season_player.setText("赛季 : " + infoMap.get("season"));
        tv_team_name.setText("球队 : " + infoMap.get("teamName"));
        tv_is_first.setText("首发 : " + infoMap.get("isFirst")+"  次");
        tv_total_matches.setText("上场 : " + infoMap.get("totalMatches")+"  次");
        tv_time.setText("场均上场时间: " + infoMap.get("time")+"  分钟");
        tv_two_shot.setText("场均投篮尝试 : " + infoMap.get("twoShot"));
        tv_two_hit.setText("场均投篮命中 : " + infoMap.get("twoHit"));
        tv_three_shot.setText("场均三分尝试 : " + infoMap.get("threeShot"));
        tv_three_hit.setText("场均三分命中 : " + infoMap.get("threeHit"));
        tv_free_throw_shot.setText("场均罚球尝试 : " + infoMap.get("freeThrowShot"));
        tv_free_throw_hit.setText("场均罚球命中 : " + infoMap.get("freeThrowHit"));
        tv_off_reb.setText("场均进攻篮板 : " + infoMap.get("offReb"));
        tv_def_reb.setText("场均防守篮板 : " + infoMap.get("defReb"));
        tv_tot_reb.setText("场均篮板 : " + infoMap.get("totReb"));
        tv_ass.setText("场均助攻 : " + infoMap.get("ass"));
        tv_steal.setText("场均抢断 : " + infoMap.get("steal"));
        tv_block_shot.setText("场均盖帽 : " + infoMap.get("blockShot"));
        tv_turn_over.setText("场均失误 : " + infoMap.get("turnOver"));
        tv_foul.setText("场均犯规  :" + infoMap.get("foul"));
        tv_score.setText("场均得分 : " + infoMap.get("score"));
    }


    /**
     * 获取球队具体信息
     *
     * @return
     */
    public Map<String, String> getPlayerInfo() {
        Map<String ,String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerSeasonStatistics.do?playerId="+id).openStream(), "utf-8"));
            String line;
            StringBuffer content = new StringBuffer();
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();
            if (content != null) {
                try {
                    JSONObject JO = new JSONObject(content.toString());
                    map.put("season",JO.getString("season"));
                    map.put("teamName",JO.getString("teamName"));
                    map.put("isFirst",JO.getString("isFirst"));
                    map.put("totalMatches",JO.getString("totalMatches"));
                    map.put("time",JO.getString("time"));
                    map.put("twoHit",JO.getString("twoHit"));
                    map.put("twoShot",JO.getString("twoShot"));
                    map.put("threeHit",JO.getString("threeHit"));
                    map.put("threeShot",JO.getString("threeShot"));
                    map.put("freeThrowHit",JO.getString("freeThrowHit"));
                    map.put("freeThrowShot",JO.getString("freeThrowShot"));
                    map.put("offReb",JO.getString("offReb"));
                    map.put("defReb",JO.getString("defReb"));
                    map.put("totReb",JO.getString("totReb"));
                    map.put("ass",JO.getString("ass"));
                    map.put("steal",JO.getString("steal"));
                    map.put("blockShot",JO.getString("blockShot"));
                    map.put("turnOver",JO.getString("turnOver"));
                    map.put("foul",JO.getString("foul"));
                    map.put("score",JO.getString("score"));

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


}
