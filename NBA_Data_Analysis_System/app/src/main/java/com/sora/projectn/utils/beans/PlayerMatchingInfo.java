package com.sora.projectn.utils.beans;

/**
 * Created by lenovo on 2016/5/1.
 */
public class PlayerMatchingInfo {
    private int playerId;
    private String playerName;
    private double twoRate;
    private double threeRate;
    private double freeThrowRate;
    private String state;

    public double getFreeThrowRate() {
        return freeThrowRate;
    }

    public void setFreeThrowRate(double freeThrowRate) {
        this.freeThrowRate = freeThrowRate;
    }

    public double getTwoRate() {
        return twoRate;
    }

    public void setTwoRate(double twoRate) {
        this.twoRate = twoRate;
    }

    public double getThreeRate() {
        return threeRate;
    }

    public void setThreeRate(double threeRate) {
        this.threeRate = threeRate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
