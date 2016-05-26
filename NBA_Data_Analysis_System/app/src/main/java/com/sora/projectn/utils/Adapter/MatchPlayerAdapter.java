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
 * Created by qhy on 2016/4/26.
 */
public class MatchPlayerAdapter extends BaseAdapter {

    private List<Map<String,String>> match_player_infos;
    private LayoutInflater inflater;
    public MatchPlayerAdapter(List<Map<String,String>> match_player_infos ,Context context){
        this.match_player_infos = match_player_infos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return match_player_infos.size();
    }

    @Override
    public Object getItem(int position) {
        return match_player_infos.get(position);
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
            view = inflater.inflate(R.layout.item_matchplayerinfo, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }
        vh.player_two.setText(String.valueOf(Integer.parseInt(match_player_infos.get(position).get("twoHit"))*2));
        vh.player_three.setText(String.valueOf(Integer.parseInt(match_player_infos.get(position).get("threeHit"))*2));
        vh.player_name.setText(match_player_infos.get(position).get("playerName"));
        vh.player_foul.setText(match_player_infos.get(position).get("foul"));
        vh.player_blockshot.setText(match_player_infos.get(position).get("blockShot"));
        vh.player_ass.setText(match_player_infos.get(position).get("ass"));
        vh.player_turnover.setText(match_player_infos.get(position).get("turnOver"));
        vh.player_reb.setText(match_player_infos.get(position).get("reb"));
        vh.player_score.setText(match_player_infos.get(position).get("score"));
        vh.player_steal.setText(match_player_infos.get(position).get("steal"));


        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色

        view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同

        return view;
    }

    private class ViewHolder {

        TextView player_name,player_score,player_two,player_three,player_reb,player_ass,player_steal,player_blockshot,player_turnover,player_foul;

        ViewHolder(View view){
            player_ass = (TextView)view.findViewById(R.id.match_player_ass);
            player_blockshot = (TextView)view.findViewById(R.id.match_player_blockshot);
            player_foul =(TextView) view.findViewById(R.id.match_player_foul);
            player_name = (TextView)view.findViewById(R.id.match_player_name);
            player_reb = (TextView)view.findViewById(R.id.match_player_reb);
            player_score = (TextView)view.findViewById(R.id.match_player_score);
            player_steal =(TextView) view.findViewById(R.id.match_player_steal);
            player_three =(TextView) view.findViewById(R.id.match_player_three);
            player_turnover = (TextView)view.findViewById(R.id.match_player_turnover);
            player_two = (TextView)view.findViewById(R.id.match_player_two);


        }
    }

}