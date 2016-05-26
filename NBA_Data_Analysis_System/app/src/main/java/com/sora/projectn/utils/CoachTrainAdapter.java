package com.sora.projectn.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.PlayerTrainingInfo;

import java.util.List;

/**
 * Created by lenovo on 2016/5/1.
 */
public class CoachTrainAdapter extends BaseAdapter {

    private List<PlayerTrainingInfo> list;
    LayoutInflater inflater;

    public CoachTrainAdapter(Context context, List<PlayerTrainingInfo> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
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

        PlayerTrainingInfo playerTrainingInfo = (PlayerTrainingInfo) getItem(position);

        viewHolder.item_entry.setText(playerTrainingInfo.getPlayerName());
        viewHolder.item_data.setText(playerTrainingInfo.getMatchTime()+"分钟，"+playerTrainingInfo.getState());

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
