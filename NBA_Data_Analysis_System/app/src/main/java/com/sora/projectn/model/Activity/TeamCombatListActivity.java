package com.sora.projectn.model.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora.projectn.R;
import com.sora.projectn.model.Fragment.TeamCombatListFragment;

/**
 * Created by Sora on 2016-05-02.
 *
 * 代码
 */
public class TeamCombatListActivity extends TeamListActivity {

    int teamId = 0;


    /**
     * 初始化View
     */
    protected void initView() {

        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("球队选择");
        //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TextView
        tv_westConference = (TextView) findViewById(R.id.tv_westConference);
        tv_eastConference = (TextView) findViewById(R.id.tv_eastConference);


        //ImageView
        cursor = (ImageView) findViewById(R.id.iv_teamListCursor);



        //Fragment
        westFragment = new TeamCombatListFragment();
        eastFragment = new TeamCombatListFragment();




    }


    /**
     * 获取Intent传递来的abbr值
     *
     * 接受Intent包含数据 ： 球队id
     */
    protected void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        teamId = bundle.getInt("teamId");

        int west = 0;
        int east = 1;

        //需要统一Fragment实例化对象 采用全局变量定义
        Bundle bundle1 = new Bundle();
        bundle1.putInt("league", west);
        bundle1.putInt("teamId",teamId);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("league", east);
        bundle2.putInt("teamId",teamId);

        westFragment.setArguments(bundle1);

        eastFragment.setArguments(bundle2);


    }

}

