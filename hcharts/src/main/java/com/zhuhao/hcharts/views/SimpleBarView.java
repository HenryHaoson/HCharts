package com.zhuhao.hcharts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zhuhao.hcharts.entity.BarData;

import java.util.ArrayList;


/**
 * Created by HenryZhuhao on 2017/6/11.
 */

public class SimpleBarView extends View {
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<BarData> mData;
    // 宽高
    private int mWidth, mHeight;
    // 画笔
    private Paint mPaint;

    //数值最大高度
    private float totalHeight;
    //bar的宽度
    private float barWidth;
    //bar之间的间隔
    private float intervalWidth;

    //xLine的长度
    private float xLineLength;

    //valueLine的个数
    private int valueLineCount = 3;

    //动画支持数据。
    private float[] length;
    private float[] length1;

    //坐标系转换
    private Matrix mMapMatrix;

    /**
     * 构造方法
     */


    public SimpleBarView(Context context) {
        this(context, null, 0);
    }

    public SimpleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
    }

    public void setBarWidth(float width) {
        barWidth = width;
    }

    public void setIntervalWidth(float width) {
        intervalWidth = width;
    }


    public void setData(ArrayList<BarData> list, float totalHeight) {
        mData = list;
        this.totalHeight = totalHeight;
        initData(mData);
        invalidate();
    }

    private void initData(ArrayList<BarData> mData) {
        if (null == mData || mData.size() == 0) {
            return;
        }
        length = new float[mData.size()];
        length1 = new float[mData.size()];
        for (int i = 0; i < mData.size(); i++) {
            BarData bar = mData.get(i);
            int j = i % mColors.length;
            bar.setColor(mColors[j]);

        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData) {
            return;
        }
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.getMatrix().invert(mMapMatrix);
    }

    public void drawValueLines(Canvas canvas){

    }

    public void drawNames(Canvas canvas){

    }

    public void drawValues(Canvas canvas){

    }

    public void drawBars(Canvas canvas){

    }



    public void startAnimator() {

    }
}
