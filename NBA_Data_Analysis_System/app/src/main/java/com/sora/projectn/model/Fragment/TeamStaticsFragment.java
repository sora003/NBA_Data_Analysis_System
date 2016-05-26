package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.Adapter.TeamStaticsAdapter;
import com.sora.projectn.utils.beans.TeamSeasonStaticsVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016/1/28.
 */
public class TeamStaticsFragment extends Fragment {



    private List<TeamSeasonStaticsVo> teamSeasonStaticsList = new ArrayList<>();
    private int id;
    private Context mContext;


    private ListView listView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamstatics,container,false);

        //获取上下文环境
        mContext = this.getActivity();

        listView = (ListView) view.findViewById(R.id.lv_teamStatics);

        textView = (TextView) view.findViewById(R.id.tv_teamStatics);

        parseIntent();

        /**
         * 获取数据
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                teamSeasonStaticsList = getTeamSeasonStatistics();


                if (teamSeasonStaticsList == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }

            }
        }).start();


        return view;

    }

    private List<TeamSeasonStaticsVo> getTeamSeasonStatistics() {
        List<TeamSeasonStaticsVo> list = new ArrayList<>();

        String jsonString = ACache.get(mContext).getAsString("getTeamSeasonStatistics - " + id);



        if (jsonString == null){
            //从server获取数据

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamSeasonStatistics + "?teamId=" + id);


            if (jsonString == null){
                return null;
            }


            ACache.get(mContext).put("getTeamSeasonStatistics - " + id ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造List
        try {


            JSONObject obj = new JSONObject(jsonString);


            DecimalFormat decimalFormat = new DecimalFormat("######0.00");

            /**
             * 两分命中
             */
            list.add(new TeamSeasonStaticsVo("两分命中",String.valueOf(obj.getDouble("twoHit"))));

            /**
             * 两分出手
             */
            list.add(new TeamSeasonStaticsVo("两分出手",String.valueOf(obj.getDouble("twoShot"))));

            /**
             * 两分命中率
             */
            list.add(new TeamSeasonStaticsVo("两分命中率",decimalFormat.format(obj.getDouble("twoHit") / obj.getDouble("twoShot") * 100.0) + "%"));

            /**
             * 三分命中
             */
            list.add(new TeamSeasonStaticsVo("三分命中",String.valueOf(obj.getDouble("threeHit"))));

            /**
             * 三分出手
             */
            list.add(new TeamSeasonStaticsVo("三分出手",String.valueOf(obj.getDouble("threeShot"))));

            /**
             * 三分命中率
             */
            list.add(new TeamSeasonStaticsVo("三分命中率",decimalFormat.format(obj.getDouble("threeHit") / obj.getDouble("threeShot") * 100.0) + "%"));

            /**
             * 罚球命中
             */
            list.add(new TeamSeasonStaticsVo("罚球命中",String.valueOf(obj.getDouble("freeThrowHit"))));

            /**
             * 罚球出手
             */
            list.add(new TeamSeasonStaticsVo("罚球出手",String.valueOf(obj.getDouble("freeThrowShot"))));

            /**
             * 罚球命中率
             */
            list.add(new TeamSeasonStaticsVo("罚球命中率",decimalFormat.format(obj.getDouble("freeThrowHit") / obj.getDouble("freeThrowShot") * 100.0) + "%"));

            /**
             * 前场篮板
             */
            list.add(new TeamSeasonStaticsVo("前场篮板",String.valueOf(obj.getDouble("offReb"))));

            /**
             * 后场篮板
             */
            list.add(new TeamSeasonStaticsVo("后场篮板",String.valueOf(obj.getDouble("defReb"))));

            /**
             * 总篮板
             */
            list.add(new TeamSeasonStaticsVo("总篮板",String.valueOf(obj.getDouble("totReb"))));

            /**
             * 助攻
             */
            list.add(new TeamSeasonStaticsVo("助攻",String.valueOf(obj.getDouble("ass"))));

            /**
             * 抢断
             */
            list.add(new TeamSeasonStaticsVo("抢断",String.valueOf(obj.getDouble("steal"))));

            /**
             * 盖帽
             */
            list.add(new TeamSeasonStaticsVo("盖帽",String.valueOf(obj.getDouble("blockShot"))));

            /**
             * 失误
             */
            list.add(new TeamSeasonStaticsVo("失误",String.valueOf(obj.getDouble("turnOver"))));

            /**
             * 犯规
             */
            list.add(new TeamSeasonStaticsVo("犯规",String.valueOf(obj.getDouble("foul"))));

            /**
             * 得分
             */
            list.add(new TeamSeasonStaticsVo("得分",String.valueOf(obj.getDouble("score"))));



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
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
                break;
        }
    }
};

    /**
     * 更新界面
     */
    private void setView() {
        TeamStaticsAdapter adapter = new TeamStaticsAdapter(mContext,teamSeasonStaticsList);

        listView.setAdapter(adapter);

        textView.setText("赛季场均数据统计");


    }
}
