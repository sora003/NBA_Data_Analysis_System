package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.MatchActivity;
import com.sora.projectn.utils.beans.MatchInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qhy on 2016/5/3.
 */
public class MainCardsAdapter extends BaseAdapter {
    private List<String> date = new ArrayList<String>();
    private List<List<MatchInfo>> matches = new ArrayList<List<MatchInfo>>();
    private LayoutInflater inflater;
    Context mContext;

    public MainCardsAdapter(Map<String,List<MatchInfo>> latestmatches,Context context){
        for(Map.Entry e:latestmatches.entrySet()){
            date.add(0,(String)e.getKey());
            matches.add(0,(List<MatchInfo>) e.getValue());
        }

        this.inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return date.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        View view = null;
        if (convertView != null) {
            view = convertView;
            vh = (ViewHolder) convertView.getTag();
            // 使用缓存的view,节约内存
            // 当listview的item过多时，拖动会遮住一部分item，被遮住的item的view就是convertView保存着。
            // 当滚动条回到之前被遮住的item时，直接使用convertView，而不必再去new view()

        } else {
//            view = super.getView(position, convertView, parent);
            view = inflater.inflate(R.layout.item_main_card, null);
            vh = new ViewHolder(view);
            view.setTag(vh);

        }
        vh.date.setText(date.get(position));
//        vh.matches进行注册
        MatchListAdapter mladapter = new MatchListAdapter(matches.get(position),mContext);
        vh.matches.setAdapter(mladapter);
        vh.matches.setOnItemClickListener(new MListener(position));
        return view;
    }


    private class ViewHolder {
        TextView date;
        ListView matches;
        ViewHolder(View view){
            date = (TextView)view.findViewById(R.id.latestmatch_date);
            matches = (ListView)view.findViewById(R.id.latestmatch_list);
        }
    }

    private class MListener implements AdapterView.OnItemClickListener{
        int pos;
        public MListener(int pos){
            this.pos = pos;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(mContext, MatchActivity.class);
                    intent.putExtra("no",Integer.parseInt(matches.get(pos).get(position).getId()));
                    mContext.startActivity(intent);


        }
    }

}
