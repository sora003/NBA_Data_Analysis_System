package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.TeamSeasonStaticsVo;

import java.util.List;

/**
 * Created by Sora on 2016/1/31.
 */
public class TeamStaticsAdapter extends BaseAdapter{

    private List<TeamSeasonStaticsVo> list;
    LayoutInflater inflater;

    public TeamStaticsAdapter(Context context, List<TeamSeasonStaticsVo> list) {
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

        TeamSeasonStaticsVo vo = (TeamSeasonStaticsVo) getItem(position);

        viewHolder.item_entry.setText(vo.getEntry());
        viewHolder.item_data.setText(vo.getData());

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
