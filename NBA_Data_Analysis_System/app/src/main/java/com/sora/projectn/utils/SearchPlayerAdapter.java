package com.sora.projectn.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.SearchPlayerInfo;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lenovo on 2016/5/13.
 */
public class SearchPlayerAdapter extends BaseAdapter {


    private List<SearchPlayerInfo> list;
    LayoutInflater inflater;

    public SearchPlayerAdapter(Context context, List<SearchPlayerInfo> list) {
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
            view = inflater.inflate(R.layout.item_teamstatics,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SearchPlayerInfo vo = (SearchPlayerInfo) getItem(position);

        DecimalFormat df = new DecimalFormat( "0.00");

        viewHolder.item_entry.setText(vo.getPlayerName());
        viewHolder.item_data.setText(df.format(vo.getKey())+"");

        return view;
    }

    class ViewHolder{
        TextView item_entry;
        TextView item_data;

        public ViewHolder(View view) {
            this.item_entry = (TextView) view.findViewById(R.id.item_teamstatics_entry);
            this.item_data = (TextView) view.findViewById(R.id.item_teamstatics_data);
        }
    }


}
