package cn.henry.zhuhao.animatordemo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

import cn.henry.zhuhao.animatordemo.entity.PieData;
import cn.henry.zhuhao.animatordemo.utils.ColorUtils;
import cn.henry.zhuhao.animatordemo.utils.GeomTool;

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

    private int currentFlag = -2;

    //动画支持数据。
    private float[] angles;//实时数据
    private float[] angles1;//静态数据

    //region
    private Region globalRegion;

    private Region[] regions;
    private Path[] paths;
    //Pie图的半径
    private float r;
    //pie图内圆半径
    private float nr;
    //pie点击半径
    private float cr;

    //pie图外切圆
    private RectF rectBig;
    //pie内部圆
    private RectF rectSmall;
    private PieListener mListener;
    //pie点击圆
    private RectF rectClick;

    private Matrix mMapMatrix;


    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mMapMatrix = new Matrix();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMapMatrix.reset();
        mWidth = w;
        mHeight = h;
        initCommons();
        for (int i = 0; i < mData.size(); i++) {
            initRegion(i);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData) {
            return;
        }
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.getMatrix().invert(mMapMatrix);
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rectBig, mData.get(i).getStartangle(), angles[i], true, mPaint);
            drawText(canvas, i);
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawOval(rectSmall, mPaint);
        if (currentFlag == -2) {

        } else {
            onClickAnimator(canvas, currentFlag);
        }


    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle 用户传入
     */
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    /**
     * 设置数据
     *
     * @param mData 用户初始化数据
     */
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();   // 刷新
    }

    /**
     * 初始化数据
     *
     * @param mData 用户初始化数据
     */
    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;
        angles = new float[mData.size()];
        angles1 = new float[mData.size()];
        regions = new Region[mData.size()];
        paths = new Path[mData.size()];
        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {

            PieData pie = mData.get(i);


            sumValue += pie.getValue();       //计算数值和
            if (pie.getColor() == 0) {
                int j = i % mColors.length;       //设置颜色
                pie.setColor(mColors[j]);
            }
        }

        float currentAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度
            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
            pie.setStartangle(currentAngle);
            angles1[i] = pie.getAngle();//获取角度
            currentAngle += angle;
            Log.e("pieview", mData.get(i).getStartangle() + "start");
        }
    }

    /**
     * 设置动画
     */
    public void startAnimator() {
        for (int i = 0; i < mData.size(); i++) {
            ValueAnimator valueAnimatorA = ValueAnimator.ofFloat(0f, angles1[i]);
            valueAnimatorA.setInterpolator(new LinearInterpolator());
            valueAnimatorA.setDuration(1000);
            final int finalI = i;
            valueAnimatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    angles[finalI] = (float) animation.getAnimatedValue();
                    Log.e("tag", angles[finalI] + "");
                    invalidate();
                }
            });

            valueAnimatorA.start();
        }
    }

    /**
     * 设置常量
     */
    private void initCommons() {
        r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
        nr = r * 3 / 8;
        cr = 9 * r / 8;
        rectBig = new RectF(-r, -r, r, r);
        rectSmall = new RectF(-nr, -nr, nr, nr);
        rectClick = new RectF(-cr, -cr, cr, cr);
        globalRegion = new Region(-mWidth, -mHeight, mWidth, mHeight);
    }

    /**
     * 初始化region区域
     *
     * @param position posotion
     */
    public void initRegion(int position) {
        paths[position] = new Path();
        regions[position] = new Region();
        PieData pie = mData.get(position);
        mPaint.setColor(pie.getColor());
        paths[position].addArc(rectBig, mData.get(position).getStartangle(), mData.get(position).getAngle());
        if (position < mData.size() - 1) {
            paths[position].arcTo(rectSmall, mData.get(position + 1).getStartangle(), -angles1[position]);
        } else {
            paths[position].arcTo(rectSmall, mData.get(0).getStartangle(), -angles1[position]);
        }
        paths[position].close();
        regions[position].setPath(paths[position], globalRegion);
    }


    /**
     * 获取点击位置
     *
     * @param x x坐标
     * @param y y坐标
     * @return posotion
     */
    public int getTouchedPath(int x, int y) {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains(x, y)) {
                currentFlag = i;
                return i;
            }
        }
        currentFlag = -2;
        return -1;
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
        Log.e("pieview", "onTouch x:" + x + "y:" + y);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int position = getTouchedPath(x, y);
                mListener.onPieClicked(position);
                // Toast.makeText(getContext(), position + "pieClicked", Toast.LENGTH_SHORT).show();
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

    private void drawText(Canvas canvas, int position) {
        PieData pie = mData.get(position);
        float value=pie.getPercentage();
        TextPaint textPaint = new TextPaint();
        //不换行
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        mPaint.setTextSize(40);
//        mPaint.setColor(Color.BLACK);
//        canvas.drawText(pie.getName()+(pie.getPercentage()*100)+"%",getCenter(pie.getStartangle(),pie.getAngle(),nr,r).x,
//        getCenter(pie.getStartangle(),pie.getAngle(),nr,r).y,mPaint);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTextSize(40.0F);
        textPaint.setAntiAlias(true);
        StaticLayout layout = new StaticLayout(((float)(Math.round(value*1000))/1000* 100) + "%\r\n" + pie.getName(),
                textPaint, 1000, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate(-mWidth / 2,0);
        canvas.translate(getCenter(pie.getStartangle(), pie.getAngle(), nr, r).x,
                getCenter(pie.getStartangle(), pie.getAngle(), nr, r).y);
        layout.draw(canvas);
        canvas.restore();

    }

    private Point getCenter(float startAngle, float sweepAngle, float nr, float r) {
        Point centerPoint = new Point();
        float centerr = (nr + r) / 2;
        float angle = startAngle + sweepAngle / 2;
        GeomTool.calcCirclePoint((int) angle, centerr, 0, 0, centerPoint);
        return centerPoint;
    }

    private void onClickAnimator(Canvas canvas, int position) {
        mPaint.setColor(ColorUtils.getColorWithAlpha(mData.get(position).getColor(), 0.5f));
        Path clickPath = new Path();
        clickPath.addArc(rectClick, mData.get(position).getStartangle(), mData.get(position).getAngle());
        if (position < mData.size() - 1) {
            clickPath.arcTo(rectBig, mData.get(position + 1).getStartangle(), -angles1[position]);
        } else {
            clickPath.arcTo(rectBig, mData.get(0).getStartangle(), -angles1[position]);
        }
        canvas.drawPath(clickPath, mPaint);

    }

    public void setListener(PieListener listener) {
        mListener = listener;
    }


    public interface PieListener {
        void onPieClicked(int position);
    }

}
