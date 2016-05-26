package com.sora.projectn.utils.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.BitmapHelper;
import com.sora.projectn.utils.beans.MainDrawerMenu;
import com.sora.projectn.utils.beans.TeamCombatStaticsVo;

import java.util.List;

/**
 * Created by Sora on 2016-05-12.
 */
public class MainLvDrawerAdapter extends BaseAdapter {

    private List<MainDrawerMenu> list;
    LayoutInflater inflater;

    public MainLvDrawerAdapter(Context context, List<MainDrawerMenu> list) {
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
            view = inflater.inflate(R.layout.item_fragment_main_drawer,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MainDrawerMenu mainDrawerMenu = (MainDrawerMenu) getItem(position);

        viewHolder.item_icon.setImageBitmap(BitmapHelper.loadBitMap(view.getContext(), mainDrawerMenu.getMainDrawer_Icon()));
        viewHolder.item_name.setText(mainDrawerMenu.getMainDrawer_Name());

        return view;
    }

    class ViewHolder{
        ImageView item_icon;
        TextView item_name;

        public ViewHolder(View view) {
            this.item_icon = (ImageView) view.findViewById(R.id.item_icon);
            this.item_name = (TextView) view.findViewById(R.id.item_name);
        }
    }
}
