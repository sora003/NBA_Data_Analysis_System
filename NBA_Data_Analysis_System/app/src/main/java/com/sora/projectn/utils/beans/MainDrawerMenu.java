package com.sora.projectn.utils.beans;

/**
 * Created by Sora on 2016-05-10.
 *
 * MainActivity左侧抽屉内容
 */
public class MainDrawerMenu {

    /**
     * 图标
     */
    private int mainDrawer_Icon;

    /**
     * 条目
     */
    private String mainDrawer_Name;

    public MainDrawerMenu(int mainDrawer_Icon, String mainDrawer_Name) {
        this.mainDrawer_Icon = mainDrawer_Icon;
        this.mainDrawer_Name = mainDrawer_Name;
    }

    public int getMainDrawer_Icon() {
        return mainDrawer_Icon;
    }

    public void setMainDrawer_Icon(int mainDrawer_Icon) {
        this.mainDrawer_Icon = mainDrawer_Icon;
    }

    public String getMainDrawer_Name() {
        return mainDrawer_Name;
    }

    public void setMainDrawer_Name(String mainDrawer_Name) {
        this.mainDrawer_Name = mainDrawer_Name;
    }
}
