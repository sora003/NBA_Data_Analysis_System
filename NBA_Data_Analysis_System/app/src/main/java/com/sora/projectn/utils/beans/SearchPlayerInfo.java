package com.sora.projectn.utils.beans;

/**
 * Created by Erwin Rommel on 2016/4/26.
 *
 *playerId 			球员id
 *season 			赛季信息
 *teamName 			球队名
 *isFirst 			首发次数
 *totalMatches 		出场次数
 *time 				场均上场时间
 *twoHit 			场均两分命中数
 *twoShot 			场均两分出手次数
 *threeHit 			场均三分命中数
 *threeShot 		场均三分出手次数
 *freeThrowHit 		场均罚球次数
 *freeThrowShot 	场均罚球命中率
 *offReb 			场均进攻篮板个数
 *defReb 			场均防守篮板个数
 *totReb 			场均篮板数
 *ass 				场均助攻数
 *steal 			场均抢断数
 *blockShot 		场均盖帽数
 *turnOver 			场均失误
 *foul 				场均犯规
 *score 			场均得分
 *
 * twoRate			两分命中率
 * threeRate		三分命中率
 * freeThrowRate	罚球命中率
 * trueRate			真实命中率
 * PER				效率值
 * doubleDouble		赛季两双次数
 * tripleDouble		赛季三双次数
 */
public class SearchPlayerInfo {

    private int playerId;

    private String playerName;

    private String season;

    private String teamName;

    private int isFirst;

    private int totalMatches;

    private double time;

    private double twoHit;

    private double twoShot;

    private double threeHit;

    private double threeShot;

    private double freeThrowHit;

    private double freeThrowShot;

    private double offReb;

    private double defReb;

    private double totReb;

    private double ass;

    private double steal;

    private double blockShot;

    private double turnOver;

    private double foul;

    private double score;

    private double twoRate;
    private double threeRate;
    private double freeThrowRate;
    private double trueRate;
    private double PER;
    private int doubleDouble;
    private int tripleDouble;

    //用于排序
    private double key;

    public double getKey() {
        return key;
    }

    public void setKey(double key) {
        this.key = key;
    }

    public double getTwoShot() {
        return twoShot;
    }

    public void setTwoShot(double twoShot) {
        this.twoShot = twoShot;
    }

    public double getAss() {
        return ass;
    }

    public void setAss(double ass) {
        this.ass = ass;
    }

    public double getBlockShot() {
        return blockShot;
    }

    public void setBlockShot(double blockShot) {
        this.blockShot = blockShot;
    }

    public double getDefReb() {
        return defReb;
    }

    public void setDefReb(double defReb) {
        this.defReb = defReb;
    }

    public double getFoul() {
        return foul;
    }

    public void setFoul(double foul) {
        this.foul = foul;
    }

    public double getFreeThrowHit() {
        return freeThrowHit;
    }

    public void setFreeThrowHit(double freeThrowHit) {
        this.freeThrowHit = freeThrowHit;
    }

    public double getFreeThrowShot() {
        return freeThrowShot;
    }

    public void setFreeThrowShot(double freeThrowShot) {
        this.freeThrowShot = freeThrowShot;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public double getOffReb() {
        return offReb;
    }

    public void setOffReb(double offReb) {
        this.offReb = offReb;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public double getSteal() {
        return steal;
    }

    public void setSteal(double steal) {
        this.steal = steal;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public double getThreeHit() {
        return threeHit;
    }

    public void setThreeHit(double threeHit) {
        this.threeHit = threeHit;
    }

    public double getThreeShot() {
        return threeShot;
    }

    public void setThreeShot(double threeShot) {
        this.threeShot = threeShot;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public double getTotReb() {
        return totReb;
    }

    public void setTotReb(double totReb) {
        this.totReb = totReb;
    }

    public double getTurnOver() {
        return turnOver;
    }

    public void setTurnOver(double turnOver) {
        this.turnOver = turnOver;
    }

    public double getTwoHit() {
        return twoHit;
    }

    public void setTwoHit(double twoHit) {
        this.twoHit = twoHit;
    }

    public double getTwoRate() {
        return twoRate;
    }

    public void setTwoRate(double twoRate) {
        this.twoRate = twoRate;
    }

    public double getTrueRate() {
        return trueRate;
    }

    public void setTrueRate(double trueRate) {
        this.trueRate = trueRate;
    }

    public int getDoubleDouble() {
        return doubleDouble;
    }

    public void setDoubleDouble(int doubleDouble) {
        this.doubleDouble = doubleDouble;
    }

    public double getFreeThrowRate() {
        return freeThrowRate;
    }

    public void setFreeThrowRate(double freeThrowRate) {
        this.freeThrowRate = freeThrowRate;
    }

    public double getPER() {
        return PER;
    }

    public void setPER(double PER) {
        this.PER = PER;
    }

    public double getThreeRate() {
        return threeRate;
    }

    public void setThreeRate(double threeRate) {
        this.threeRate = threeRate;
    }

    public int getTripleDouble() {
        return tripleDouble;
    }

    public void setTripleDouble(int tripleDouble) {
        this.tripleDouble = tripleDouble;
    }

    public Object getInfoByParameter(String para){

        switch (para){
            case "playerId":
                return getPlayerId();
            case "isFirst":
                return getIsFirst();
            case "totalMatches":
                return getTotalMatches();
            case "time":
                return getTime();
            case "twoHit":
                return getTwoHit();
            case "twoShot":
                return getTwoShot();
            case "threeHit":
                return getThreeHit();
            case "threeShot":
                return getThreeShot();
            case "freeThrowHit":
                return getFreeThrowHit();
            case "freeThrowShot":
                return getFreeThrowShot();
            case "offReb":
                return getOffReb();
            case "defReb":
                return getDefReb();
            case "totReb":
                return getTotReb();
            case "ass":
                return getAss();
            case "steal":
                return getSteal();
            case "blockShot":
                return getBlockShot();
            case "turnOver":
                return getTurnOver();
            case "foul":
                return getFoul();
            case "score":
                return getScore();
            case "twoRate":
                return getTwoRate();
            case "threeRate":
                return getThreeRate();
            case "freeThrowRate":
                return getFreeThrowRate();
            case "trueRate":
                return getTrueRate();
            case "per":
                return getPER();
            case "doubleDouble":
                return getDoubleDouble();
            case "tripleDouble":
                return getTripleDouble();

        }

        return null;
    }
}
