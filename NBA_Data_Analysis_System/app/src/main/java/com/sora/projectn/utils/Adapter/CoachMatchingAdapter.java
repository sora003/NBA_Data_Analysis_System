package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.PlayerMatchingInfo;

import java.util.List;

/**
 * Created by lenovo on 2016/5/1.
 */
public class CoachMatchingAdapter extends BaseAdapter {


    private List<PlayerMatchingInfo> list;
    LayoutInflater inflater;
    private int type=0;//数值代表 n分命中率的Adapter

    public CoachMatchingAdapter(Context context, List<PlayerMatchingInfo> list, int type) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view = null;
        if (convertView == null || convertView.getTag() == null){
            view = inflater.inflate(R.layout.item_coachtrain,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PlayerMatchingInfo playerMatchingInfo = (PlayerMatchingInfo) getItem(position);

        if(type==1){
            viewHolder.item_entry.setText(playerMatchingInfo.getPlayerName());
            viewHolder.item_data.setText(playerMatchingInfo.getFreeThrowRate()+"%，"+playerMatchingInfo.getState());
        }
        else if(type==2){
            viewHolder.item_entry.setText(playerMatchingInfo.getPlayerName());
            viewHolder.item_data.setText(playerMatchingInfo.getTwoRate()+"%，"+playerMatchingInfo.getState());
        }
        else if(type==3){
            viewHolder.item_entry.setText(playerMatchingInfo.getPlayerName());
            viewHolder.item_data.setText(playerMatchingInfo.getThreeRate()+"%，"+playerMatchingInfo.getState());
        }


        return view;
    }

    class ViewHolder{
        TextView item_entry;
        TextView item_data;

        public ViewHolder(View view) {
            this.item_entry = (TextView) view.findViewById(R.id.item_coach_train_entry);
            this.item_data = (TextView) view.findViewById(R.id.item_coach_train_data);
        }
    }

}
