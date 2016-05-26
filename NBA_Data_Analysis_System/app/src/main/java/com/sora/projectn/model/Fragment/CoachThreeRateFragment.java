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
import com.sora.projectn.utils.CoachMatchingAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.beans.PlayerHistoryMatchInfo;
import com.sora.projectn.utils.beans.PlayerMatchingInfo;
import com.sora.projectn.utils.beans.PlayerTrainingInfo;
import com.sora.projectn.utils.beans.SearchPlayerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class CoachThreeRateFragment extends Fragment {
    private ArrayList<SearchPlayerInfo> players = new ArrayList<>();
    private ArrayList<SearchPlayerInfo> teamPlayers = new ArrayList<>();
    private ArrayList<PlayerTrainingInfo> trainingInfoList = new ArrayList<>();
    private ArrayList<PlayerMatchingInfo> playerRecommend = new ArrayList<>();
    private int teamId;
    private Context mContext;

    private static final int ALREADY_GET_PLAYER_DATA_1 = 0x01;
    private static final int ALREADY_GET_PLAYER_DATA_2 = 0x02;
    private static final int ALREADY_GET_PLAYER_LIST = 0x03;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_1 = 0x04;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_2 = 0x05;
    private static final int ALREADY_GET_PLAYER_MATCH_INFO_3 = 0x06;
    private static final int SET_VIEW = 0x06;

    private ListView listView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_three_rate,container,false);

        //获取上下文环境
        mContext = this.getActivity();


        listView = (ListView) view.findViewById(R.id.lv_playerThreeRate);

        textView = (TextView) view.findViewById(R.id.tv_playerThreeRate);

        parseIntent();


        /**
         * 获取球员数据
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(loadPlayerData()){
                    Message msg = new Message();
                    msg.what=ALREADY_GET_PLAYER_DATA_1;
                    msg.obj=players;
                    handler.sendMessage(msg);
                }
                else{

                }

            }
        }).start();



        return view;

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

                case ALREADY_GET_PLAYER_DATA_1:
                    players=(ArrayList<SearchPlayerInfo>)msg.obj;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadAllPlayerAdvancedInfo();
                            Message msg = new Message();
                            msg.what=ALREADY_GET_PLAYER_DATA_2;
                            msg.obj=players;
                            handler.sendMessage(msg);
                        }
                    }).start();


                    break;



                case ALREADY_GET_PLAYER_DATA_2:
                    players=(ArrayList<SearchPlayerInfo>)msg.obj;
                    for(int i=0; i<players.size(); i++){
                        System.out.println(players.get(i).getThreeRate()+"#########2222222222222");
                    }

                    //获取球队队员id
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
                                            int playerId=JO.getInt("id");
                                            SearchPlayerInfo searchPlayerInfo = getPlayerByPlayerId(playerId);

                                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "+searchPlayerInfo.getPlayerId()+"   "+searchPlayerInfo.getThreeRate());

                                            teamPlayers.add(searchPlayerInfo);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }
                                }
                                Message msg = new Message();
                                msg.what=ALREADY_GET_PLAYER_LIST;
                                msg.obj=teamPlayers;
                                handler.sendMessage(msg);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                break;

                case ALREADY_GET_PLAYER_LIST:
                    teamPlayers=(ArrayList<SearchPlayerInfo>)msg.obj;
                    for(int i=0; i<teamPlayers.size(); i++){
                        System.out.println(teamPlayers.get(i).getPlayerName()+"#########333333333333");
                    }

                    trainingInfoList=((MainActivity)mContext).getPlayerTrainingInfoList();
                    System.out.println(trainingInfoList.size()+"CCCCCCCCCCCCCCCCCCCCCCCCCCCCXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    for(int i=0; i<trainingInfoList.size();i++){
                        System.out.println("#################33333333333"+trainingInfoList.get(i).getState());
                    }

                    for(int i=0; i<teamPlayers.size(); i++){
                        PlayerMatchingInfo playerMatchingInfo = new PlayerMatchingInfo();
                        playerMatchingInfo.setPlayerId(teamPlayers.get(i).getPlayerId());
                        playerMatchingInfo.setPlayerName(teamPlayers.get(i).getPlayerName());
                        playerMatchingInfo.setThreeRate(teamPlayers.get(i).getThreeRate());
                        int j;
                        for(j=0; j<teamPlayers.size(); j++){
                            if (trainingInfoList.get(j).getPlayerId()==teamPlayers.get(i).getPlayerId()){
                                break;
                            }
                        }
                        playerMatchingInfo.setState(trainingInfoList.get(j).getState());
                        playerRecommend.add(playerMatchingInfo);
                    }

                    for(int i=0; i<playerRecommend.size()-1;i++){
                        for(int j=i+1; j<playerRecommend.size();j++){
                            if(playerRecommend.get(i).getThreeRate()<playerRecommend.get(j).getThreeRate()){
                                PlayerMatchingInfo temp;
                                temp=playerRecommend.get(i);
                                playerRecommend.set(i,playerRecommend.get(j));
                                playerRecommend.set(j,temp);
                            }
                        }
                    }


                    for(int i=0; i<teamPlayers.size(); i++){
                        System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT  "+teamPlayers.get(i).getPlayerId()+"   "+teamPlayers.get(i).getThreeRate());
                    }

                    for(int i=0; i<playerRecommend.size(); i++){
                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  "+playerRecommend.get(i).getPlayerId()+"   "+playerRecommend.get(i).getThreeRate());
                    }
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");


                    setView();

                    break;
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
        CoachMatchingAdapter adapter = new CoachMatchingAdapter(mContext,playerRecommend,3);

        listView.setAdapter(adapter);

        textView.setText("球员名称：           三分命中率及状态");


    }

    private boolean loadPlayerData() {

//        String jsonString= GetHttpResponse.getHttpResponse("http://172.17.142.40:8080/NBADataSystem/getTeamPlayerList.do?teamId=10");

        System.out.println(">>>>>>>>>>LOAD PLAYER");
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getTotalPlayerSeasonStatistics.do").openStream(), "utf-8"));

            String line;
            StringBuffer content = new StringBuffer();

            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();

            if (content != null) {
                try {
                    JSONArray jsonArray = new JSONArray(content.toString());
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"+content.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject JO = jsonArray.getJSONObject(i);
                        SearchPlayerInfo player = new SearchPlayerInfo();
                        player.setPlayerId(JO.getInt("playerId"));
                        player.setPlayerName(JO.getString("playerName"));
                        player.setIsFirst(JO.getInt("isFirst"));
                        player.setTotalMatches(JO.getInt("totalMatches"));
                        player.setTime(JO.getDouble("time"));
                        player.setTwoHit(JO.getDouble("twoHit"));
                        player.setThreeHit(JO.getDouble("threeHit"));
                        player.setTwoShot(JO.getDouble("twoShot"));
                        player.setThreeShot(JO.getDouble("threeShot"));
                        player.setFreeThrowHit(JO.getDouble("freeThrowHit"));
                        player.setFreeThrowShot(JO.getDouble("freeThrowShot"));
                        player.setOffReb(JO.getDouble("offReb"));
                        player.setDefReb(JO.getDouble("defReb"));
                        player.setTotReb(JO.getDouble("totReb"));
                        player.setAss(JO.getDouble("ass"));
                        player.setSteal(JO.getDouble("steal"));
                        player.setBlockShot(JO.getDouble("blockShot"));
                        player.setTurnOver(JO.getDouble("turnOver"));
                        player.setFoul(JO.getDouble("foul"));
                        player.setScore(JO.getDouble("score"));
                        players.add(player);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } finally {

                }

            }




        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;


    }


    private SearchPlayerInfo getPlayerByPlayerId(int playerId){
        int i;
        for (i=0; i<players.size(); i++){
            if(players.get(i).getPlayerId()==playerId){
                return players.get(i);
            }
        }
        return null;
    }

    private void loadAllPlayerAdvancedInfo(){

        System.out.println(">>>>>>>>>>LOAD PLAYER ADVANCED DATA");
        try {

            BufferedReader br2 = new BufferedReader(new InputStreamReader(new URL(Consts.url+"getTotalPlayerSeasonAdvancedStatistics.do").openStream(), "utf-8"));

            String line = null;
            StringBuffer content = new StringBuffer();

            while ((line = br2.readLine()) != null) {
                content.append(line);
            }
            br2.close();

            if (content != null) {
                try {
                    JSONArray jsonArray = new JSONArray(content.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {////////////////////////you wen ti!!!!!!!!!!!
                        JSONObject JO = jsonArray.getJSONObject(i);
                        SearchPlayerInfo tempPlayer = new SearchPlayerInfo();
                        tempPlayer.setPlayerId(JO.getInt("playerId"));

                        SearchPlayerInfo player = getPlayerByPlayerId(tempPlayer.getPlayerId());

                        player.setTwoRate(JO.getInt("twoRate"));
                        player.setThreeRate(JO.getInt("threeRate"));
                        player.setFreeThrowRate(JO.getDouble("freeThrowRate"));
                        player.setTrueRate(JO.getDouble("trueRate"));
                        player.setDoubleDouble(JO.getInt("doubleDouble"));
                        player.setTripleDouble(JO.getInt("tripleDouble"));
                        player.setPER(JO.getDouble("per"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            for(int i=0; i< players.size();i++){
                System.out.println(players.get(i).getTripleDouble());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
