package com.zhuhao.hcharts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.zhuhao.hcharts.entity.BarData;

import java.util.ArrayList;


/**
 * Created by HenryZhuhao on 2017/6/11.
 * 进度:画笔设置
 */

public class SimpleBarView extends View {

    //view坐标系
    //      |
    //      |
    //     O|------->
    //      |
    //      |
    //      ⌄
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<BarData> mData;
    // 宽高
    private int mWidth, mHeight;
    //text画笔
    private TextPaint textPaint;

    //rect画笔
    private Paint rectPaint;

    //line画笔
    private Paint linePaint;

    //valueLine的Path；
    private Path[] valueLinePaths;

    //bars的rects
    private RectF[] barsRects;


    //value-->height转换比例，使用的时候是height=value*scale;
    private float scale;
    private float totalValue;
    //数值最大高度
    private int totalHeight;

    //valueLine 之间的距离。
    private int intervalValueHeight;

    //bar的宽度
    private int barWidth;
    //bar之间的间隔
    private int intervalWidth;

    //xLine的长度
    private float valueLineLength;

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
    }

    public void setBarWidth(int width) {
        barWidth = width;
    }

    public void setIntervalWidth(int width) {
        intervalWidth = width;
    }


    public void setData(ArrayList<BarData> list) {
        mData = list;
        initData(mData);
        invalidate();
    }

    private void initData(ArrayList<BarData> mData) {
        if (null == mData || mData.size() == 0) {
            return;
        }
        length = new float[mData.size()];
        length1 = new float[mData.size()];
        valueLinePaths = new Path[mData.size()];
        barsRects = new RectF[mData.size()];
        for (int i = 0; i < mData.size(); i++) {
            BarData bar = mData.get(i);
            int j = i % mColors.length;
            bar.setColor(mColors[j]);
        }
    }

    private void initPaint() {
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);

        linePaint=new Paint();
        PathEffect effects = new DashPathEffect(new float[]{}, 1);
        linePaint.setPathEffect(effects);

        rectPaint=new Paint();
        rectPaint.setStyle(Paint.Style.FILL);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    public void initCommons() {
        //bar的宽度是view宽度的1／11
        barWidth = mWidth / 11;

        //间隔的距离计算
        intervalWidth = (mWidth - (mData.size() + 1) * barWidth) / (mData.size());

        //valueLine的长度的计算，value的宽度占一个bar宽度
        valueLineLength = mWidth - barWidth;

        //bar可绘制最大高度
        totalHeight = mHeight / 10 * 8;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData) {
            return;
        }
        //移动坐标系，注意坐标系是这样
        //      |
        //     O|------->
        //      |
        //      |
        //      ⌄
        canvas.translate(0, mHeight);
        canvas.getMatrix().invert(mMapMatrix);
    }

    public void drawValueLines(Canvas canvas) {
        for (int i = 0; i < valueLineCount; i++) {
            Path path = valueLinePaths[i];
            path.moveTo(barWidth, -intervalValueHeight * (i + 1));
            path.lineTo(mWidth, -intervalValueHeight * (i + 1));
            canvas.drawPath(path, textPaint);
        }

    }

    public void drawNames(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            canvas.drawText(mData.get(i).getName(), barWidth, 0, textPaint);
        }
    }

    public void drawValues(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            if (i == 0) {
                canvas.drawText(0+"",0,0,textPaint);
            }else {
                canvas.drawText((totalValue/3*i)+"",0,-intervalValueHeight*i,textPaint);
            }
        }
    }

    public void drawBars(Canvas canvas) {
        for (int i = 0; i <mData.size() ; i++) {
            RectF rect=barsRects[i];
            rect.left=barWidth+i*(barWidth+intervalValueHeight);
            rect.right=rect.left+barWidth;
            rect.top=-mData.get(i).getViewHeight();
            rect.top=-mWidth/10;

            canvas.drawRect(rect,rectPaint);
        }
    }

    /**
     * 设置横向valueLine的个数
     *
     * @param count 用户输入个数，默认3个
     */
    public void setValueLineCount(int count) {
        valueLineCount = count;
    }

    /**
     * 设置最大value，就是最上面的ValueLine的值
     *
     * @param value 用户输入的value
     */
    public void setTotalValue(float value) {
        totalValue = value;

        //在这里设置比例，就不暴露给外部了。
        scale = totalHeight / totalValue;
    }


    public void startAnimator() {

    }


    //*************
    public static Builder build(SimpleBarView view) {
        return new Builder(view);
    }


    public static class Builder {
        private SimpleBarView mView;

        private Builder(SimpleBarView view) {
            this.mView = view;
        }

        public Builder setTotalValue(float value) {
            mView.setTotalValue(value);
            return this;
        }

        public Builder setData(ArrayList<BarData> list) {
            mView.setData(list);
            return this;
        }

        public void start() {
            mView.startAnimator();
        }
    }
}
