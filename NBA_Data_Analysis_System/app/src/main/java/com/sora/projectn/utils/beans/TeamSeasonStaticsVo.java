package com.sora.projectn.utils.beans;

/**
 * Created by Sora on 2016/1/30.
 */
public class TeamSeasonStaticsVo {

    private String entry;
    private String data;

    public TeamSeasonStaticsVo( String entry,String data) {
        this.data = data;
        this.entry = entry;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

}
