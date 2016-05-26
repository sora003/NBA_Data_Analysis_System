package com.sora.projectn.model.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.projectn.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.model.Activity.TeamCombatListActivity;
import com.sora.projectn.model.Fragment.CoachFreeThrowRateFragment;

import com.sora.projectn.model.Fragment.CoachThreeRateFragment;
import com.sora.projectn.model.Fragment.CoachTrainFragment;
import com.sora.projectn.model.Fragment.CoachTwoRateFragment;
import com.sora.projectn.utils.ACache;
import com.sora.projectn.utils.Adapter.FragAdapter;
import com.sora.projectn.utils.BitmapHelper;
import com.sora.projectn.utils.Consts;

import com.sora.projectn.utils.GetHttpResponse;
import com.sora.projectn.utils.beans.PlayerTrainingInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachFragment extends Fragment {


    private Context mContext;
    /**
     * 球队编号
     */
    private int id=0;
    private Map<String,String> infoMap = new HashMap<>();
    private Map<String,Integer> infoMap2 = new HashMap<>();

    //Fragment
    private CoachTrainFragment coachTrainFragment;
    private CoachThreeRateFragment coachThreeRateFragment;
    private CoachTwoRateFragment coachTwoRateFragment;
    private CoachFreeThrowRateFragment coachFreeThrowRateFragment;

    //ViewPager
    private ViewPager viewPager;


    //TextView
    private TextView tv_teamGuide1_Coach;
    private TextView tv_teamGuide2_Coach;
    private TextView tv_teamGuide3_Coach;
    private TextView tv_teamGuide4_Coach;
    private TextView tv_teamName_Coach;
    private TextView tv_season_Coach;
    private TextView tv_winlose_Coach;
    private TextView tv_rank_Coach;
    private TextView tv_combat_history;

    private View fView;

    //ImageView
    private ImageView cursor_Coach;
    private ImageView iv_teamLogo_Coach;

    private List<Fragment> fragments_Coach;

    private FragAdapter adapter_Coach;

    //游标宽度
    private int bmpw = 0;
    //动画图片偏移量 滑块占据一个标签栏 offset设置为0
    private int offset = 0;
    //当前页卡编号  初始编号为0
    private int currIndex = 0;
    private Map<String, String> teamInfo;

    private ArrayList<PlayerTrainingInfo> playerTrainingInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_coach,container,false);

        fView = view;

        mContext = this.getActivity();

        initView();

        parseIntent();

        initViewPager();
        initListener();

        //获取球队基本信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                infoMap = getTeamInfo();

                infoMap2 = getTeamsId();

                if (infoMap == null){
                    handler.sendEmptyMessage(Consts.RES_ERROR);
                }
                else {
                    handler.sendEmptyMessage(Consts.SET_VIEW);
                }
            }
        }).start();

        return view;

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Consts.SET_VIEW:
                    setTopView();
                    break;
                case Consts.RES_ERROR:
                    Toast.makeText(mContext, Consts.ToastMessage01, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //设置顶层视图
    private void setTopView() {
        iv_teamLogo_Coach.setImageBitmap(BitmapHelper.loadBitMap(mContext, BitmapHelper.getBitmap(infoMap.get("name"))));
        tv_teamName_Coach.setText(infoMap.get("city")+infoMap.get("name"));
        tv_season_Coach.setText(infoMap.get("season") + "赛季");
        tv_winlose_Coach.setText(infoMap.get("wins") + " 胜 " + infoMap.get("loses") + " 负");
        tv_rank_Coach.setText("排名 : "+infoMap.get("rank"));
    }

    /**
     * 获取Intent传递来的abbr值
     */
    private void parseIntent() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
//        id=2;//>>>>>>>>>>>>>
        //需要统一Fragment实例化对象 采用全局变量定义
        bundle = new Bundle();
        bundle.putInt("id", id);

        coachTrainFragment.setArguments(bundle);

        coachThreeRateFragment.setArguments(bundle);

        coachTwoRateFragment.setArguments(bundle);

        coachFreeThrowRateFragment.setArguments(bundle);

    }

    /**
     * 初始化View
     */
    private void initView() {

        mContext = this.getActivity();


        //TextView
        tv_teamGuide1_Coach = (TextView) fView.findViewById(R.id.tv_teamGuide1_Coach);
        tv_teamGuide2_Coach = (TextView) fView.findViewById(R.id.tv_teamGuide2_Coach);
        tv_teamGuide3_Coach = (TextView) fView.findViewById(R.id.tv_teamGuide3_Coach);
        tv_teamGuide4_Coach = (TextView) fView.findViewById(R.id.tv_teamGuide4_Coach);
        tv_teamName_Coach = (TextView) fView.findViewById(R.id.tv_teamName_Coach);
        tv_season_Coach = (TextView) fView.findViewById(R.id.tv_season_Coach);
        tv_winlose_Coach = (TextView) fView.findViewById(R.id.tv_winlose_Coach);
        tv_rank_Coach = (TextView) fView.findViewById(R.id.tv_rank_Coach);
        tv_combat_history = (TextView) fView.findViewById(R.id.tv_combat_history);

        //ImageView
        cursor_Coach = (ImageView) fView.findViewById(R.id.iv_teamCursor_Coach);
        iv_teamLogo_Coach = (ImageView) fView.findViewById(R.id.iv_teamLogo_Coach);


        //Fragment
        coachTrainFragment = new CoachTrainFragment();
        coachThreeRateFragment = new CoachThreeRateFragment();
        coachTwoRateFragment = new CoachTwoRateFragment();
        coachFreeThrowRateFragment = new CoachFreeThrowRateFragment();

    }



    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager = (ViewPager) fView.findViewById(R.id.viewpager);

        fragments_Coach = new ArrayList<Fragment>();
        fragments_Coach.add(coachTrainFragment);
        fragments_Coach.add(coachThreeRateFragment);
        fragments_Coach.add(coachTwoRateFragment);
        fragments_Coach.add(coachFreeThrowRateFragment);

        //新建适配器对象,有改动
        adapter_Coach = new FragAdapter(getChildFragmentManager(),fragments_Coach);


        //设定适配器
        viewPager.setAdapter(adapter_Coach);

        //初始化指示器位置
        initCursorPos();

        //初始化ViewPager在第一个模块
        viewPager.setCurrentItem(0);
        //对应的标签栏高亮显示
        tv_teamGuide1_Coach.setAlpha(1);

    }


    private void initCursorPos() {
        //初始化游标宽度为四分之一屏幕宽度
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        bmpw = width/4;

        ViewGroup.LayoutParams params = cursor_Coach.getLayoutParams();

        //动态设置cursor的宽度
        params.width = bmpw;

    }

    /**
     * 设置监听
     */
    private void initListener() {
        //标签栏的监听
        tv_teamGuide1_Coach.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide2_Coach.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide3_Coach.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });
        tv_teamGuide4_Coach.setOnClickListener(new MyOnClickListener(){
            @Override
            public void onClick(View v) {
                super.onClick(v);
            }
        });

        //ViewPager的监听  解决用户通过手势滑动获取新的模块 此时标签栏没有同步改变的问题
        //同时完成滑块滑动的动画
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());


        //交锋历史按钮的监听
        tv_combat_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("COMBAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Bundle bundle= new Bundle();
                bundle.putInt("teamId", id);
                Intent intent = new Intent(mContext, TeamCombatListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    public Map<String, String> getTeamInfo() {
        Map<String ,String> map = new HashMap<>();

        String jsonString;

        /**
         * getTeamInfos
         */
        jsonString = ACache.get(mContext).getAsString("getTeamInfos - " + id);

        if (jsonString == null){

            jsonString = GetHttpResponse.getHttpResponse(Consts.getTeamInfos + "?teamId=" + id);

            if (jsonString == null){
                return null;
            }

            ACache.get(mContext).put("getTeamInfos - " + id ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONObject obj = new JSONObject(jsonString);

            map.put("name",obj.getString("name"));
            map.put("city",obj.getString("city"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        /**
         * getTeamSeasonStatistics
         */
        jsonString = ACache.get(mContext).getAsString("getTeamSeasonStatistics - " + id);

        if (jsonString == null){
            //从server获取数据

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamSeasonStatistics + "?teamId=" + id);

            ACache.get(mContext).put("getTeamSeasonStatistics - " + id ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONObject obj = new JSONObject(jsonString);

            map.put("season", obj.getString("season"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * getTeamSeasonRanks
         */
        jsonString = ACache.get(mContext).getAsString("getTeamSeasonRanks - " + (id));

        if (jsonString == null){
            //从server获取数据

            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeamSeasonRanks + "?teamId=" + (id-1));

            ACache.get(mContext).put("getTeamSeasonRanks - " + (id) ,jsonString,ACache.TEST_TIME);
            Log.i("Resource", "server");
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {

            JSONArray array = new JSONArray(jsonString);

            JSONObject obj = array.getJSONObject(id - 1);

            map.put("rank", String.valueOf(obj.getInt("rank")));

            map.put("wins", String.valueOf(obj.getInt("wins")));

            map.put("loses", String.valueOf(obj.getInt("loses")));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //跳转到新的fragment
            switch (v.getId()){
                case R.id.tv_teamGuide1_Coach:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tv_teamGuide2_Coach:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.tv_teamGuide3_Coach:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.tv_teamGuide4_Coach:
                    viewPager.setCurrentItem(3);
                    break;
            }

        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //相邻页面的偏移量
        private int one = bmpw;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //即将显示的页卡的index
        @Override
        public void onPageSelected(int position) {
            //初始化移动的动画 (从当前位置 平移到即将摇到的位置)
            Animation animation = new TranslateAnimation(currIndex * one , position * one , 0 , 0);
            //动画终止时停留在最后一帧  不然会回到没有执行前的状态
            animation.setFillAfter(true);
            //动画持续时间0.2秒
            animation.setDuration(200);
            //用游标来显示动画
            cursor_Coach.startAnimation(animation);

            //取消之前选中的标签的高亮显示
            switch (currIndex){
                case 0:
                    tv_teamGuide1_Coach.setAlpha((float) 0.5);
                    break;
                case 1:
                    tv_teamGuide2_Coach.setAlpha((float) 0.5);
                    break;
                case 2:
                    tv_teamGuide3_Coach.setAlpha((float) 0.5);
                    break;
                case 3:
                    tv_teamGuide4_Coach.setAlpha((float) 0.5);
                    break;

            }
            //将即将切换到的标签的高亮显示
            switch (position){
                case 0:
                    tv_teamGuide1_Coach.setAlpha(1);
                    break;
                case 1:
                    tv_teamGuide2_Coach.setAlpha(1);
                    break;
                case 2:
                    tv_teamGuide3_Coach.setAlpha(1);
                    break;
                case 3:
                    tv_teamGuide4_Coach.setAlpha(1);
                    break;

            }

            //当前页卡发生改变 重定义currIndex
            currIndex = position;
        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public ArrayList<PlayerTrainingInfo> getPlayerTrainingInfoList() {
        return playerTrainingInfoList;
    }

    public void setPlayerTrainingInfoList(ArrayList<PlayerTrainingInfo> playerTrainingInfoList) {
        this.playerTrainingInfoList = playerTrainingInfoList;
    }

    protected Map<String,Integer> getTeamsId() {

        Map<String,Integer> map = new HashMap<>();

        String jsonString = ACache.get(mContext).getAsString("getTeams");

        if (jsonString == null){
            //从server获取数据
            jsonString = "[{\"id\":1,\"name\":\"老鹰\",\"city\":\"亚特兰大\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"菲利普斯球馆\",\"startYearInNBA\":1949,\"numOfChampions\":1},{\"id\":2,\"name\":\"凯尔特人\",\"city\":\"波士顿\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"TD花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":17},{\"id\":3,\"name\":\"鹈鹕\",\"city\":\"新奥尔良\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"新奥尔良球馆\",\"startYearInNBA\":1988,\"numOfChampions\":0},{\"id\":4,\"name\":\"公牛\",\"city\":\"芝加哥\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"联合中心球馆\",\"startYearInNBA\":1966,\"numOfChampions\":6},{\"id\":5,\"name\":\"骑士\",\"city\":\"克利夫兰\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"快贷球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":6,\"name\":\"小牛\",\"city\":\"达拉斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1980,\"numOfChampions\":1},{\"id\":7,\"name\":\"掘金\",\"city\":\"丹佛\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"百事中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":8,\"name\":\"活塞\",\"city\":\"底特律\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"奥本山宫球馆\",\"startYearInNBA\":1948,\"numOfChampions\":3},{\"id\":9,\"name\":\"勇士\",\"city\":\"金州\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"甲骨文球馆\",\"startYearInNBA\":1946,\"numOfChampions\":4},{\"id\":10,\"name\":\"火箭\",\"city\":\"休斯顿\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"丰田中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":2},{\"id\":11,\"name\":\"步行者\",\"city\":\"印第安纳\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"银行家生活球馆\",\"startYearInNBA\":1976,\"numOfChampions\":0},{\"id\":12,\"name\":\"快船\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1970,\"numOfChampions\":0},{\"id\":13,\"name\":\"湖人\",\"city\":\"洛杉矶\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"斯台普斯中心球馆\",\"startYearInNBA\":1948,\"numOfChampions\":16},{\"id\":14,\"name\":\"热火\",\"city\":\"迈阿密\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"美航球馆\",\"startYearInNBA\":1988,\"numOfChampions\":3},{\"id\":15,\"name\":\"雄鹿\",\"city\":\"密尔沃基\",\"league\":\"东部\",\"conference\":\"中部区\",\"court\":\"布拉德利中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":1},{\"id\":16,\"name\":\"森林狼\",\"city\":\"明尼苏达\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"标靶中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":17,\"name\":\"篮网\",\"city\":\"布鲁克林\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"巴克莱中心球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":18,\"name\":\"尼克斯\",\"city\":\"纽约\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"麦迪逊广场花园球馆\",\"startYearInNBA\":1946,\"numOfChampions\":2},{\"id\":19,\"name\":\"魔术\",\"city\":\"奥兰多\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"安利中心球馆\",\"startYearInNBA\":1989,\"numOfChampions\":0},{\"id\":20,\"name\":\"76人\",\"city\":\"费城\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"富国银行中心球馆\",\"startYearInNBA\":1937,\"numOfChampions\":3},{\"id\":21,\"name\":\"太阳\",\"city\":\"菲尼克斯\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"美航中心球馆\",\"startYearInNBA\":1968,\"numOfChampions\":0},{\"id\":22,\"name\":\"开拓者\",\"city\":\"波特兰\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"玫瑰花园球馆\",\"startYearInNBA\":1970,\"numOfChampions\":1},{\"id\":23,\"name\":\"国王\",\"city\":\"萨克拉门托\",\"league\":\"西部\",\"conference\":\"太平洋区\",\"court\":\"能量平衡球馆\",\"startYearInNBA\":1948,\"numOfChampions\":1},{\"id\":24,\"name\":\"马刺\",\"city\":\"圣安东尼奥\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"AT&T中心球馆\",\"startYearInNBA\":1976,\"numOfChampions\":5},{\"id\":25,\"name\":\"雷霆\",\"city\":\"俄克拉荷马城\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"切萨皮克能源球馆\",\"startYearInNBA\":1967,\"numOfChampions\":1},{\"id\":26,\"name\":\"爵士\",\"city\":\"犹他\",\"league\":\"西部\",\"conference\":\"西北区\",\"court\":\"能源方案球馆\",\"startYearInNBA\":1974,\"numOfChampions\":0},{\"id\":27,\"name\":\"奇才\",\"city\":\"华盛顿\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"威瑞森中心球馆\",\"startYearInNBA\":1961,\"numOfChampions\":1},{\"id\":28,\"name\":\"猛龙\",\"city\":\"多伦多\",\"league\":\"东部\",\"conference\":\"大西洋区\",\"court\":\"加航中心球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":29,\"name\":\"灰熊\",\"city\":\"孟菲斯\",\"league\":\"西部\",\"conference\":\"西南区\",\"court\":\"联邦快递球馆\",\"startYearInNBA\":1995,\"numOfChampions\":0},{\"id\":30,\"name\":\"黄蜂\",\"city\":\"夏洛特\",\"league\":\"东部\",\"conference\":\"东南区\",\"court\":\"时代华纳中心球馆\",\"startYearInNBA\":2004,\"numOfChampions\":0}]";

//            jsonString= GetHttpResponse.getHttpResponse(Consts.getTeams);

            if (jsonString == null){
                return null;
            }

            ACache.get(mContext).put("getTeams",jsonString,ACache.TEST_TIME);
            Log.i("Resource", Consts.resourceFromServer);
        }
        else
        {
            Log.i("Resource",Consts.resourceFromCache);
        }

        //解析jsonString 构造Map
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                int id = obj.getInt("id");

                map.put(name,id);

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;

    }

}
