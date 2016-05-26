package com.sora.projectn.model.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sora.projectn.R;
import com.sora.projectn.utils.Consts;
import com.sora.projectn.utils.SharedPreferencesHelper;

/**
 * Created by Sora on 2016-04-26.
 */
public class WelcomeActivity extends AppCompatActivity{


    /**
     * 记录是否从MainActivity跳转
     */
    private int flag = 0;

    private String character = "";

    private ImageView welcome_01;
    private ImageView welcome_02;
    private ImageView welcome_03;

    private ImageView iv_commit;

    private TextInputLayout textInputLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        parseIntent();




        new Thread(new Runnable() {
            @Override
            public void run() {

                /**
                 * 默认设置关注球队
                 */
                SharedPreferencesHelper.setFavTeam(getApplicationContext(),"灰熊");


                /**
                 * 获取用户角色
                 */
                String character = SharedPreferencesHelper.getCharacter(getApplicationContext());

                //TODO
                if ((flag != 1) && (!character.equals(""))){
                    handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
                }

            }
        }).start();

        initView();



    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Consts.TURN_ACTIVITY:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            flag = bundle.getInt("MainActivity");
        }


    }

    private void initView() {


        welcome_01 = (ImageView) findViewById(R.id.welcome_01);
        welcome_02 = (ImageView) findViewById(R.id.welcome_02);
        welcome_03 = (ImageView) findViewById(R.id.welcome_03);


        iv_commit = (ImageView) findViewById(R.id.iv_commit);

        textInputLayout = (TextInputLayout) findViewById(R.id.usernameWrapper);

        welcome_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                character = Consts.SharedPreferences_Value_01;
                welcome_01.setAlpha((float) 1);
                welcome_02.setAlpha((float) 0.5);
                welcome_03.setAlpha((float) 0.5);
            }
        });

        welcome_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                character = Consts.SharedPreferences_Value_02;
                welcome_01.setAlpha((float) 0.5);
                welcome_02.setAlpha((float) 1);
                welcome_03.setAlpha((float) 0.5);
            }
        });

        welcome_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                character = Consts.SharedPreferences_Value_03;
                welcome_01.setAlpha((float) 0.5);
                welcome_02.setAlpha((float) 0.5);
                welcome_03.setAlpha((float) 1);
            }
        });


        iv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                String username = textInputLayout.getEditText().getText().toString();


                if ((!character.equals("")) && (username != null) && (!username.equals("")) ){
                    Toast.makeText(getApplicationContext(),"设置完成",Toast.LENGTH_SHORT).show();
                    /**
                     * 设置用户角色
                     */
                    SharedPreferencesHelper.setCharacter(getApplicationContext(),character);
                    /**
                     * 设置用户名
                     */
                    SharedPreferencesHelper.setUser(getApplicationContext(),username);

                    handler.sendEmptyMessage(Consts.TURN_ACTIVITY);
                }
                else {
                    Toast.makeText(getApplicationContext(),"输入不合法",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }





}
