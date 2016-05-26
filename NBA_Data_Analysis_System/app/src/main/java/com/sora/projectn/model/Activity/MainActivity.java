package com.sora.projectn.model.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.sora.projectn.R;
import com.sora.projectn.model.Fragment.CoachFragment;
import com.sora.projectn.model.Fragment.MatchListFragment;
import com.sora.projectn.model.Fragment.ScoutFragment;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.SharedPreferencesHelper;
import com.sora.projectn.utils.beans.PlayerTrainingInfo;

import java.util.ArrayList;

import static com.sora.projectn.model.Fragment.Tool_NavigationDrawerFragment.*;


public class MainActivity extends AppCompatActivity implements menuClickListener,settingClickListener,MatchListFragment.OnFragmentInteractionListener {

    //侧拉菜单
    private DrawerLayout mDrawerLayout;

    private Toolbar toolbar;

    //ActionBarDrawerToggle控件
    private ActionBarDrawerToggle mDrawerToggle;



    private Intent intent;
    private Bundle bundle;

    //正在使用的Fragment
    private Fragment isFragment;

    private ArrayList<PlayerTrainingInfo> playerTrainingInfoList = new ArrayList<>();

    public ArrayList<PlayerTrainingInfo> getPlayerTrainingInfoList() {
        return playerTrainingInfoList;
    }

    public void setPlayerTrainingInfoList(ArrayList<PlayerTrainingInfo> playerTrainingInfoList) {
        this.playerTrainingInfoList = playerTrainingInfoList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();

        initFragment(savedInstanceState);

    }

    /**
     * 动态更新fragment
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        switch (SharedPreferencesHelper.getCharacter(getApplicationContext())){
            case Consts.SharedPreferences_Value_01:
                fragmentTransaction.add(R.id.main_fragment_container, new MatchListFragment());
                break;
            case Consts.SharedPreferences_Value_02:
                fragmentTransaction.add(R.id.main_fragment_container, new CoachFragment());
                break;
            case Consts.SharedPreferences_Value_03:
                fragmentTransaction.add(R.id.main_fragment_container, new ScoutFragment());
                break;
        }


        fragmentTransaction.commit();
    }


    /**
     * 实现左侧菜单的接口 实现跳转
     * @param menuName
     */
    @Override
    public void menuClick(String menuName){
        //关闭侧拉菜单
        mDrawerLayout.closeDrawers();
        switch (menuName){
            case "我关注的球队":
                startActivity(new Intent(getApplicationContext(), TeamFavActivity.class));
                break;
            case "球队":
                startActivity(new Intent(getApplicationContext(), TeamListActivity.class));
                break;
            case "球员检索":
                startActivity(new Intent(getApplicationContext(), SearchPlayerActivity.class));
                break;
            case "历史比赛数据":
                startActivity(new Intent(getApplicationContext(), MatchSearchActivity.class));
                break;
            case "排行榜":
                startActivity(new Intent(getApplicationContext(), RankActivity.class));
                break;

        }
    }

    /**
     * 实现左侧菜单的接口 实现跳转 设置和切换主题
     * @param settingName
     */
    @Override
    public void settingClick(String settingName) {
        switch (settingName){
            case "设置":
                Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("MainActivity" , 1);
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //设置Toolbar标题
        toolbar.setTitle("NBA");
        //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        setSupportActionBar(toolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //决定左上角的图标是否可以点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);



    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
