package com.sora.projectn.utils.beans;

/**
 * Created by qhy on 2016/5/16.
 */
public class DayRankInfo {

    private String name;//球员姓名
    private String id;//球员ID
    private String teamName;//球队名
    private String data;//数据
    private String seasonData;//赛季数据
    private String type;//排行类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeasonData() {
        return seasonData;
    }

    public void setSeasonData(String seasonData) {
        this.seasonData = seasonData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
