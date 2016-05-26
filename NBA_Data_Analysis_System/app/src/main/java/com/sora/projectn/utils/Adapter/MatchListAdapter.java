package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.MatchInfo;

import java.util.List;

/**
 * Created by qhy on 2016/5/4.
 */
public class MatchListAdapter extends BaseAdapter {

    private List<MatchInfo> matches;
    private LayoutInflater inflater;

    public MatchListAdapter(List<MatchInfo> matches,Context context){
        this.matches = matches;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int position) {
        return matches.get(position);
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
            view = inflater.inflate(R.layout.item_matchlist, null);
            vh = new ViewHolder(view);
            view.setTag(vh);

        }
        vh.hometeam.setText(matches.get(position).getHomeTeam());
        vh.matchscore.setText(matches.get(position).getHomeScore()+":"+matches.get(position).getVisitingScore());
        vh.visitteam.setText(matches.get(position).getVisitingTeam());


        return view;
    }


    private class ViewHolder {
        TextView hometeam;
        TextView matchscore;
        TextView visitteam;
        ViewHolder(View view){
            hometeam = (TextView)view.findViewById(R.id.hometeam);
            matchscore = (TextView)view.findViewById(R.id.match_score);
            visitteam = (TextView)view.findViewById(R.id.visitteam);
        }
    }

}
