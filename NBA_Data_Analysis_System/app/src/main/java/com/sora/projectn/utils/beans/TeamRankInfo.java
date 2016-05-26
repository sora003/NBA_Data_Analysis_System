package com.sora.projectn.utils.beans;

/**
 * Created by qhy on 2016/5/16.
 */
public class TeamRankInfo {

    private String teamId;//球队ID
    private String rank;//球队排名
    private String name;//球队名称
    private String league;//球队所属
    private String wins;//胜场
    private String loses;//负场
    private String winRate;//胜率
    private String gamesBehind;//胜场差
    private String pspg;
    private String papg;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLoses() {
        return loses;
    }

    public void setLoses(String loses) {
        this.loses = loses;
    }

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public String getGamesBehind() {
        return gamesBehind;
    }

    public void setGamesBehind(String gamesBehind) {
        this.gamesBehind = gamesBehind;
    }

    public String getPspg() {
        return pspg;
    }

    public void setPspg(String pspg) {
        this.pspg = pspg;
    }

    public String getPapg() {
        return papg;
    }

    public void setPapg(String papg) {
        this.papg = papg;
    }
}
