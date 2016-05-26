package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.TeamCombatStaticsVo;
import com.sora.projectn.utils.beans.TeamSeasonStaticsVo;

import java.util.List;

/**
 * Created by Sora on 2016-05-03.
 */
public class TeamCombatAdapter extends BaseAdapter{
    private List<TeamCombatStaticsVo> list;
    LayoutInflater inflater;

    public TeamCombatAdapter(Context context, List<TeamCombatStaticsVo> list) {
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
            view = inflater.inflate(R.layout.item_teamcombatstatics,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TeamCombatStaticsVo vo = (TeamCombatStaticsVo) getItem(position);

        viewHolder.item_entry.setText(vo.getEntry());
        viewHolder.item_teamA.setText(vo.getTeamA());
        viewHolder.item_teamB.setText(vo.getTeamB());

        return view;
    }

    class ViewHolder{
        TextView item_entry;
        TextView item_teamA;
        TextView item_teamB;

        public ViewHolder(View view) {
            this.item_entry = (TextView) view.findViewById(R.id.item_teamcombatstatics_entry);
            this.item_teamA = (TextView) view.findViewById(R.id.item_teamcombatstatics_teamA);
            this.item_teamB = (TextView) view.findViewById(R.id.item_teamcombatstatics_teamB);
        }
    }
}
