package cn.henry.zhuhao.animatordemo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

import cn.henry.zhuhao.animatordemo.entity.PieData;

/**
 * Created by HenryZhuhao on 2017/6/1.
 */

public class PieView extends View {

    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<PieData> mData;
    // 宽高
    private int mWidth, mHeight;
    // 画笔
    private Paint mPaint = new Paint();


    //动画支持数据。
    public float[] angles;
    public float[] angles1;

    public PieView(Context context) {
        this(context,null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData) {
            return;
        }
        float currentStartAngle = mStartAngle;                    // 当前起始角度
        canvas.translate(mWidth / 2, mHeight / 2);
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
        RectF rect = new RectF(-r, -r, r, r);                     // 饼状图绘制区域

        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rect, currentStartAngle,angles[i], true, mPaint);
            currentStartAngle += pie.getAngle();
        }
        mPaint.setColor(Color.WHITE);
        float rn=r*2/7;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(-rn,-rn,rn,rn,mPaint);
        }

    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    // 设置数据
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();   // 刷新
    }

    // 初始化数据
    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;
        angles = new float[mData.size()];
        angles1 = new float[mData.size()];
        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {

            PieData pie = mData.get(i);


            sumValue += pie.getValue();       //计算数值和

            int j = i % mColors.length;       //设置颜色
            pie.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度
            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
            angles1[i] = pie.getAngle();     //获取角度

            sumAngle += angle;

            Log.i("angle", "" + pie.getAngle());
        }
    }


    public void startAnimator() {
        for (int i = 0; i < mData.size(); i++) {
            ValueAnimator valueAnimatorA = ValueAnimator.ofFloat(0f,angles1[i]);
            valueAnimatorA.setInterpolator(new LinearInterpolator());
            valueAnimatorA.setDuration(1000);
            final int finalI = i;
            valueAnimatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    angles[finalI]= (float) animation.getAnimatedValue();
                    Log.e("tag",angles[finalI]+"");
                    invalidate();
                }
            });
           valueAnimatorA.start();
        }
    }


}
