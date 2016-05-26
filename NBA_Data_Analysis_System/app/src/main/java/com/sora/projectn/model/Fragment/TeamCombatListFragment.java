package com.sora.projectn.model.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.TeamCombatActivity;
import com.sora.projectn.utils.Consts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sora on 2016-05-02.
 */
public class TeamCombatListFragment extends TeamListFragment {

    /**
     * 原球队id
     */
    private int originTeamId = 0;
    /**
     * 原球队名字
     */
    private String originTeamName;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamlist, container, false);

        fView = view;

        initView();

        parseIntent();

        initListener();


        new Thread(new Runnable() {
            @Override
            public void run() {


                //交换执行顺序 否则idMap取得为空
                idMap = getTeamsId();


                confMap = removeOriginTeam(getTeams());


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
                ListView myListView = (ListView) parent;
                HashMap<String, Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView myListView = (ListView) parent;
                HashMap<String, Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView myListView = (ListView) parent;
                HashMap<String, Object> item = (HashMap<String, Object>) myListView.getItemAtPosition(position);
                teamId = idMap.get(item.get("name"));
                handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
            }
        });
    }

    /**
     * Handler
     *
     * 修改发送参数 添加需要分析的球队Id
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
                    Intent intent = new Intent(mContext,TeamCombatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("teamA", originTeamId);
                    bundle.putInt("teamB", teamId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };


    /**
     * 删除了包含原球队的数据项
     *
     * @param teams
     * @return
     */
    private Map<String, String> removeOriginTeam(Map<String, String> teams) {

        Iterator iterator = idMap.keySet().iterator();

        /**
         * 根据球队id获取球队名字
         */
        while (iterator.hasNext()){
            Object key = iterator.next();
            String name = key.toString();
            int value = idMap.get(name);
            if (value == originTeamId){
                originTeamName = name;
                break;
            }
        }

        teams.remove(originTeamName);

        return teams;
    }

    /**
     * 接收Activity传递的参数
     */
    protected void parseIntent(){
        Bundle bundle = this.getArguments();
        league = bundle.getInt("league");
        originTeamId = bundle.getInt("teamId");



    }



}
