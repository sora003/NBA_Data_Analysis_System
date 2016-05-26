package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.SearchPlayerInfo;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/5/2.
 */
public class SearchPlayerActivityAdapter extends BaseAdapter {

    private ArrayList<SearchPlayerInfo> list = new ArrayList<SearchPlayerInfo>(500);
    private Context context;

    public SearchPlayerActivityAdapter(ArrayList<SearchPlayerInfo> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_player_activity,null);
            holder = new ViewHolder();
            holder.playerNameTV = (TextView) convertView.findViewById(R.id.tv_search_player_player_name);
            holder.teamNameTV = (TextView) convertView.findViewById(R.id.tv_search_player_team_name);
            holder.playerNameTV.setText(list.get(position).getPlayerName());
            holder.teamNameTV.setText(list.get(position).getTeamName());
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            holder.playerNameTV.setText(list.get(position).getPlayerName());
            holder.teamNameTV.setText(list.get(position).getTeamName());
        }


        return convertView;
    }

    private static class ViewHolder{
        TextView playerNameTV;
        TextView teamNameTV;
    }
}
