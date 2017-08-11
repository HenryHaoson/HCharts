package com.zhuhao.hcharts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhuhao.hcharts.entity.RadarData;

import java.util.ArrayList;


/**
 * Created by zhuhao on 1/8/2017.
 */

public class RadarView extends View implements BaseView<RadarData> {

    //画笔
    private Paint mPaint;

    //数据
    private ArrayList<RadarData> mData;



    public RadarView(Context context) {
        super(context, null, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void initCommons() {

    }

    @Override
    public void initRegion(int position) {

    }

    @Override
    public int getTouchedPath(int x, int y) {
        return 0;
    }

    @Override
    public void setData(ArrayList<RadarData> data) {

    }

    @Override
    public void startAnimator() {

    }

    @Override
    public void onClickAnimator(Canvas canvas, int position) {

    }
}
