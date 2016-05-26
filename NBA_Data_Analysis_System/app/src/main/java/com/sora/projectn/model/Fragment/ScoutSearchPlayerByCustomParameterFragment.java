package com.sora.projectn.model.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sora.projectn.R;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.MyLexical;
import com.sora.projectn.utils.MySemantic;
import com.sora.projectn.utils.MySyntax;
import com.sora.projectn.utils.SearchPlayerAdapter;
import com.sora.projectn.utils.beans.SearchPlayerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScoutSearchPlayerByCustomParameterFragment extends Fragment {

    private Context mContext;

    private List<SearchPlayerInfo> list = new ArrayList<>();

    private ListView listView;
    private View  fView;
    private EditText customFunction;
    private Button btn_scout_search_player_by_custom_para;


    private static final int ALREADY_GET_PLAYER_DATA_1 = 0x01;
    private static final int ALREADY_GET_PLAYER_DATA_2 = 0x02;
    private ArrayList<SearchPlayerInfo> players = new ArrayList<SearchPlayerInfo>(500);


    /**
     * Handler
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALREADY_GET_PLAYER_DATA_1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadAllPlayerAdvancedInfo();
                        }
                    }).start();

                    players=(ArrayList<SearchPlayerInfo>)msg.obj;


                    break;

                case ALREADY_GET_PLAYER_DATA_2:
                    players=(ArrayList<SearchPlayerInfo>)msg.obj;
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scout_search_player_by_custom_parameter, container, false);

        fView = view;
        initView();

        btn_scout_search_player_by_custom_para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=customFunction.getText().toString();
                //最末尾加一个空作为结束
                input=input+" ";
                String token = (new MyLexical()).getToken(input);
                if(token.contains("error")) {
                    new AlertDialog.Builder(mContext).setTitle("提醒").setMessage("存在不合法字符").setNegativeButton("确定", null).show();
                }else{
                    String parseResult=(new MySyntax()).parseSentence(token);
                    if(parseResult.contains("ERROR")){
                        new AlertDialog.Builder(mContext).setTitle("提醒").setMessage("语法错误").setNegativeButton("确定", null).show();
                    }
                    else{
                        generateKeysForAllPlayer(getSentenceByToken(token));
                        list = getPlayerListOrderedBtKey();

                        setView();
                    }
                }
            }
        });

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
//                    Toast.makeText, "无法获取数据", Toast.LENGTH_SHORT).show();
//                    Toast.makeText((SearchPlayerActivity.this,Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
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

        listView = (ListView) fView.findViewById(R.id.lv_search_player_by_custom_para);

        btn_scout_search_player_by_custom_para = (Button) fView.findViewById(R.id.btn_scout_search_player_by_custom_para);
        customFunction = (EditText) fView.findViewById(R.id.customFunction_scout);
    }


    /**
     * 更新界面
     */
    private void setView() {
        SearchPlayerAdapter adapter = new SearchPlayerAdapter(mContext,list);

        listView.setAdapter(adapter);

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

            for(int i=0; i< players.size();i++){
                System.out.println(players.get(i).getScore());
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;


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

//    private void loadAllPlayerInfo(){
//
//        for(int i=0; i<players.size(); i++){
//            loadPlayerInfoByPlayerId(players.get(i).getPlayerId());
//        }
//
//        for(int i=0; i<players.size(); i++){
//            System.out.println(players.get(i).getBlockShot()+"xxXX");
//        }
//
//    }

    private SearchPlayerInfo getPlayerByPlayerId(int playerId){
        int i;
        for (i=0; i<players.size(); i++){
            if(players.get(i).getPlayerId()==playerId){
                return players.get(i);
            }
        }
        return null;
    }

    private ArrayList<SearchPlayerInfo> getPlayerListOrderedBtKey(){
        for(int i=0; i<players.size()-1;i++){
            for(int j=i+1; j<players.size();j++){
                if(players.get(i).getKey()<players.get(j).getKey()){
                    SearchPlayerInfo temp;
                    temp=players.get(i);
                    players.set(i,players.get(j));
                    players.set(j,temp);
                }
            }
        }

        ArrayList<SearchPlayerInfo> playerList = new ArrayList<SearchPlayerInfo>(50);
        for(int i=0; i<50; i++){
            SearchPlayerInfo tempPlayer;
            System.out.println(players.size()+" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            tempPlayer=players.get(i);
            playerList.add(tempPlayer);
            System.out.println("前50" + tempPlayer.getScore() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return playerList;
    }

    private ArrayList<String> getSentenceByToken(String token){

        ArrayList<String> sentence = new ArrayList<String>();


        String[] temp1;
        temp1 = token.split(";");//每行分别保存到单独字符串中

        //提取token中有用部分
        for(int i=0; i<temp1.length; i++) {
            String str = new String("");

            char c = temp1[i].charAt(1);
            if (c == 'o') {//如果是操作符 则取第四个
                str = "" + temp1[i].charAt(4);

            } else if (c == 'n') {//如果是数字，则返回num
                int length= temp1[i].length();
                str = temp1[i].substring(11,length-1);
            } else if (c == 'i') {
                int length= temp1[i].length();
                str = temp1[i].substring(10,length-1);
            } else {
                //应该不会有其他情况了
            }
            sentence.add(str);

        }
        return sentence;
    }

    private boolean generateKeyForAPlayer(ArrayList<String> sentence, SearchPlayerInfo player){
        String expression="";
        for(int i=0; i<sentence.size(); i++){
            if(sentence.get(i).contains("a")||sentence.get(i).contains("b")||sentence.get(i).contains("c")||sentence.get(i).contains("d")||
                    sentence.get(i).contains("e")||sentence.get(i).contains("f")||sentence.get(i).contains("g")||sentence.get(i).contains("h")||
                    sentence.get(i).contains("i")||sentence.get(i).contains("j")||sentence.get(i).contains("k")||sentence.get(i).contains("l")||
                    sentence.get(i).contains("m")||sentence.get(i).contains("n")||sentence.get(i).contains("o")||sentence.get(i).contains("p")||
                    sentence.get(i).contains("q")||sentence.get(i).contains("r")||sentence.get(i).contains("s")||sentence.get(i).contains("t")||
                    sentence.get(i).contains("u")||sentence.get(i).contains("v")||sentence.get(i).contains("w")||
                    sentence.get(i).contains("x")||sentence.get(i).contains("y")||sentence.get(i).contains("z")){
                //数据类型是int
                String parameter = sentence.get(i);
                if(parameter.equals("isFirst")||parameter.equals("toTalMatches")||parameter.equals("doubleDouble")||parameter.equals("tripleDouble")){

                    double value=(double)((int) player.getInfoByParameter(parameter));
                    expression=expression+value;
                }
                //数据类型是double,本来该写else if的
                else{
                    double value=(double)player.getInfoByParameter(parameter);
                    expression=expression+value;
                }
            }
            else{
                expression=expression+sentence.get(i);
            }

        }

        double key = (new MySemantic()).semanticParsing(expression);
        int i;
        for(i=0; i<players.size(); i++){
            if(players.get(i).getPlayerId()==player.getPlayerId()){
                break;
            }
        }

        players.get(i).setKey(key);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx"+key+"CCCCCCCCCCCCCC"+i);
        return true;
    }

    private boolean generateKeysForAllPlayer(ArrayList<String> sentence){

        for(int i=0; i<players.size(); i++){
            generateKeyForAPlayer(sentence, players.get(i));
        }

        return true;
    }
}
