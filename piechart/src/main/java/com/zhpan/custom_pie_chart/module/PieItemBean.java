package com.zhpan.custom_pie_chart.module;

/**
 * create by zhpan on 2018/8/27 17:09
 * Describe:
 */
public class PieItemBean {
    private String itemType;

    private int itemValue;

    private int color;

    public PieItemBean(String itemType,  int color,int itemValue) {
        this.itemType = itemType;
        this.itemValue = itemValue;
        this.color = color;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
