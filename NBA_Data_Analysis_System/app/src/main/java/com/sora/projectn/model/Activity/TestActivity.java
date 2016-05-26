package com.sora.projectn.model.Activity;


import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sora.projectn.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Sora on 2016/1/22.
 */
public class TestActivity extends AppCompatActivity {

    CountDownLatch countDownLatch = new CountDownLatch(1);
    TextView textView1;
    TextView textView2;

    List<String> list = new ArrayList<String>();
    Bitmap bmp = null;

    ImageView imageView;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        textView1 = (TextView) findViewById(R.id.testText1);
        textView2 = (TextView) findViewById(R.id.testText2);

        textView1.setText("111");
        String s = (String) textView1.getText();
        textView2.setText(s);
    }



}
