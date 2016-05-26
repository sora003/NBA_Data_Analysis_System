package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.projectn.R;
import com.sora.projectn.utils.Adapter.PlayerRankAdapter;
import com.sora.projectn.utils.MyListView;
import com.sora.projectn.utils.beans.PlayerRankInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerRankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerRankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<PlayerRankInfo> playerRanks;
    private List<PlayerRankInfo> rankList1 = new ArrayList<>(), rankList2 = new ArrayList<>(), rankList3 = new ArrayList<>(), rankList4 = new ArrayList<>();//得分篮板,助攻,抢断
    private MyListView rank1,rank2,rank3,rank4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PlayerRankFragment() {
        // Required empty public constructor
    }

    public PlayerRankFragment(List<PlayerRankInfo> playerRanks){
        this.playerRanks = playerRanks;
        initdata();
    }

    private void initdata(){
        for(PlayerRankInfo player: playerRanks){
            switch (player.getType()){
                case "0":
                    rankList1.add(player);
                    break;
                case "1":
                    rankList2.add(player);
                    break;
                case "2":
                    rankList3.add(player);
                    break;
                case "3":
                    rankList4.add(player);
                    break;
            }
        }
        Collections.sort(rankList1, new Comparator<PlayerRankInfo>() {
            @Override
            public int compare(PlayerRankInfo lhs, PlayerRankInfo rhs) {
                if (Double.parseDouble(lhs.getSeasonData()) < Double.parseDouble(rhs.getSeasonData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(rankList2, new Comparator<PlayerRankInfo>() {
            @Override
            public int compare(PlayerRankInfo lhs, PlayerRankInfo rhs) {
                if (Double.parseDouble(lhs.getSeasonData()) < Double.parseDouble(rhs.getSeasonData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(rankList3, new Comparator<PlayerRankInfo>() {
            @Override
            public int compare(PlayerRankInfo lhs, PlayerRankInfo rhs) {
                if (Double.parseDouble(lhs.getSeasonData()) < Double.parseDouble(rhs.getSeasonData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(rankList4, new Comparator<PlayerRankInfo>() {
            @Override
            public int compare(PlayerRankInfo lhs, PlayerRankInfo rhs) {
                if (Double.parseDouble(lhs.getSeasonData()) < Double.parseDouble(rhs.getSeasonData())) {
                    return 1;
                } else return -1;
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerRankFragment newInstance(String param1, String param2) {
        PlayerRankFragment fragment = new PlayerRankFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_rank, container, false);
        rank1 = (MyListView)view.findViewById(R.id.player_rank_pts);
        rank2 = (MyListView)view.findViewById(R.id.player_rank_2);
        rank3 = (MyListView)view.findViewById(R.id.player_rank_3);
        rank4 = (MyListView)view.findViewById(R.id.player_rank_4);
        PlayerRankAdapter adapter1 = new PlayerRankAdapter(rankList1,getContext());
        PlayerRankAdapter adapter2 = new PlayerRankAdapter(rankList2,getContext());
        PlayerRankAdapter adapter3 = new PlayerRankAdapter(rankList3,getContext());
        PlayerRankAdapter adapter4 = new PlayerRankAdapter(rankList4,getContext());
        rank1.setAdapter(adapter1);
        rank2.setAdapter(adapter2);
        rank3.setAdapter(adapter3);
        rank4.setAdapter(adapter4);
        return view;
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
