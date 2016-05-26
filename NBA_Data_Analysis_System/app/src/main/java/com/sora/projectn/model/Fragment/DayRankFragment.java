package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.Adapter.DayRankAdapter;
import com.sora.projectn.utils.MyListView;
import com.sora.projectn.utils.beans.DayRankInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayRankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayRankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<DayRankInfo> dayranks;
    private List<DayRankInfo> ranklist1 = new ArrayList<DayRankInfo>(),ranklist2 = new ArrayList<DayRankInfo>(),ranklist3 = new ArrayList<DayRankInfo>(),ranklist4 = new ArrayList<DayRankInfo>();//得分篮板,助攻,抢断
    private MyListView rank1,rank2,rank3,rank4;
    private TextView titleMessage;
    private String date;

    private OnFragmentInteractionListener mListener;

    public DayRankFragment() {
        // Required empty public constructor
    }

    public DayRankFragment(List<DayRankInfo> dayranks,String date){
        this.dayranks = dayranks;
        this.date = date;
        initdata();
    }


    private void initdata(){
        for(DayRankInfo player:dayranks){
            switch (player.getType()){
                case "0":
                    ranklist1.add(player);
                    break;
                case "1":
                    ranklist2.add(player);
                    break;
                case "2":
                    ranklist3.add(player);
                    break;
                case "3":
                    ranklist4.add(player);
                    break;
            }
        }
        Collections.sort(ranklist1, new Comparator<DayRankInfo>() {
            @Override
            public int compare(DayRankInfo lhs, DayRankInfo rhs) {
                if (Double.parseDouble(lhs.getData()) < Double.parseDouble(rhs.getData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(ranklist2, new Comparator<DayRankInfo>() {
            @Override
            public int compare(DayRankInfo lhs, DayRankInfo rhs) {
                if (Double.parseDouble(lhs.getData()) < Double.parseDouble(rhs.getData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(ranklist3, new Comparator<DayRankInfo>() {
            @Override
            public int compare(DayRankInfo lhs, DayRankInfo rhs) {
                if (Double.parseDouble(lhs.getData()) < Double.parseDouble(rhs.getData())) {
                    return 1;
                } else return -1;
            }
        });
        Collections.sort(ranklist4, new Comparator<DayRankInfo>() {
            @Override
            public int compare(DayRankInfo lhs, DayRankInfo rhs) {
                if (Double.parseDouble(lhs.getData()) < Double.parseDouble(rhs.getData())) {
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
     * @return A new instance of fragment DayRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayRankFragment newInstance(String param1, String param2) {
        DayRankFragment fragment = new DayRankFragment();
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
        View view = inflater.inflate(R.layout.fragment_day_rank, container, false);
        rank1 = (MyListView)view.findViewById(R.id.day_rank_pts);
        rank2 = (MyListView)view.findViewById(R.id.day_rank_2);
        rank3 = (MyListView)view.findViewById(R.id.day_rank_3);
        rank4 = (MyListView)view.findViewById(R.id.day_rank_4);
        titleMessage = (TextView)view.findViewById(R.id.dayrank_titlemessage);
        DayRankAdapter adapter1 = new DayRankAdapter(ranklist1,getContext());
        DayRankAdapter adapter2 = new DayRankAdapter(ranklist2,getContext());
        DayRankAdapter adapter3 = new DayRankAdapter(ranklist3,getContext());
        DayRankAdapter adapter4 = new DayRankAdapter(ranklist4,getContext());
        rank1.setAdapter(adapter1);
        rank2.setAdapter(adapter2);
        rank3.setAdapter(adapter3);
        rank4.setAdapter(adapter4);
        titleMessage.setText("以下是" + date + "最新消息");
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
