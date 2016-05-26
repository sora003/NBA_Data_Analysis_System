package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;

import java.util.List;
import java.util.Map;

/**
 * Created by qhy on 2016/4/19.
 */
public class PlayerRankAdapter extends BaseAdapter{

    private List<Map<String,String>> rank;
    private LayoutInflater inflater;

    public PlayerRankAdapter(List<Map<String,String>> rank,Context context){
        this.rank = rank;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            view = inflater.inflate(R.layout.item_playerrank, null);
            vh = new ViewHolder(view);
            view.setTag(vh);

        }
        vh.name.setText(rank.get(position).get("name"));
        vh.teamname.setText(rank.get(position).get("teamName"));
        vh.data.setText(rank.get(position).get("seasonData"));

        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色

        view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同

        return view;
    }


    private class ViewHolder {
        TextView name;
        TextView teamname;
        TextView data;
        ViewHolder(View view){
            name = (TextView)view.findViewById(R.id.player_rank_name);
            teamname = (TextView)view.findViewById(R.id.player_rank_team);
            data = (TextView)view.findViewById(R.id.player_rank_data);
        }
    }

}
