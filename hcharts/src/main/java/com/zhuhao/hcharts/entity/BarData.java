package com.zhuhao.hcharts.entity;

/**
 * Created by HenryZhuhao on 2017/6/11.
 */

public class BarData {
    // 用户关心数据
    private String name;        // 名字
    private float value;        // 数值

    // 非用户关心数据
    private int color = 0;      // 颜色
    private int viewHeight=0;   //绘制时的高度

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
