package com.sora.projectn.model.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.TeamActivity;
import com.sora.projectn.model.Activity.TeamListActivity;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sora on 2016-05-20.
 */

public class TeamFavActivity extends AppCompatActivity{

        Toolbar toolbar;
        TextView textView_fav;
        TextView textView_change;

        private int teamId;

        private String teamName;

        protected Map<String ,Integer> idMap = new HashMap<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_teamfav);

            initView();

            parseIntent();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    teamName = SharedPreferencesHelper.getFavTeam(getApplicationContext());

                    idMap = getTeamsId();

                    teamId = idMap.get(teamName);

                    if (idMap == null){
                        handler.sendEmptyMessage(Consts.RES_ERROR);
                    }
                    else {
                        handler.sendEmptyMessage(Consts.SET_VIEW);
                    }


                }
            }).start();


            initListener();

        }

        private void parseIntent() {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null){
                teamName = bundle.getString("teamName");
                SharedPreferencesHelper.setFavTeam(getApplicationContext(),teamName);
            }

        }



    private void setView() {
            textView_fav.setText(teamName);
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
                        Toast.makeText(getApplicationContext(), Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        private void initListener() {
            textView_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("TeamFavActivity", 1);
                    Intent intent = new Intent(getApplicationContext(),TeamListActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            textView_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", teamId);
                    Intent intent = new Intent(getApplicationContext(),TeamActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        private void initView() {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            //设置Toolbar标题
            toolbar.setTitle("关注球队");
            //设置标题颜色
            toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
            //设置返回键可用
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    System.exit(0);
                }
            });


            textView_fav = (TextView) findViewById(R.id.tv_favTeam);
            textView_change = (TextView) findViewById(R.id.tv_changeFavTeam);
        }


        /**
         * 获取数据
         * @return idMap
         */
        protected Map<String,Integer> getTeamsId() {

            Map<String,Integer> map = new HashMap<>();

            String jsonString = ACache.get(getApplicationContext()).getAsString("getTeams");

            if (jsonString == null){
                //从server获取数据
//            jsonString = "[{\"id\":1,\"name\":\"老鹰\",\"city\":\"亚特兰大\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"菲利普斯球馆\",\"startYearInNBA\":1949,\"numOfChampions\":1},{\"id\":2,\"name\":\"凯尔特人\",\"city\":\"波士顿\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"TD花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":17},{\"id\":3,\"name\":\"鹈鹕\",\"city\":\"新奥尔良\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"新奥尔良球馆\",\"startYearInNBA\":1988,\"numOfChampions\":0},{\"id\":4,\"name\":\"公牛\...(line truncated)...

                jsonString= GetHttpResponse.getHttpResponse(Consts.getTeams);

                if (jsonString == null){
                    return null;
                }

                ACache.get(getApplicationContext()).put("getTeams",jsonString,ACache.TEST_TIME);
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
    }