package com.sora.projectn.model.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.Adapter.SearchPlayerActivityAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.beans.SearchPlayerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class SearchPlayerActivity extends Activity {

    private ListView lvSearchPlayerActivity;
    private Context mContext;
    private SearchView svSearchPlayer;

    private ArrayList<SearchPlayerInfo> players = new ArrayList<SearchPlayerInfo>(500);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_player);

        mContext = this;

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (!loadPlayerData()){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }
            }
        }).start();


        lvSearchPlayerActivity = (ListView)findViewById(R.id.lv_search_player_activity);

        lvSearchPlayerActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle b = new Bundle();
                b.putInt("id", players.get(position).getPlayerId());

                Intent intent = new Intent(SearchPlayerActivity.this, PlayerInfoActivity.class);
                intent.putExtra("bundleData",b);
                startActivity(intent);
            }
        });

        svSearchPlayer = (SearchView)findViewById(R.id.search_player_search_view);

        svSearchPlayer.onActionViewExpanded();
        svSearchPlayer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                final ArrayList<SearchPlayerInfo> result = new ArrayList<SearchPlayerInfo>();
                for (int i=0 ; i<players.size(); i++){
                    if (players.get(i).getPlayerName().contains(query)){
                        result.add(players.get(i));
                    }
                }

                SearchPlayerActivityAdapter adapter = new SearchPlayerActivityAdapter(result,mContext);
                lvSearchPlayerActivity.setAdapter(adapter);

                lvSearchPlayerActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Bundle b = new Bundle();
                        b.putInt("id", result.get(position).getPlayerId());

                        Intent intent = new Intent(SearchPlayerActivity.this, PlayerInfoActivity.class);
                        intent.putExtra("bundleData",b);
                        startActivity(intent);
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<SearchPlayerInfo> result = new ArrayList<SearchPlayerInfo>();
                for (int i=0 ; i<players.size(); i++){
                    if (players.get(i).getPlayerName().contains(newText)){
                        result.add(players.get(i));
                    }
                }

                SearchPlayerActivityAdapter adapter = new SearchPlayerActivityAdapter(result,mContext);
                lvSearchPlayerActivity.setAdapter(adapter);

                lvSearchPlayerActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Bundle b = new Bundle();
                        b.putInt("id", result.get(position).getPlayerId());
                        System.out.println("CCCCAAAACCCCCCCCCAAAAA"+id);

                        Intent intent = new Intent(SearchPlayerActivity.this, PlayerInfoActivity.class);
                        intent.putExtra("bundleData", b);
                        startActivity(intent);
                    }
                });


                return true;
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
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
        SearchPlayerActivityAdapter adapter = new SearchPlayerActivityAdapter(players,mContext);
        lvSearchPlayerActivity.setAdapter(adapter);
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
                        player.setTeamName(JO.getString("teamName"));
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

        sort();

        return true;
    }

    private void sort(){
        for(int i=0; i<players.size()-1; i++){
            for(int j=i+1; j<players.size(); j++){
                if((players.get(i).getTeamName()).compareTo(players.get(j).getTeamName())>0){
                    SearchPlayerInfo temp = players.get(i);
                    players.set(i,players.get(j));
                    players.set(j,temp);
                }
            }
        }
    }


}
