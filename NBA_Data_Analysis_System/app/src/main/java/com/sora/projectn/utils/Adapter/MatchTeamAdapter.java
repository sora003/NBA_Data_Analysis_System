package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.TeamMatchInfo;

import java.util.List;

/**
 * Created by qhy on 2016/4/26.
 */
public class MatchTeamAdapter extends BaseAdapter {

    private List<TeamMatchInfo> match_team_infos;
    private LayoutInflater inflater;
    public MatchTeamAdapter(List<TeamMatchInfo> match_team_infos ,Context context){
        this.match_team_infos = match_team_infos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return match_team_infos.size();
    }

    @Override
    public Object getItem(int position) {
        return match_team_infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // listview每次得到一个item，都要view去绘制，通过getView方法得到view
        // position为item的序号
        ViewHolder vh;
        View view;
        if (convertView != null) {
            view = convertView;
            vh = (ViewHolder) convertView.getTag();
            // 使用缓存的view,节约内存
            // 当listview的item过多时，拖动会遮住一部分item，被遮住的item的view就是convertView保存着。
            // 当滚动条回到之前被遮住的item时，直接使用convertView，而不必再去new view()

        } else {
//            view = super.getView(position, convertView, parent);
            view = inflater.inflate(R.layout.item_matchteaminfo, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }
        vh.team_two.setText(String.valueOf(Integer.parseInt(match_team_infos.get(position).getTwoHit())*2));
        vh.team_three.setText(String.valueOf(Integer.parseInt(match_team_infos.get(position).getThreeHit())*3));
        //现在只有ID
        vh.team_name.setText(match_team_infos.get(position).getName());
        vh.team_ifHome.setText(match_team_infos.get(position).getIfHome().equals("1")?"是":"否");
        vh.team_foul.setText(match_team_infos.get(position).getFoul());
        vh.team_blockshot.setText(match_team_infos.get(position).getBlockShot());
        vh.team_ass.setText(match_team_infos.get(position).getAss());
        vh.team_turnover.setText(match_team_infos.get(position).getTurnOver());
        vh.team_reb.setText(match_team_infos.get(position).getTotReb());
        vh.team_score.setText(match_team_infos.get(position).getScore());
        vh.team_steal.setText(match_team_infos.get(position).getSteal());



        return view;
    }

    private class ViewHolder {

        TextView team_name,team_ifHome,team_score,team_two,team_three,team_reb,team_ass,team_steal,team_blockshot,team_turnover,team_foul;

        ViewHolder(View view){
            team_ass = (TextView)view.findViewById(R.id.match_team_ass);
            team_blockshot = (TextView)view.findViewById(R.id.match_team_blockshot);
            team_foul =(TextView) view.findViewById(R.id.match_team_foul);
            team_ifHome =(TextView) view.findViewById(R.id.match_team_ifhome);
            team_name = (TextView)view.findViewById(R.id.match_team_name);
            team_reb = (TextView)view.findViewById(R.id.match_team_reb);
            team_score = (TextView)view.findViewById(R.id.match_team_score);
            team_steal =(TextView) view.findViewById(R.id.match_team_steal);
            team_three =(TextView) view.findViewById(R.id.match_team_three);
            team_turnover = (TextView)view.findViewById(R.id.match_team_turnover);
            team_two = (TextView)view.findViewById(R.id.match_team_two);


        }
    }

}