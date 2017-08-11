package com.zhuhao.hcharts.views;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by zhuhao on 31/7/2017.
 */

public interface BaseView <T>{
    /**
     * 初始化必要参数
     */
    void initCommons();



    /**
     * 初始化点击范围
     * @param position
     */
    void initRegion(int position);



    /**
     * 根据touch的坐标来返回点击的区域位置
     * @param x 点击的x坐标
     * @param y 点击的y坐标
     * @return 返回点击的是哪一块区域
     */
    int getTouchedPath(int x, int y);



    /**
     * 传入数据参数
     * @param data
     */
    void setData(ArrayList<T> data);



    /**
     * 通过属性动画来执行各种动画效果
     * valueAnimator的监听addUpdateListener设置各个属性值，然后绘制。
     */
    void startAnimator();



    /**
     *点击对应的position的部分，绘制动画效果。
     * @param canvas 画布
     * @param position 对应的position点击事件
     */
    void onClickAnimator(Canvas canvas, int position);


    
}
