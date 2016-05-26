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
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerInfoDetailFragment extends Fragment {

    /**
     * 记录球队具体信息
     */
    private Map<String ,String> infoMap = new HashMap<>();


    private int id;
    private Context mContext;
    private View fView;

    private TextView tv_birthday;
    private TextView tv_birthplace;
    private TextView tv_college;
    private TextView tv_height;
    private TextView tv_weight;
    private TextView tv_draftStatus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_info_detail,container,false);

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

        tv_birthday = (TextView) fView.findViewById(R.id.tv_birthday);
        tv_birthplace = (TextView) fView.findViewById(R.id.tv_birthplace);
        tv_college = (TextView) fView.findViewById(R.id.tv_college);
        tv_height = (TextView) fView.findViewById(R.id.tv_height);
        tv_weight = (TextView) fView.findViewById(R.id.tv_weight);
        tv_draftStatus = (TextView) fView.findViewById(R.id.tv_draftStatus);
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
        tv_birthday.setText("生日 : " + infoMap.get("birthday"));
        tv_birthplace.setText("出生地 : " + infoMap.get("birthplace"));
        tv_college.setText("大学 : " +infoMap.get("college"));
        tv_height.setText("身高 : " + infoMap.get("height"));
        tv_weight.setText("体重 : " + infoMap.get("weight"));
        tv_draftStatus.setText("选秀情况 : " + infoMap.get("draftStatus"));

    }


    /**
     * 获取球队具体信息
     *
     * @return
     */
    public Map<String, String> getPlayerInfo() {
        Map<String ,String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerInfoDetail.do?playerId="+id).openStream(), "utf-8"));
            String line;
            StringBuffer content = new StringBuffer();
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();
            if (content != null) {
                try {
                    JSONObject JO = new JSONObject(content.toString());
                    map.put("birthday",JO.getString("birthday"));
                    map.put("birthplace",JO.getString("birthPlace"));
                    map.put("college",JO.getString("college"));
                    map.put("height",JO.getString("height"));
                    map.put("weight",JO.getString("weight"));
                    map.put("draftStatus",JO.getString("draftStatus"));

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
