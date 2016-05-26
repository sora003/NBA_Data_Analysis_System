package com.sora.projectn.utils;

/**
 * Created by lenovo on 2016/5/2.
 */
public class Token {
    private String type="";
    private String value="";
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public Token(){

    }

    public Token(String type, String value){
        this.type=type;
        this.value=value;
    }

}
