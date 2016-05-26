package com.sora.projectn.utils.beans;

/**
 * Created by lenovo on 2016/5/1.
 */
public class PlayerTrainingInfo {

/*
    球员ID
    球员名称
    球员前三场上场时间
    球员状态(维持，下滑，提升)
     */

    private int playerId;
    private String playerName;
    private int matchTime;
    private String state;


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    public int getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(int matchTime) {
        this.matchTime = matchTime;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
