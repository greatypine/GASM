package com.cnpc.pms.base.file.utils;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/16.
 */
public enum HelloEnum {
    red(2),yellow(3),blank(4),widht(5);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value =0;
    private HelloEnum(int value){
        this.value = value;
    }

    public HelloEnum valueOf(int index){
        switch (index){
            case 1:
                return red;
            case 2:
                return yellow;
            default:
                return null;
        }

    }
}
