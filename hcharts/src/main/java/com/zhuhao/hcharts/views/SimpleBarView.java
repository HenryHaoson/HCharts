package com.zhuhao.hcharts.views;

import android.animation.ValueAnimator;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhuhao.hcharts.entity.BarData;

import java.util.ArrayList;


/**
 * Created by HenryZhuhao on 2017/6/11.
 * 进度:valueline绘制，点击事件，动画
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
    private float scale = 0;
    private float totalValue = 0;
    //数值最大高度
    private int totalHeight = 0;

    //valueLine 之间的距离。
    private int intervalValueHeight = 0;

    //bar的宽度
    private int barWidth;
    //bar之间的间隔
    private int intervalWidth = 0;

    //xLine的长度
    private float valueLineLength = 0;

    //valueLine的个数
    private int valueLineCount = 3;

    //动画支持数据。
    private float[] length;

    private float[] barValues;

    //坐标系转换
    private Matrix mMapMatrix;

    private BarListener mListener;

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
        initPaint();
        mMapMatrix = new Matrix();
    }

//    public void setBarWidth(int width) {
//        barWidth = width;
//    }
//
//    public void setIntervalWidth(int width) {
//        intervalWidth = width;
//    }


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
        barValues = new float[mData.size()];

        valueLinePaths = new Path[mData.size()];
        barsRects = new RectF[mData.size()];
        for (int i = 0; i < mData.size(); i++) {

            BarData bar = mData.get(i);
            if (bar.getColor() == 0) {
                int j = i % mColors.length;
                bar.setColor(mColors[j]);
            }
        }
        //initCommons();
    }

    private void initPaint() {

        //设置文字画笔
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);
        //Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        //textPaint.setTypeface(font);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //设置虚线画笔
        linePaint = new Paint();
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        linePaint.setPathEffect(effects);

        //设置矩形画笔
        rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMapMatrix.reset();
        mHeight = h;
        mWidth = w;
        initCommons();
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


        intervalValueHeight = totalHeight / valueLineCount;


        scale = totalHeight / totalValue;

        Log.d("simpleBarView", "barWidth" + barWidth);
        Log.d("simpleBarView", "intervalWidth" + intervalWidth);
        Log.d("simpleBarView", "totalHeight" + totalHeight);
        scale = totalHeight / totalValue;
        initBarHeights();
        initPaths();
        //生命周期问题，在这里执行start方法。
        startAnimator();

    }

    public void initBarHeights() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setViewHeight((int) (mData.get(i).getValue() * scale));
        }
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
        drawNames(canvas);
        drawBars(canvas);
        drawValues(canvas);
        drawBarValues(canvas);
    }

    public void initPaths() {

        for (int i = 0; i < valueLineCount; i++) {
            valueLinePaths[i] = new Path();
            valueLinePaths[i].moveTo(barWidth, -intervalValueHeight * (i + 1));
            valueLinePaths[i].lineTo(mWidth, -intervalValueHeight * (i + 1));
        }
    }


    public void drawNames(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            canvas.drawText(mData.get(i).getName(), barWidth + i * (barWidth + intervalWidth) + barWidth / 2, -20, textPaint);
        }
    }

    public void drawValues(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            if (i == 0) {
                canvas.drawText(0 + "", barWidth / 3, -mHeight / 10, textPaint);
            } else {
                canvas.drawText(((Math.round((totalValue / 3 * i) * 1000)) / 1000) + "", barWidth / 3, -(intervalValueHeight * i + mHeight / 10), textPaint);
            }
        }
    }

    public void initBars() {
        for (int i = 0; i < mData.size(); i++) {
            barsRects[i] = new RectF();
            barsRects[i].left = barWidth + i * (barWidth + intervalWidth);
            barsRects[i].right = barsRects[i].left + barWidth;
//            barsRects[i].top = -mData.get(i).getViewHeight();

            barsRects[i].bottom = -mHeight / 10;
            barsRects[i].top = -length[i] + barsRects[i].bottom;
            Log.d("simpleBarView", "getviewHeight" + mData.get(i).getViewHeight() + "");
        }
    }

    public void drawBars(Canvas canvas) {
        initBars();
        for (int i = 0; i < mData.size(); i++) {
            rectPaint.setColor(mData.get(i).getColor());
            canvas.drawRect(barsRects[i], rectPaint);
        }
    }

    public void drawBarValues(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            canvas.drawText((Math.round((barValues[i] * 1000) / 1000) + ""),
                    barWidth + i * (barWidth + intervalWidth) + barWidth / 2, barsRects[i].top - 30, textPaint);

        }
    }

    /**
     * 设置横向valueLine的个数
     *
     * @param count 用户输入个数，默认3个
     */
    public void setValueLineCount(int count) {
        valueLineCount = count;
        invalidate();
    }

    /**
     * 设置最大value，就是最上面的ValueLine的值
     *
     * @param value 用户输入的value
     */
    public void setTotalValue(float value) {
        totalValue = value;
        invalidate();

    }


    public void startAnimator() {
        for (int i = 0; i < mData.size(); i++) {
            ValueAnimator valueAnimatorA = ValueAnimator.ofFloat(0f, 1);
            valueAnimatorA.setInterpolator(new LinearInterpolator());
            valueAnimatorA.setDuration(1000);
            final int finalI = i;
            valueAnimatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    length[finalI] = (float) animation.getAnimatedValue() * mData.get(finalI).getViewHeight();
                    barValues[finalI] = (float) animation.getAnimatedValue() * mData.get(finalI).getValue();
                    Log.e("tag", length[finalI] + "");
                    invalidate();
                }
            });

            valueAnimatorA.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float[] pts = new float[2];
//        pts[0] = event.getRawX();
//        pts[1] = event.getRawY();
        pts[0] = event.getX();
        pts[1] = event.getY();
        mMapMatrix.mapPoints(pts);


        int x = (int) pts[0];
        int y = (int) pts[1];
        Log.e("simplebarView", "onTouch x:" + x + "y:" + y);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int position = getClickedBar(x, y);
                mListener.onBarClicked(position);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        invalidate();
        return true;
    }

    public int getClickedBar(int x, int y) {
        for (int i = 0; i < mData.size(); i++) {
            if (barsRects[i].contains(x, y)) {
                return i;
            }


        }
        return -1;
    }

    public void setListener(BarListener listener) {
        mListener = listener;
    }


    /**
     * 点击事件监听接口
     */
    public interface BarListener {
        void onBarClicked(int position);
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

        public Builder setValueLineCount(int count) {
            mView.setValueLineCount(count);
            return this;
        }

        public Builder setData(ArrayList<BarData> list) {
            mView.setData(list);
            return this;
        }

    }
}
