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
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.Adapter.TeamPlayerAdapter;
import com.sora.projectn.utils.beans.TeamPlayerVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016/1/28.
 */
public class TeamPlayerFragment extends Fragment {

    private Context mContext;


    private int id;
    private List<TeamPlayerVo> list = new ArrayList<>();

    private ListView listView;
    private View  fView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teamplayer, container, false);

        fView = view;

        initView();

        parseIntent();

        new Thread(new Runnable() {
            @Override
            public void run() {
                list = getTeamPlayer();


                if (list == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }

            }
        }).start();


        return view;

    }

    /**
     * 初始化
     */
    private void initView() {
        //获取上下文环境
        mContext = this.getActivity();

        listView = (ListView) fView.findViewById(R.id.lv_teamPlayer);
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
        TeamPlayerAdapter adapter = new TeamPlayerAdapter(mContext,list);

        listView.setAdapter(adapter);

    }

    public List<TeamPlayerVo> getTeamPlayer() {
        List<TeamPlayerVo> list = new ArrayList<>();

        String jsonString = ACache.get(mContext).getAsString("getTeamPlayerList - " + id);

        if (jsonString == null){

            //从server获取数据


            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamPlayerList + "?teamId=" + id);

            if (jsonString == null){
                return null;
            }


            ACache.get(mContext).put("getTeamPlayerList - " + id ,jsonString,ACache.TEST_TIME);

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
                TeamPlayerVo vo = new TeamPlayerVo(obj.getInt("id"),obj.getInt("num"),obj.getString("name"),obj.getString("pos"));
                list.add(vo);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}