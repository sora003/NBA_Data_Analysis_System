package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.TeamRankInfo;

import java.util.List;

/**
 * Created by qhy on 2016/4/19.
 * Unfinished
 */
public class TeamRankAdapter extends BaseAdapter {

    private List<TeamRankInfo> rank;
    private LayoutInflater inflater;
    public TeamRankAdapter(List<TeamRankInfo> rank ,Context context){
        inflater = LayoutInflater.from(context);
        this.rank = rank;
    }

    @Override
    public int getCount() {
        return rank.size();
    }

    @Override
    public Object getItem(int position) {
        return rank.get(position);
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
            view = inflater.inflate(R.layout.item_teamrank, null);
            vh = new ViewHolder(view);
            view.setTag(vh);

        }
        vh.rank.setText(rank.get(position).getRank());
        vh.name.setText(rank.get(position).getName());
        vh.winorlose.setText(rank.get(position).getWins()+"/"+rank.get(position).getLoses());
        vh.gamebehind.setText(rank.get(position).getGamesBehind());


        return view;
    }

    private class ViewHolder {
        TextView rank;
        TextView name;
        TextView winorlose;
        TextView gamebehind;
        ViewHolder(View view){
            rank = (TextView)view.findViewById(R.id.team_rank);
            name = (TextView)view.findViewById(R.id.team_rank_name);
            winorlose = (TextView)view.findViewById(R.id.team_win_lose);
            gamebehind = (TextView)view.findViewById(R.id.team_field_difference);
        }
    }

}