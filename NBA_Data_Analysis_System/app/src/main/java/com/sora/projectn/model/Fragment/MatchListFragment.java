package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Adapter.MainCardsAdapter;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.beans.MatchInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int teamId = 0;

    private Map<String,List<MatchInfo>> latestMatchList = new TreeMap<>();
    private ListView mainCards;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MatchListFragment() {
        // Required empty public constructor
    }

    public MatchListFragment(int teamId){
        this.teamId = teamId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchListFragment newInstance(String param1, String param2) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private List<MatchInfo> getLatestMatches(){
        String jsonString;

        jsonString = ACache.get(getContext()).getAsString("getLatestMatchList" );

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.getLatestMatchList);

            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(getContext()).put("getLatestMatchList" ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        List<MatchInfo> matches = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                MatchInfo temp = new MatchInfo();
                JSONObject obj = array.getJSONObject(i);
                String id = obj.getString("id");
                String vId = obj.getString("vId");
                String visitingTeam = obj.getString("visitingTeam");
                String hId = obj.getString("hId");
                String homeTeam = obj.getString("homeTeam");
                String visitingScore = obj.getString("visitingScore");
                String homeScore = obj.getString("homeScore");
                String type = obj.getString("type");
                String date = obj.getString("date");
                String year = obj.getString("year");
                String time = obj.getString("time");

                temp.setId(id);
                temp.setvId(vId);
                temp.setVisitingTeam(visitingTeam);
                temp.sethId(hId);
                temp.setHomeTeam(homeTeam);
                temp.setVisitingScore(visitingScore);
                temp.setHomeScore(homeScore);
                temp.setType(type);
                temp.setDate(date);
                temp.setYear(year);
                temp.setTime(time);

                matches.add(temp);
            }



        }catch (NullPointerException e){
            handler.sendEmptyMessage(Consts.RES_ERROR);
            handler.sendEmptyMessage(Consts.SET_VIEW);
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally
        {
            return matches;
        }
    }

    private List<MatchInfo> getTeamMatches(){

        String jsonString;

        jsonString = ACache.get(getContext()).getAsString("getTeamMatchList" + teamId);

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.getTeamMatchList+"?teamId="+String.valueOf(teamId));

            if (jsonString == null){
                handler.sendEmptyMessage(Consts.RES_ERROR);
            }

            ACache.get(getContext()).put("getTeamMatchList" + teamId,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        List<MatchInfo> matches = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                MatchInfo temp = new MatchInfo();
                JSONObject obj = array.getJSONObject(i);
                String id = obj.getString("id");
                String vId = obj.getString("vId");
                String visitingTeam = obj.getString("visitingTeam");
                String hId = obj.getString("hId");
                String homeTeam = obj.getString("homeTeam");
                String visitingScore = obj.getString("visitingScore");
                String homeScore = obj.getString("homeScore");
                String type = obj.getString("type");
                String date = obj.getString("date");
                String year = obj.getString("year");
                String time = obj.getString("time");

                temp.setId(id);
                temp.setvId(vId);
                temp.setVisitingTeam(visitingTeam);
                temp.sethId(hId);
                temp.setHomeTeam(homeTeam);
                temp.setVisitingScore(visitingScore);
                temp.setHomeScore(homeScore);
                temp.setType(type);
                temp.setDate(date);
                temp.setYear(year);
                temp.setTime(time);

                matches.add(temp);
            }



        }catch (NullPointerException e){
            handler.sendEmptyMessage(Consts.RES_ERROR);
            handler.sendEmptyMessage(Consts.SET_VIEW);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally{
            return matches;
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Consts.SET_VIEW:
                    initView();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(getContext(), Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initData(List<MatchInfo> matches){
        //TODO 这里对数据进行接收和处理
        latestMatchList = new TreeMap<>();
        for(MatchInfo m:matches){
            if(latestMatchList.get(m.getDate()) != null){
                latestMatchList.get(m.getDate()).add(m);
            }else{
                latestMatchList.put(m.getDate(), new ArrayList<MatchInfo>());
                latestMatchList.get(m.getDate()).add(m);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchlist, container, false);
        mainCards = (ListView)view.findViewById(R.id.main_cards);
        parseIntent();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(teamId == 0){
                    initData(getLatestMatches());
                    ACache a = ACache.get(getContext());
                    Map.Entry<String,List<MatchInfo>> entry = ((TreeMap<String,List<MatchInfo>>)latestMatchList).lastEntry();
                    int year = Integer.parseInt(entry.getValue().get(0).getYear());
                    int monthOfYear = Integer.parseInt(entry.getKey().split("月")[0]);
                    int dayOfMonth = Integer.parseInt(entry.getKey().split("月")[1].split("日")[0]);
                    String date = String.valueOf(year) + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth);

                    a.put("LatestMatchDate", date, ACache.TEST_TIME);
                }
                else{
                    initData(getTeamMatches());
                }

                handler.sendEmptyMessage(Consts.SET_VIEW);
            }
        }).start();
        return view;
    }

    private void parseIntent(){

        Bundle bundle = this.getArguments();
        try{
           teamId = bundle.getInt("id");
        }
        catch(NullPointerException e){

        }

    }

    private void initView(){

        MainCardsAdapter mcadapter = new MainCardsAdapter(latestMatchList,getContext());
        mainCards.setAdapter(mcadapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
