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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.MainActivity;
import com.sora.projectn.utils.CoachTrainAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.beans.PlayerHistoryMatchInfo;
import com.sora.projectn.utils.beans.PlayerTrainingInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CoachTrainFragment extends Fragment {
    private ArrayList<PlayerTrainingInfo> playerTrainingInfoList = new ArrayList<>();
    private ArrayList<PlayerHistoryMatchInfo> PlayerHistoryMatchInfoList1 = new ArrayList<>();
    private ArrayList<PlayerHistoryMatchInfo> PlayerHistoryMatchInfoList2 = new ArrayList<>();
    private ArrayList<PlayerHistoryMatchInfo> PlayerHistoryMatchInfoList3 = new ArrayList<>();
    private ArrayList<Integer> matchIdList = new ArrayList<>();
    private int teamId;
    private Context mContext;
    private int NUM=3;

    private static final int ALREADY_GET_MATCH_ID_LIST = 0x01;
    private static final int ALREADY_GET_PLAYER_LIST = 0x02;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_1 = 0x03;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_2 = 0x04;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_3 = 0x05;
    private static final int SET_VIEW = 0x06;

    private ListView listView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_train,container,false);

        //获取上下文环境
        mContext = this.getActivity();

        listView = (ListView) view.findViewById(R.id.lv_playerState);

        textView = (TextView) view.findViewById(R.id.tv_playerState);

        parseIntent();


        /**
         * 获取数据
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                matchIdList = getMatchIdList();

                if (matchIdList == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    Message msg = new Message();
                    msg.what=ALREADY_GET_MATCH_ID_LIST;
                    msg.obj=matchIdList;
                    handler.sendMessage(msg);
                }

            }
        }).start();



        return view;

    }

    private ArrayList<Integer> getMatchIdList() {
        ArrayList<Integer> matchIdList = new ArrayList<>();

        //从server获取数据

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getTeamMatchList.do?teamId="+teamId).openStream(), "utf-8"));
            String line;
            StringBuffer content = new StringBuffer();

            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();

            if (content != null) {
                try {
                    JSONArray jsonArray = new JSONArray(content.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject JO = jsonArray.getJSONObject(i);
                        Integer matchId = JO.getInt("id");
                        matchIdList.add(matchId);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                } finally {

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<matchIdList.size()-1;i++){
            for(int j=i+1; j<matchIdList.size();j++){
                if(matchIdList.get(i)<matchIdList.get(j)){
                    Integer temp;
                    temp=matchIdList.get(i);
                    matchIdList.set(i,matchIdList.get(j));
                    matchIdList.set(j,temp);
                }
            }
        }
        ArrayList<Integer> latestThreeMatchIds = new ArrayList<>();
        for (int i=0; i<NUM; i++){
            latestThreeMatchIds.add(matchIdList.get(i));
        }
        return latestThreeMatchIds;
    }


    /**
     * 接收Activity传递的参数
     */
    private void parseIntent(){
        Bundle bundle = this.getArguments();
        teamId = bundle.getInt("id");
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
//                    setView();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(mContext, Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
                    break;
                case ALREADY_GET_MATCH_ID_LIST:
                    matchIdList=(ArrayList<Integer>)msg.obj;
                    //获取比赛id后，获取球队所有球员
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getTeamPlayerList.do?teamId="+teamId).openStream(), "utf-8"));
                                String line;
                                StringBuffer content = new StringBuffer();
                                while ((line = br.readLine()) != null) {
                                    content.append(line);
                                }
                                br.close();

                                if (content != null) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(content.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            PlayerHistoryMatchInfo player = new PlayerHistoryMatchInfo();
                                            player.setPlayerId(JO.getInt("id"));
                                            player.setPlayerName(JO.getString("name"));
                                            player.setIsFirst(0);
                                            player.setTime(0);
                                            player.setTwoHit(0);
                                            player.setThreeHit(0);
                                            player.setTwoShot(0);
                                            player.setThreeShot(0);
                                            player.setFreeThrowHit(0);
                                            player.setFreeThrowShot(0);
                                            player.setOffReb(0);
                                            player.setDefReb(0);
                                            player.setTotReb(0);
                                            player.setAss(0);
                                            player.setSteal(0);
                                            player.setBlockShot(0);
                                            player.setTurnOver(0);
                                            player.setFoul(0);
                                            player.setScore(0);
                                            PlayerHistoryMatchInfoList1.add(player);
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            PlayerHistoryMatchInfo player = new PlayerHistoryMatchInfo();
                                            player.setPlayerId(JO.getInt("id"));
                                            player.setPlayerName(JO.getString("name"));
                                            player.setIsFirst(0);
                                            player.setTime(0);
                                            player.setTwoHit(0);
                                            player.setThreeHit(0);
                                            player.setTwoShot(0);
                                            player.setThreeShot(0);
                                            player.setFreeThrowHit(0);
                                            player.setFreeThrowShot(0);
                                            player.setOffReb(0);
                                            player.setDefReb(0);
                                            player.setTotReb(0);
                                            player.setAss(0);
                                            player.setSteal(0);
                                            player.setBlockShot(0);
                                            player.setTurnOver(0);
                                            player.setFoul(0);
                                            player.setScore(0);
                                            PlayerHistoryMatchInfoList2.add(player);
                                        }
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            PlayerHistoryMatchInfo player = new PlayerHistoryMatchInfo();
                                            player.setPlayerId(JO.getInt("id"));
                                            player.setPlayerName(JO.getString("name"));
                                            player.setIsFirst(0);
                                            player.setTime(0);
                                            player.setTwoHit(0);
                                            player.setThreeHit(0);
                                            player.setTwoShot(0);
                                            player.setThreeShot(0);
                                            player.setFreeThrowHit(0);
                                            player.setFreeThrowShot(0);
                                            player.setOffReb(0);
                                            player.setDefReb(0);
                                            player.setTotReb(0);
                                            player.setAss(0);
                                            player.setSteal(0);
                                            player.setBlockShot(0);
                                            player.setTurnOver(0);
                                            player.setFoul(0);
                                            player.setScore(0);
                                            PlayerHistoryMatchInfoList3.add(player);
                                        }




                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } finally {

                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Message msg = new Message();
                            msg.what=ALREADY_GET_PLAYER_LIST;
                            ArrayList<ArrayList<PlayerHistoryMatchInfo>> lists = new ArrayList<ArrayList<PlayerHistoryMatchInfo>>();
                            lists.add(PlayerHistoryMatchInfoList1);
                            lists.add(PlayerHistoryMatchInfoList2);
                            lists.add(PlayerHistoryMatchInfoList3);
                            msg.obj=lists;
                            handler.sendMessage(msg);

                        }
                    }).start();
                    break;

                case ALREADY_GET_PLAYER_LIST:
                    //获取到了球员列表，填充前三场比赛的数据
                    ArrayList<ArrayList<PlayerHistoryMatchInfo>> lists=(ArrayList<ArrayList<PlayerHistoryMatchInfo>>)msg.obj;

                    for(int i=0; i<matchIdList.size(); i++){
                        System.out.println(matchIdList.get(i).toString()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!@@@@@@@@@@@@");
                    }


                    PlayerHistoryMatchInfoList1=lists.get(0);
                    PlayerHistoryMatchInfoList2=lists.get(1);
                    PlayerHistoryMatchInfoList3=lists.get(2);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerMatchStatistics.do?matchId="+matchIdList.get(NUM-1)).openStream(), "utf-8"));

                                String line;
                                StringBuffer content = new StringBuffer();

                                while ((line = br.readLine()) != null) {
                                    content.append(line);
                                }
                                br.close();

                                if (content != null) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(content.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            int playerId=JO.getInt("playerId");
                                            if(hasPlayerId(playerId,PlayerHistoryMatchInfoList1)){
                                                PlayerHistoryMatchInfo player=getPlayerById(playerId,PlayerHistoryMatchInfoList1);
                                                player.setPlayerName(JO.getString("playerName"));
                                                player.setIsFirst(JO.getInt("isFirst"));
                                                player.setTime(JO.getInt("time"));
                                                player.setTwoHit(JO.getInt("twoHit"));
                                                player.setThreeHit(JO.getInt("threeHit"));
                                                player.setTwoShot(JO.getInt("twoShot"));
                                                player.setThreeShot(JO.getInt("threeShot"));
                                                player.setFreeThrowHit(JO.getInt("freeThrowHit"));
                                                player.setFreeThrowShot(JO.getInt("freeThrowShot"));
                                                player.setOffReb(JO.getInt("offReb"));
                                                player.setDefReb(JO.getInt("defReb"));
                                                player.setTotReb(JO.getInt("totReb"));
                                                player.setAss(JO.getInt("ass"));
                                                player.setSteal(JO.getInt("steal"));
                                                player.setBlockShot(JO.getInt("blockShot"));
                                                player.setTurnOver(JO.getInt("turnOver"));
                                                player.setFoul(JO.getInt("foul"));
                                                player.setScore(JO.getInt("score"));
                                                PlayerHistoryMatchInfoList1.set(getIndexByPlayerId(playerId,PlayerHistoryMatchInfoList1),player);
                                            }
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    } finally {

                                    }

                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (int i=0; i<PlayerHistoryMatchInfoList1.size();i++){
                                System.out.println("PLAYERMATCHLIST1 "+PlayerHistoryMatchInfoList1.get(i).getTime());
                            }
                            Message msg = new Message();
                            msg.what=ALREADY_GET_PLAYER_MATCH_INFO_1;
                            msg.obj=PlayerHistoryMatchInfoList1;
                            handler.sendMessage(msg);

                        }
                    }).start();
                    break;


                case ALREADY_GET_PLAYER_MATCH_INFO_1:
                    for(int i=0; i<PlayerHistoryMatchInfoList1.size(); i++){
                        PlayerHistoryMatchInfoList1.set(i, ((ArrayList<PlayerHistoryMatchInfo>) msg.obj).get(i));
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerMatchStatistics.do?matchId="+matchIdList.get(NUM-2)).openStream(), "utf-8"));

                                String line;
                                StringBuffer content = new StringBuffer();

                                while ((line = br.readLine()) != null) {
                                    content.append(line);
                                }
                                br.close();

                                if (content != null) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(content.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            int playerId=JO.getInt("playerId");
                                            if(hasPlayerId(playerId,PlayerHistoryMatchInfoList2)){
                                                PlayerHistoryMatchInfo player=getPlayerById(playerId,PlayerHistoryMatchInfoList2);
                                                player.setPlayerName(JO.getString("playerName"));
                                                player.setIsFirst(JO.getInt("isFirst"));
                                                player.setTime(JO.getInt("time"));
                                                player.setTwoHit(JO.getInt("twoHit"));
                                                player.setThreeHit(JO.getInt("threeHit"));
                                                player.setTwoShot(JO.getInt("twoShot"));
                                                player.setThreeShot(JO.getInt("threeShot"));
                                                player.setFreeThrowHit(JO.getInt("freeThrowHit"));
                                                player.setFreeThrowShot(JO.getInt("freeThrowShot"));
                                                player.setOffReb(JO.getInt("offReb"));
                                                player.setDefReb(JO.getInt("defReb"));
                                                player.setTotReb(JO.getInt("totReb"));
                                                player.setAss(JO.getInt("ass"));
                                                player.setSteal(JO.getInt("steal"));
                                                player.setBlockShot(JO.getInt("blockShot"));
                                                player.setTurnOver(JO.getInt("turnOver"));
                                                player.setFoul(JO.getInt("foul"));
                                                player.setScore(JO.getInt("score"));
                                                PlayerHistoryMatchInfoList2.set(getIndexByPlayerId(playerId,PlayerHistoryMatchInfoList2),player);

                                            }
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    } finally {

                                    }

                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (int i=0; i<PlayerHistoryMatchInfoList2.size();i++){
                                System.out.println("PLAYERMATCHLIST2 "+PlayerHistoryMatchInfoList2.get(i).getTime());
                            }
                            Message msg = new Message();
                            msg.what=ALREADY_GET_PLAYER_MATCH_INFO_2;
                            msg.obj=PlayerHistoryMatchInfoList2;
                            handler.sendMessage(msg);

                        }
                    }).start();
                    break;

                case ALREADY_GET_PLAYER_MATCH_INFO_2:
                    for(int i=0; i<PlayerHistoryMatchInfoList1.size(); i++){
                        PlayerHistoryMatchInfoList2.set(i, ((ArrayList<PlayerHistoryMatchInfo>) msg.obj).get(i));
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getPlayerMatchStatistics.do?matchId="+matchIdList.get(NUM-3)).openStream(), "utf-8"));

                                String line;
                                StringBuffer content = new StringBuffer();

                                while ((line = br.readLine()) != null) {
                                    content.append(line);
                                }
                                br.close();

                                if (content != null) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(content.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject JO = jsonArray.getJSONObject(i);
                                            int playerId=JO.getInt("playerId");
                                            if(hasPlayerId(playerId,PlayerHistoryMatchInfoList3)){
                                                PlayerHistoryMatchInfo player=getPlayerById(playerId,PlayerHistoryMatchInfoList3);
                                                player.setPlayerName(JO.getString("playerName"));
                                                player.setIsFirst(JO.getInt("isFirst"));
                                                player.setTime(JO.getInt("time"));
                                                player.setTwoHit(JO.getInt("twoHit"));
                                                player.setThreeHit(JO.getInt("threeHit"));
                                                player.setTwoShot(JO.getInt("twoShot"));
                                                player.setThreeShot(JO.getInt("threeShot"));
                                                player.setFreeThrowHit(JO.getInt("freeThrowHit"));
                                                player.setFreeThrowShot(JO.getInt("freeThrowShot"));
                                                player.setOffReb(JO.getInt("offReb"));
                                                player.setDefReb(JO.getInt("defReb"));
                                                player.setTotReb(JO.getInt("totReb"));
                                                player.setAss(JO.getInt("ass"));
                                                player.setSteal(JO.getInt("steal"));
                                                player.setBlockShot(JO.getInt("blockShot"));
                                                player.setTurnOver(JO.getInt("turnOver"));
                                                player.setFoul(JO.getInt("foul"));
                                                player.setScore(JO.getInt("score"));
                                                PlayerHistoryMatchInfoList3.set(getIndexByPlayerId(playerId,PlayerHistoryMatchInfoList3),player);
                                            }
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    } finally {

                                    }

                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            for (int i=0; i<PlayerHistoryMatchInfoList3.size();i++){
                                System.out.println("PLAYERMATCHLIST3 "+PlayerHistoryMatchInfoList3.get(i).getTime());
                            }
                            Message msg = new Message();
                            msg.what=ALREADY_GET_PLAYER_MATCH_INFO_3;
                            msg.obj=PlayerHistoryMatchInfoList3;
                            handler.sendMessage(msg);

                        }
                    }).start();
                    break;
                //得到PlayerTrainingInfo
                case ALREADY_GET_PLAYER_MATCH_INFO_3:
                    PlayerHistoryMatchInfoList3 = (ArrayList<PlayerHistoryMatchInfo>) msg.obj;
                    for(int i=0; i<PlayerHistoryMatchInfoList1.size(); i++){
                        PlayerHistoryMatchInfoList3.set(i, ((ArrayList<PlayerHistoryMatchInfo>) msg.obj).get(i));
                    }


                    for (int i=0; i<PlayerHistoryMatchInfoList2.size();i++){
                        System.out.println("!!!!!!!PLAYERMATCHLIST1 "+PlayerHistoryMatchInfoList1.get(i).getTime());
                        System.out.println("!!!!!!!PLAYERMATCHLIST2 "+PlayerHistoryMatchInfoList2.get(i).getTime());
                        System.out.println("!!!!!!!PLAYERMATCHLIST3 "+PlayerHistoryMatchInfoList3.get(i).getTime());
                    }
                    System.out.println("!!!!!!!MatchIdList     "+matchIdList.get(0));

                    getPlayerTrainingInfoList();
                    ((MainActivity)mContext).setPlayerTrainingInfoList(playerTrainingInfoList);
                    setView();

                    break;

                case SET_VIEW:

            }
        }
    };

    private boolean hasPlayerId(int id, ArrayList<PlayerHistoryMatchInfo> list){
        for (int i=0; i<list.size(); i++){
            if (id==list.get(i).getPlayerId()){
                return true;
            }
        }

        return false;
    }

    private PlayerHistoryMatchInfo getPlayerById(int playerId,ArrayList<PlayerHistoryMatchInfo> list){
        for (int i=0; i<list.size(); i++){
            if (playerId==list.get(i).getPlayerId()){
                return list.get(i);
            }
        }
        return null;
    }

    private void getPlayerTrainingInfoList(){

        for(int i=0;i<PlayerHistoryMatchInfoList3.size();i++){
            PlayerTrainingInfo playerTrainingInfo = new PlayerTrainingInfo();

            int time = PlayerHistoryMatchInfoList3.get(i).getTime();
            System.out.println(PlayerHistoryMatchInfoList1.get(i).getTime()+"hehe"+PlayerHistoryMatchInfoList2.get(i).getTime()+"hehe"+
                    PlayerHistoryMatchInfoList3.get(i).getTime());
            String state=getPlayerState(i);

            playerTrainingInfo.setPlayerId(PlayerHistoryMatchInfoList1.get(i).getPlayerId());
            playerTrainingInfo.setMatchTime(time);
            playerTrainingInfo.setState(state);
            playerTrainingInfo.setPlayerName(PlayerHistoryMatchInfoList1.get(i).getPlayerName());
            playerTrainingInfoList.add(playerTrainingInfo);

        }

        for(int i=0; i<playerTrainingInfoList.size()-1;i++){
            for(int j=i+1; j<playerTrainingInfoList.size();j++){
                if(playerTrainingInfoList.get(i).getMatchTime()<playerTrainingInfoList.get(j).getMatchTime()){
                    PlayerTrainingInfo temp;
                    temp=playerTrainingInfoList.get(i);
                    playerTrainingInfoList.set(i,playerTrainingInfoList.get(j));
                    playerTrainingInfoList.set(j,temp);
                }
            }
        }
    }

    private String getPlayerState(int i){
        String state="";
        double key[]=new double[3];
        key[0]=((double)PlayerHistoryMatchInfoList1.get(i).getScore()+0.5*(double)PlayerHistoryMatchInfoList1.get(i).getAss()
                +0.5*(double)PlayerHistoryMatchInfoList1.get(i).getTotReb()+0.5*(double)PlayerHistoryMatchInfoList1.get(i).getSteal()
                +0.5*(double)PlayerHistoryMatchInfoList1.get(i).getBlockShot()-0.5*(double)PlayerHistoryMatchInfoList1.get(i).getTurnOver()
                -(double)PlayerHistoryMatchInfoList1.get(i).getFreeThrowShot()+(double)PlayerHistoryMatchInfoList1.get(i).getFreeThrowHit())
                /((double)PlayerHistoryMatchInfoList1.get(i).getTime()+1)*10;
        key[1]=((double)PlayerHistoryMatchInfoList2.get(i).getScore()+0.5*(double)PlayerHistoryMatchInfoList2.get(i).getAss()
                +0.5*(double)PlayerHistoryMatchInfoList2.get(i).getTotReb()+0.5*(double)PlayerHistoryMatchInfoList2.get(i).getSteal()
                +0.5*(double)PlayerHistoryMatchInfoList2.get(i).getBlockShot()-0.5*(double)PlayerHistoryMatchInfoList2.get(i).getTurnOver()
                -(double)PlayerHistoryMatchInfoList2.get(i).getFreeThrowShot()+(double)PlayerHistoryMatchInfoList2.get(i).getFreeThrowHit())
                /((double)PlayerHistoryMatchInfoList2.get(i).getTime()+1)*10;
        key[2]=((double)PlayerHistoryMatchInfoList3.get(i).getScore()+0.5*(double)PlayerHistoryMatchInfoList3.get(i).getAss()
                +0.5*(double)PlayerHistoryMatchInfoList3.get(i).getTotReb()+0.5*(double)PlayerHistoryMatchInfoList3.get(i).getSteal()
                +0.5*(double)PlayerHistoryMatchInfoList3.get(i).getBlockShot()-0.5*(double)PlayerHistoryMatchInfoList3.get(i).getTurnOver()
                -(double)PlayerHistoryMatchInfoList3.get(i).getFreeThrowShot()+(double)PlayerHistoryMatchInfoList3.get(i).getFreeThrowHit())
                /((double)PlayerHistoryMatchInfoList3.get(i).getTime()+1)*10;

        System.out.println(key[0]+"keykeykey"+key[1]+"keykeykey"+key[2]);

        if(key[0]==0.0||key[1]==0.0||key[2]==0.0){
            state="维持";
        }
        else{
            if (key[0]>key[1]&&key[1]>key[2]){
                state="下滑";
            }
            else if(key[0]<key[1]&&key[1]<key[2]){
                state="上升";
            }
            else{
                if(Math.abs(key[0]-key[1])>4||Math.abs(key[0]-key[2])>4||Math.abs(key[1]-key[2])>4){
                    state="波动";
                }
                else{
                    state="维持";
                }

            }
        }



        return state;
    }

    private int getIndexByPlayerId(int playerId, ArrayList<PlayerHistoryMatchInfo> list){
        int index=0;
        for(index=0; index<list.size(); index++){
            if (list.get(index).getPlayerId()==playerId){
                return index;
            }
        }
        return index;
    }


    /**
     * 更新界面
     */
    private void setView() {
        CoachTrainAdapter adapter = new CoachTrainAdapter(mContext,playerTrainingInfoList);

        listView.setAdapter(adapter);

        textView.setText(" 球员名称：           上场时间及状态");


    }

}
