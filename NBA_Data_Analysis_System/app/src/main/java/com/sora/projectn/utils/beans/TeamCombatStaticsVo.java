package com.sora.projectn.utils.beans;

/**
 * Created by Sora on 2016-05-03.
 */
public class TeamCombatStaticsVo {

    /**
     * 条目
     */
    private String entry;
    /**
     * 球队A数据
     */
    private String teamA;
    /**
     * 球队B数据
     */
    private String teamB;

    public TeamCombatStaticsVo(String entry, String teamA, String teamB) {
        this.entry = entry;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }
}
