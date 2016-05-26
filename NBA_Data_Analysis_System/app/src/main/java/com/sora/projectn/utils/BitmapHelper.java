package com.sora.projectn.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sora.projectn.R;

import java.io.InputStream;

/**
 * Created by Sora on 2016/4/12.
 */
public class BitmapHelper {

    /**
     * 获取球队对应图标
     *
     * @param s
     * @return
     */
    public static  int getBitmap(String s) {

        switch (s){
            case "老鹰" :

                return R.drawable.atl;

            case "凯尔特人" :
                return R.drawable.bos;

            case "黄蜂" :
                return R.drawable.cha;

            case "公牛" :
                return R.drawable.chi;

            case "骑士" :
                return R.drawable.cle;

            case "小牛" :
                return R.drawable.dal;

            case "掘金" :
                return R.drawable.den;

            case "活塞" :
                return R.drawable.det;

            case "勇士" :
                return R.drawable.gsw;

            case "火箭" :
                return R.drawable.hou;

            case "步行者" :
                return R.drawable.ind;

            case "快船" :
                return R.drawable.lac;

            case "湖人" :
                return R.drawable.lal;

            case "灰熊" :
                return R.drawable.mem;

            case "热火" :
                return R.drawable.mia;

            case "雄鹿" :
                return R.drawable.mil;

            case "森林狼" :
                return R.drawable.min;

            case "篮网" :
                return R.drawable.njn;

            case "鹈鹕" :
                return R.drawable.noh;

            case "尼克斯" :
                return R.drawable.nyk;

            case "雷霆" :
                return R.drawable.okc;

            case "魔术" :
                return R.drawable.orl;

            case "76人" :
                return R.drawable.phi;

            case "太阳" :
                return R.drawable.pho;

            case "开拓者" :
                return R.drawable.por;

            case "国王" :
                return R.drawable.sac;

            case "马刺" :
                return R.drawable.sas;

            case "猛龙" :
                return R.drawable.tor;

            case "爵士" :
                return R.drawable.uta;

            case "奇才" :
                return R.drawable.was;

        }


        return 0;
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap loadBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
}
