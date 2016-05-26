package com.sora.projectn.model.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.utils.Adapter.MainLvDrawerAdapter;
import com.sora.projectn.utils.BitmapHelper;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.SharedPreferencesHelper;
import com.sora.projectn.utils.beans.MainDrawerMenu;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sora on 2016-05-10.
 */
public class Tool_NavigationDrawerFragment extends Fragment {

    /**
     * 菜单 List
     */
    private List<MainDrawerMenu> list;

    /**
     *菜单点击接口 便于外部Activity调用
     */
    public interface menuClickListener{
        void menuClick(String menuName);
    }

    /**
     *菜单点击接口 便于外部Activity调用
     */
    public interface settingClickListener{
        void settingClick(String settingName);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_drawer,container,false);

        initLeftMenu(view);

        return view;
    }


    /**
     * 初始化左侧菜单列表 设置监听
     * @param view
     */
    private void initLeftMenu(View view){
        ListView lv_left_menu = (ListView) view.findViewById(R.id.lv_left_menu);

        TextView tv_setting = (TextView) view.findViewById(R.id.lv_setting);

        list = getMenuItem();

        lv_left_menu.setAdapter(new MainLvDrawerAdapter(getActivity(), list));

        lv_left_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() instanceof menuClickListener) {
                    ((menuClickListener) getActivity()).menuClick(list.get(position).getMainDrawer_Name());
                }
            }
        });

        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof settingClickListener) {
                    ((settingClickListener) getActivity()).settingClick("设置");
                }
            }
        });

        TextView tv_character = (TextView) view.findViewById(R.id.tv_character);
        TextView tv_user = (TextView) view.findViewById(R.id.tv_user);
        ImageView iv_character = (ImageView) view.findViewById(R.id.iv_character);

        /**
         * 设置用户角色和头像
         */
        switch (SharedPreferencesHelper.getCharacter(getActivity())){
            case Consts.SharedPreferences_Value_01:
                iv_character.setImageBitmap(BitmapHelper.loadBitMap(getActivity(),R.drawable.ch_fans));
                tv_character.setText(Consts.SharedPreferences_Value_01);
                break;
            case Consts.SharedPreferences_Value_02:
                iv_character.setImageBitmap(BitmapHelper.loadBitMap(getActivity(),R.drawable.ch_coach));
                tv_character.setText(Consts.SharedPreferences_Value_02);
                break;
            case Consts.SharedPreferences_Value_03:
                iv_character.setImageBitmap(BitmapHelper.loadBitMap(getActivity(),R.drawable.ch_search));
                tv_character.setText(Consts.SharedPreferences_Value_03);
                break;


        }

        /**
         * 设置用户昵称
         */
        tv_user.setText(SharedPreferencesHelper.getUser(getActivity()));
        tv_character.setText(SharedPreferencesHelper.getCharacter(getActivity()));

    }


    /**
     * 构造左侧菜单
     * @return
     */
    private List<MainDrawerMenu> getMenuItem(){
        List<MainDrawerMenu> mainDrawerMenus = new ArrayList<>();

        String[] names = {"我关注的球队" , "球队" , "球员检索" , "历史比赛数据","排行榜"};
        int[] icons = {R.drawable.lv_attention,R.drawable.lv_team,R.drawable.lv_player,R.drawable.lv_match,R.drawable.lv_rank};

        for (int i = 0; i < names.length; i++) {
            MainDrawerMenu mainDrawerMenu = new MainDrawerMenu(icons[i],names[i]);
            mainDrawerMenus.add(mainDrawerMenu);
        }

        return mainDrawerMenus;


    }
}
