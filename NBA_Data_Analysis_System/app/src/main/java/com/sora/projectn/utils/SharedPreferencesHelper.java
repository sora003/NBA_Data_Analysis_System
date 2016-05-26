package com.sora.projectn.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sora on 2016-05-12.
 */
public class SharedPreferencesHelper {

    /**
     * 获取角色类型
     * @param context
     * @return
     */
    public static String  getCharacter(Context context) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("character", context.MODE_PRIVATE);

        return sharedPreferences.getString(Consts.SharedPreferences_KEY_01, "");

    }

    /**
     * 设置角色类型
     * @param context
     * @param character
     */
    public static void  setCharacter(Context context,String character) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("character", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.SharedPreferences_KEY_01, character);
        editor.commit();

    }

    /**
     * 获取用户名
     * @param context
     * @return
     */
    public static String getUser(Context context) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", context.MODE_PRIVATE);

        return sharedPreferences.getString(Consts.SharedPreferences_KEY_02, "");

    }

    /**
     * 设置用户名
     * @param context
     * @param user
     */
    public static void  setUser(Context context,String user) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.SharedPreferences_KEY_02, user);
        editor.commit();

    }

    /**
     * 获取关注的球队
     * @param context
     * @return
     */
    public static String getFavTeam(Context context) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("favTeam", context.MODE_PRIVATE);

        return sharedPreferences.getString(Consts.SharedPreferences_KEY_03, "");

    }

    /**
     * 设置关注的球队
     * @param context
     * @param favTeam
     */
    public static void  setFavTeam(Context context,String favTeam) {

        //使用SharedPreferences 读取球队基本数据是否已经存在
        SharedPreferences sharedPreferences = context.getSharedPreferences("favTeam", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Consts.SharedPreferences_KEY_03, favTeam);
        editor.commit();

    }
}
