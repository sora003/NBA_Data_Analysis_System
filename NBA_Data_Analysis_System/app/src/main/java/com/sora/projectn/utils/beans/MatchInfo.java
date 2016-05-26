package com.sora.projectn.utils.beans;

/**
 * Created by qhy on 2016/5/16.
 */
public class MatchInfo {

    private String id;//比赛ID
    private String vId;//客队ID
    private String visitingTeam;//客队名称
    private String hId;//主队ID
    private String homeTeam;//主队名称
    private String visitingScore;//客队得分
    private String homeScore;//主队得分
    private String type;//比赛类型
    private String date;//比赛日期
    private String year;//赛季年份
    private String time;//比赛时长
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }

    public String getVisitingTeam() {
        return visitingTeam;
    }

    public void setVisitingTeam(String visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getVisitingScore() {
        return visitingScore;
    }

    public void setVisitingScore(String visitingScore) {
        this.visitingScore = visitingScore;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
