package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.TeamActivity;
import com.sora.projectn.model.Activity.TeamFavActivity;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.BitmapHelper;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.MyListView;
import com.sora.projectn.utils.Adapter.MySimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Sora on 2016-04-25.
 */
public class TeamListFragment extends Fragment {


    protected int isTeamFavActivity = 0;

    protected Context mContext;

    /**
     * 球队编码
     */
    protected int teamId;

    protected View fView;

    protected MyListView listView1;
    protected MyListView listView2;
    protected MyListView listView3;

    protected TextView textView1;
    protected TextView textView2;
    protected TextView textView3;

    protected int league;

    protected Map<String ,String> confMap = new HashMap<>();

    protected Map<String ,Integer> idMap = new HashMap<>();

    /**
     * 分区球队列表
     */
    protected List<String> list1 = new ArrayList<>();
    protected List<String> list2 = new ArrayList<>();
    protected List<String> list3 = new ArrayList<>();

    protected List<Map<String,Object>> maps1 = new ArrayList<>();
    protected List<Map<String,Object>> maps2 = new ArrayList<>();
    protected List<Map<String,Object>> maps3 = new ArrayList<>();



    /**
     * 记录分区
     */
    protected String[] conf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamlist,container,false);

        fView = view;

        initView();

        parseIntent();

        initListener();



        new Thread(new Runnable() {
            @Override
            public void run() {

                confMap = getTeams();
                idMap = getTeamsId();

                if (confMap == null || idMap == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    getTeamList(confMap);
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }




            }
        }).start();

        return view;
    }

    protected void initListener() {
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView myListView=(ListView)parent;
                HashMap<String,Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView myListView=(ListView)parent;
                HashMap<String,Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView myListView=(ListView)parent;
                HashMap<String,Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
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
                    break;
                case Consts.TURN_ACTIVITY:
                    //绑定id参数 启动TeamActivity
                    Intent intent;
                    Bundle bundle = new Bundle();
                    if (isTeamFavActivity == 1){
                        intent = new Intent(mContext,TeamFavActivity.class);
                        bundle.putString("teamName", getTeamName(teamId));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(mContext,TeamActivity.class);
                        bundle = new Bundle();
                        bundle.putInt("id", teamId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    /**
     * 更新界面
     */
    protected void setView() {
        listView1.setAdapter(new MySimpleAdapter(mContext,maps1,R.layout.item_teamlist,new String[]{"logo","name"},new int[] {R.id.iv_teamList,R.id.tv_teamList}));
        listView2.setAdapter(new MySimpleAdapter(mContext,maps2,R.layout.item_teamlist,new String[]{"logo","name"},new int[] {R.id.iv_teamList,R.id.tv_teamList}));
        listView3.setAdapter(new MySimpleAdapter(mContext,maps3,R.layout.item_teamlist,new String[]{"logo","name"},new int[] {R.id.iv_teamList,R.id.tv_teamList}));

        textView1.setText(conf[0]);
        textView2.setText(conf[1]);
        textView3.setText(conf[2]);


    }

    /**
     * 初始化参数
     */
    protected void initView() {
        mContext = this.getActivity();

        listView1 = (MyListView) fView.findViewById(R.id.lv_con01);
        listView2 = (MyListView) fView.findViewById(R.id.lv_con02);
        listView3 = (MyListView) fView.findViewById(R.id.lv_con03);


        textView1 = (TextView) fView.findViewById(R.id.tv_con01);
        textView2 = (TextView) fView.findViewById(R.id.tv_con02);
        textView3 = (TextView) fView.findViewById(R.id.tv_con03);
    }

    /**
     * 接收Activity传递的参数
     */
    protected void parseIntent(){
        Bundle bundle = this.getArguments();
        league = bundle.getInt("league");
        isTeamFavActivity = bundle.getInt("TeamFavActivity");
    }

    /**
     * 整理数据 对map1 map2 map3封装数据
     * @param infoMap
     */
    protected void getTeamList(Map<String,String> infoMap) {

        /**
         * 判断东西部球队
         */
        //西部球队
        if (league == 0){
            conf = new String[]{Consts.Southwest,Consts.Pacific,Consts.Northwest};
        }
        //东部球队
        else if (league == 1){
            conf = new String[]{Consts.Southeast,Consts.Central,Consts.Atlantic};
        }
        else {
            return;
        }

        //获取map的key 的首个对象
        Iterator iterator = infoMap.keySet().iterator();


        while (iterator.hasNext()){
            Object key = iterator.next();
            String name= key.toString();
            String conference = confMap.get(key);

            if (conference.equals(conf[0])){
                list1.add(name);
            }
            else if (conference.equals(conf[1])){
                list2.add(name);
            }
            else if (conference.equals(conf[2])){
                list3.add(name);
            }
        }

        for (int i = 0; i < list1.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",list1.get(i));
            map.put("logo",BitmapHelper.getBitmap(list1.get(i)));
            maps1.add(map);
        }

        for (int i = 0; i < list2.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",list2.get(i));
            map.put("logo",BitmapHelper.getBitmap(list2.get(i)));
            maps2.add(map);
        }

        for (int i = 0; i < list3.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("name",list3.get(i));
            map.put("logo",BitmapHelper.getBitmap(list3.get(i)));
            maps3.add(map);
        }

    }

    /**
     * 获取数据
     * @return confMap
     */
    protected Map<String,String> getTeams() {

        Map<String,String> map = new HashMap<>();

        String jsonString = ACache.get(mContext).getAsString("getTeams");

        if (jsonString == null){
            //从server获取数据
//            jsonString = "[{\"id\":1,\"name\":\"老鹰\",\"city\":\"亚特兰大\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"菲利普斯球馆\",\"startYearInNBA\":1949,\"numOfChampions\":1},{\"id\":2,\"name\":\"凯尔特人\",\"city\":\"波士顿\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"TD花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":17},{\"id\":3,\"name\":\"鹈鹕\",\"city\":\"新奥尔良\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"新奥尔良球馆\",\"startYearInNBA\":1988,\"numOfChampions\":0},{\"id\":4,\"name\":\"公牛\",\"city\":\"芝加哥\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"联合中心球馆\",\"startYearInNBA\":1966,\"numOfChampions\":6},{\"id\":5,\"name\":\"骑士\",\"city\":\"克利夫兰\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"快贷球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":6,\"name\":\"小牛\",\"city\":\"达拉斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1980,\"numOfChampions\":1},{\"id\":7,\"name\":\"掘金\",\"city\":\"丹佛\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"百事中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":8,\"name\":\"活塞\",\"city\":\"底特律\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"奥本山宫球馆\",\"startYearInNBA\":1948,\"numOfChampions\":3},{\"id\":9,\"name\":\"勇士\",\"city\":\"金州\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"甲骨文球馆\",\"startYearInNBA\":1946,\"numOfChampions\":4},{\"id\":10,\"name\":\"火箭\",\"city\":\"休斯顿\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"丰田中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":2},{\"id\":11,\"name\":\"步行者\",\"city\":\"印第安纳\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"银行家生活球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":12,\"name\":\"快船\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":13,\"name\":\"湖人\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1948,\"numOfChampions\":16},{\"id\":14,\"name\":\"热火\",\"city\":\"迈阿密\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"美航球馆\",\"startYearInNBA\":1988,\"numOfChampions\":3},{\"id\":15,\"name\":\"雄鹿\",\"city\":\"密尔沃基\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"布拉德利中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":1},{\"id\":16,\"name\":\"森林狼\",\"city\":\"明尼苏达\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"标靶中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":17,\"name\":\"篮网\",\"city\":\"布鲁克林\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"巴克莱中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":18,\"name\":\"尼克斯\",\"city\":\"纽约\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"麦迪逊广场花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":2},{\"id\":19,\"name\":\"魔术\",\"city\":\"奥兰多\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"安利中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":20,\"name\":\"76人\",\"city\":\"费城\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"富国银行中心球馆\",\"startYearInNBA\":1937,\"numOfChampions\":3},{\"id\":21,\"name\":\"太阳\",\"city\":\"菲尼克斯\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":0},{\"id\":22,\"name\":\"开拓者\",\"city\":\"波特兰\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"玫瑰花园球馆\",\"startYearInNBA\":1970,\"numOfChampions\":1},{\"id\":23,\"name\":\"国王\",\"city\":\"萨克拉门托\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"能量平衡球馆\",\"startYearInNBA\":1948,\"numOfChampions\":1},{\"id\":24,\"name\":\"马刺\",\"city\":\"圣安东尼奥\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"AT&T中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":5},{\"id\":25,\"name\":\"雷霆\",\"city\":\"俄克拉荷马城\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"切萨皮克能源球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":26,\"name\":\"爵士\",\"city\":\"犹他\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"能源方案球馆\",\"startYearInNBA\":1974,\"numOfChampions\":0},{\"id\":27,\"name\":\"奇才\",\"city\":\"华盛顿\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"威瑞森中心球馆\",\"startYearInNBA\":1961,\"numOfChampions\":1},{\"id\":28,\"name\":\"猛龙\",\"city\":\"多伦多\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"加航中心球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":29,\"name\":\"灰熊\",\"city\":\"孟菲斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"联邦快递球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":30,\"name\":\"黄蜂\",\"city\":\"夏洛特\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"时代华纳中心球馆\",\"startYearInNBA\":2004,\"numOfChampions\":0}]";

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeams);

            if (jsonString == null){
                return null;
            }
            ACache.get(mContext).put("getTeams",jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String conference = obj.getString("conference");

                map.put(name,conference);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;

    }

    /**
     * 获取数据
     * @return idMap
     */
    protected Map<String,Integer> getTeamsId() {

        Map<String,Integer> map = new HashMap<>();

        String jsonString = ACache.get(mContext).getAsString("getTeams");

        if (jsonString == null){
            //从server获取数据
//            jsonString = "[{\"id\":1,\"name\":\"老鹰\",\"city\":\"亚特兰大\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"菲利普斯球馆\",\"startYearInNBA\":1949,\"numOfChampions\":1},{\"id\":2,\"name\":\"凯尔特人\",\"city\":\"波士顿\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"TD花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":17},{\"id\":3,\"name\":\"鹈鹕\",\"city\":\"新奥尔良\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"新奥尔良球馆\",\"startYearInNBA\":1988,\"numOfChampions\":0},{\"id\":4,\"name\":\"公牛\",\"city\":\"芝加哥\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"联合中心球馆\",\"startYearInNBA\":1966,\"numOfChampions\":6},{\"id\":5,\"name\":\"骑士\",\"city\":\"克利夫兰\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"快贷球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":6,\"name\":\"小牛\",\"city\":\"达拉斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1980,\"numOfChampions\":1},{\"id\":7,\"name\":\"掘金\",\"city\":\"丹佛\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"百事中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":8,\"name\":\"活塞\",\"city\":\"底特律\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"奥本山宫球馆\",\"startYearInNBA\":1948,\"numOfChampions\":3},{\"id\":9,\"name\":\"勇士\",\"city\":\"金州\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"甲骨文球馆\",\"startYearInNBA\":1946,\"numOfChampions\":4},{\"id\":10,\"name\":\"火箭\",\"city\":\"休斯顿\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"丰田中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":2},{\"id\":11,\"name\":\"步行者\",\"city\":\"印第安纳\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"银行家生活球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":12,\"name\":\"快船\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":13,\"name\":\"湖人\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1948,\"numOfChampions\":16},{\"id\":14,\"name\":\"热火\",\"city\":\"迈阿密\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"美航球馆\",\"startYearInNBA\":1988,\"numOfChampions\":3},{\"id\":15,\"name\":\"雄鹿\",\"city\":\"密尔沃基\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"布拉德利中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":1},{\"id\":16,\"name\":\"森林狼\",\"city\":\"明尼苏达\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"标靶中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":17,\"name\":\"篮网\",\"city\":\"布鲁克林\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"巴克莱中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":18,\"name\":\"尼克斯\",\"city\":\"纽约\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"麦迪逊广场花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":2},{\"id\":19,\"name\":\"魔术\",\"city\":\"奥兰多\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"安利中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":20,\"name\":\"76人\",\"city\":\"费城\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"富国银行中心球馆\",\"startYearInNBA\":1937,\"numOfChampions\":3},{\"id\":21,\"name\":\"太阳\",\"city\":\"菲尼克斯\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":0},{\"id\":22,\"name\":\"开拓者\",\"city\":\"波特兰\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"玫瑰花园球馆\",\"startYearInNBA\":1970,\"numOfChampions\":1},{\"id\":23,\"name\":\"国王\",\"city\":\"萨克拉门托\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"能量平衡球馆\",\"startYearInNBA\":1948,\"numOfChampions\":1},{\"id\":24,\"name\":\"马刺\",\"city\":\"圣安东尼奥\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"AT&T中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":5},{\"id\":25,\"name\":\"雷霆\",\"city\":\"俄克拉荷马城\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"切萨皮克能源球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":26,\"name\":\"爵士\",\"city\":\"犹他\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"能源方案球馆\",\"startYearInNBA\":1974,\"numOfChampions\":0},{\"id\":27,\"name\":\"奇才\",\"city\":\"华盛顿\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"威瑞森中心球馆\",\"startYearInNBA\":1961,\"numOfChampions\":1},{\"id\":28,\"name\":\"猛龙\",\"city\":\"多伦多\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"加航中心球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":29,\"name\":\"灰熊\",\"city\":\"孟菲斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"联邦快递球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":30,\"name\":\"黄蜂\",\"city\":\"夏洛特\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"时代华纳中心球馆\",\"startYearInNBA\":2004,\"numOfChampions\":0}]";

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeams);

            if (jsonString == null){
                return null;
            }

            ACache.get(mContext).put("getTeams",jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                int id = obj.getInt("id");

                map.put(name,id);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;

    }


    private  String getTeamName(int teamId){
        String teamName = "";

        Iterator iterator = idMap.keySet().iterator();

        while (iterator.hasNext()){
            teamName = iterator.next().toString();

            if (teamId == idMap.get(teamName)){
                return teamName;
            }
        }

        return teamName;

    }
}
