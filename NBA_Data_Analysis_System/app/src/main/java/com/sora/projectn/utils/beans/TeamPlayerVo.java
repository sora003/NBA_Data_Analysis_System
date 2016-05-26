package com.sora.projectn.utils.beans;

/**
 * Created by Sora on 2016/2/5.
 */
public class TeamPlayerVo {
    private int id;
    private int num;
    private String name;
    private String pos;


    public TeamPlayerVo(int id, int num,String name,String pos) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
