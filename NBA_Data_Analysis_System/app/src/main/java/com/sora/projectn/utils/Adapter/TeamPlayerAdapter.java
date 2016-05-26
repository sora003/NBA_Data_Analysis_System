package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.beans.TeamPlayerVo;

import java.util.List;

/**
 * Created by Sora on 2016/2/5.
 */
public class TeamPlayerAdapter extends BaseAdapter {
    private List<TeamPlayerVo> list;
    LayoutInflater inflater;

    public TeamPlayerAdapter(Context context, List<TeamPlayerVo> list) {
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
            view = inflater.inflate(R.layout.item_teamplayer,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TeamPlayerVo vo = (TeamPlayerVo) getItem(position);

        //Error:android.content.res.Resources$NotFoundException: String resource ID #0x0
        //把一个int型业务数据的 设置setText（）或者类似的方法中 ,这样Android系统就会主动去资源文件当中寻找, 但是它不是一个资源文件ID, 所以就会报出这个bug  需要将int型业务数据 转换成String类型
        viewHolder.item_teamplayer_num.setText(String.valueOf(vo.getNum()));
        viewHolder.item_teamplayer_name.setText(vo.getName());
        viewHolder.item_teamplayer_pos.setText(vo.getPos());

        return view;
    }

    class ViewHolder{
        TextView item_teamplayer_num;
        TextView item_teamplayer_name;
        TextView item_teamplayer_pos;

        public ViewHolder(View view) {
            this.item_teamplayer_num = (TextView) view.findViewById(R.id.item_teamplayer_num);
            this.item_teamplayer_name = (TextView) view.findViewById(R.id.item_teamplayer_name);
            this.item_teamplayer_pos = (TextView) view.findViewById(R.id.item_teamplayer_pos);
        }
    }
}
