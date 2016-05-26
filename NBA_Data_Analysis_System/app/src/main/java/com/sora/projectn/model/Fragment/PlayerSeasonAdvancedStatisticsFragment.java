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


public class PlayerSeasonAdvancedStatisticsFragment extends Fragment {

    /**
     * 记录球员赛季基础数据
     */
    private Map<String ,String> infoMap = new HashMap<>();


    private int id;
    private Context mContext;
    private View fView;

    private TextView tv_two_rate;
    private TextView tv_three_rate;
    private TextView tv_free_throw_rate;
    private TextView tv_true_rate;
    private TextView tv_double_double;
    private TextView tv_triple_double;
    private TextView tv_per;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_season_advanced_statistics,container,false);

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

        tv_two_rate  = (TextView) fView.findViewById(R.id.tv_two_rate);
        tv_three_rate = (TextView) fView.findViewById(R.id.tv_three_rate);
        tv_free_throw_rate = (TextView) fView.findViewById(R.id.tv_free_throw_rate);
        tv_true_rate = (TextView) fView.findViewById(R.id.tv_true_rate);
        tv_double_double = (TextView) fView.findViewById(R.id.tv_double_double);
        tv_triple_double = (TextView) fView.findViewById(R.id.tv_triple_double);
        tv_per = (TextView) fView.findViewById(R.id.tv_per);

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
        tv_two_rate.setText("投篮命中率 : " + infoMap.get("twoRate"));
        tv_three_rate.setText("三分命中率 : " + infoMap.get("threeRate"));
        tv_free_throw_rate.setText("罚球命中率 : " + infoMap.get("freeThrowRate"));
        tv_true_rate.setText("真实命中率 : " + infoMap.get("trueRate"));
        tv_double_double.setText("双双: " + infoMap.get("doubleDouble"));
        tv_triple_double.setText("三双 : " + infoMap.get("tripleDouble"));
        tv_per.setText("效率值 : " + infoMap.get("per"));

    }


    /**
     * 获取球队具体信息
     *
     * @return
     */
    public Map<String, String> getPlayerInfo() {
        Map<String ,String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerSeasonAdvancedStatistics.do?playerId="+id).openStream(), "utf-8"));
            String line;
            StringBuffer content = new StringBuffer();
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();
            if (content != null) {
                try {
                    JSONObject JO = new JSONObject(content.toString());
                    map.put("twoRate",JO.getString("twoRate"));
                    map.put("threeRate",JO.getString("threeRate"));
                    map.put("freeThrowRate",JO.getString("freeThrowRate"));
                    map.put("trueRate",JO.getString("trueRate"));
                    map.put("doubleDouble",JO.getString("doubleDouble"));
                    map.put("tripleDouble",JO.getString("tripleDouble"));
                    map.put("per",JO.getString("per"));

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
