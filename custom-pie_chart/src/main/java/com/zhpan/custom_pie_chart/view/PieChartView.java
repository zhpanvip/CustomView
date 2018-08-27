package com.zhpan.custom_pie_chart.view;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zhpan.custom_pie_chart.anim.ChartAnimator;
import com.zhpan.custom_pie_chart.module.PieItemBean;
import com.zhpan.library.DensityUtils;
import com.zhpan.library.ScreenUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zhpan on 2017/3/16.
 */
public class PieChartView extends View {

    private Context context;

    /**
     * 画笔去 画 文字 扇形 线
     */
    private Paint textPaint, piePaint, linePaint, midPaint;

    /**
     * 扇形的 圆心坐标 半径
     */
    private int pieCenterX, pieCenterY, pieRadius;

    /**
     * 矩形
     */
    private RectF pieOval, rectF;

    private double totalValue;

    private ChartAnimator mAnimator;

    private Path path = new Path();

    public String mText;

    public Paint mPaintBottom;

    private float mBottomRadius;
    private float mBottomRingWidth;
    private float mTopRadius;

    private float mAlphRadius;

    private float dp_5, dp_10;

    private List<PieItemBean> mPieItems;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initRadius(context);
        initData(context);
    }


    private void initRadius(Context context) {
        dp_10 = DensityUtils.dp2px(context, 10);
        dp_5 = DensityUtils.dp2px(context, 5);
        // 获取屏幕的 宽高
        int screenW = ScreenUtils.getScreenW(context);
        int screenH = ScreenUtils.getScreenH(context);
        // 计算圆心
        pieCenterX = screenW / 3;
        pieCenterY = screenH / 5;
        // 计算半径（扇形）
        pieRadius = screenW / 5;
        mBottomRadius = pieRadius + DensityUtils.dp2px(context, 5);
        mBottomRingWidth = pieRadius - mBottomRingWidth / 2;

        mAlphRadius = pieRadius / 3 * 2;
        mTopRadius = pieRadius / 3 * 2 - dp_10 / 2;
        pieOval = new RectF(pieCenterX - pieRadius, pieCenterY - pieRadius,
                pieCenterX + pieRadius, pieCenterY + pieRadius);
    }

    private void initData(Context context) {
        // The paint to draw text.
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtils.dp2px(context, 13f));

        // The paint to draw circle.
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);
        mPaintBottom = new Paint();
        // The paint to draw line to show the concrete text
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(DensityUtils.dp2px(context, 0.5f));

        midPaint = new Paint();

        mAnimator = new ChartAnimator(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        // 圆角矩形背景
        rectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  最底部圆环
        drawBottomCircle(canvas);
        //  画扇形圆环和百分比
        drawArc(canvas);
        //  画上层圆
        drawTopCircle(canvas);
        //  画中心文字
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(mText)) return;
        Rect bounds = new Rect();
        midPaint.getTextBounds(mText, 0, mText.length(), bounds);
        midPaint.setTextSize(DensityUtils.dp2px(context, 13f));
        Paint.FontMetricsInt fontMetricsInt = midPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetricsInt.bottom) / 2 - fontMetricsInt.top;
        canvas.drawText(mText, pieCenterX - midPaint.measureText(mText.substring(0, mText.length() / 2)) - dp_5, baseline, midPaint);
    }

    //  画中心文字
   /* private void drawText(Canvas canvas) {
        midPaint.setColor(Color.parseColor("#333333"));
        midPaint.setTextSize(DensityUtils.dp2px(context, 13f));
        canvas.drawText(midString, pieCenterX - midPaint.measureText(midString.substring(0, midString.length() / 2)),
                pieCenterY + 10, midPaint);
    }*/

    //  画上层圆
    private void drawTopCircle(Canvas canvas) {
        piePaint.setColor(Color.parseColor("#4c7F7F7F"));
        canvas.drawCircle(pieCenterX, pieCenterY, mAlphRadius, piePaint);

        piePaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(pieCenterX, pieCenterY, mTopRadius, piePaint);
    }

    private void drawBottomCircle(Canvas canvas) {
        piePaint.setColor(Color.parseColor("#f1f1f1"));
//        mPaintBottom.setStyle(Paint.Style.STROKE);
//        mPaintBottom.setStrokeWidth(mBottomRingWidth + dp_10);
        // 画最底部园
        canvas.drawCircle(pieCenterX, pieCenterY, mBottomRadius, piePaint);
    }

    //  画扇形圆环和百分比
    private void drawArc(Canvas canvas) {
        // 起始的角度
        float start = 0;
        if (mPieItems != null && mPieItems.size() > 0) {
            // 获取文字的高度
            FontMetrics fm = textPaint.getFontMetrics();
            float TypeTextLen = (float) Math.ceil(fm.descent - fm.ascent);
            // 计算百分比文字在Y轴最上部的起始绘制坐标
            float txtY = pieCenterY
                    - (mPieItems.size() * TypeTextLen + (mPieItems.size() - 1)
                    * dp_10) / 2 + TypeTextLen / 2;

            // 遍历
            for (int i = 0; i < mPieItems.size(); i++) {
                // 设置画笔颜色
                piePaint.setColor(mPieItems.get(i).getColor());
                textPaint.setColor(Color.parseColor("#333333"));
                linePaint.setColor(mPieItems.get(i).getColor());
                textPaint.setAlpha((int) (255 * mAnimator.getPhaseX()));

                float sweep = (float) ((mPieItems.get(i).getItemValue()) / totalValue * 360);
                // 画 扇形
                canvas.drawArc(pieOval, start * mAnimator.getPhaseX(), sweep
                        * mAnimator.getPhaseY(), true, piePaint);

                // 获取每项对于的文字
                String itemTypeText = mPieItems.get(i).getItemType() + ":";
                // 获取百分比
                String percent = formatFloat(mPieItems.get(i).getItemValue() / totalValue * 100);
                String itemPercentText = (percent) + "%";

                float left = pieCenterX + pieRadius + 3 * dp_10;
                float top = txtY + i * (TypeTextLen + dp_10) - (float) 1.2
                        * dp_10 + (float) 0.2 * dp_10;
                float right = pieCenterX + pieRadius + 3 * dp_10
                        + (float) 1.2 * dp_10;
                float bottom = txtY + i * (TypeTextLen + dp_10)
                        + (float) 0.2 * dp_10;
                // // 绘制圆角矩形
                rectF.left = left;
                rectF.top = top;
                rectF.right = right;
                rectF.bottom = bottom;
                //piePaint.setAlpha((int) (255 * mAnimator.getPhaseX()));
                canvas.drawRoundRect(rectF, 10, 10, piePaint);
                // 绘制类型比例文字
                canvas.drawText(itemTypeText + "   " + itemPercentText, left
                        + 2 * dp_10, txtY + i
                        * (TypeTextLen + dp_10), textPaint);

                linePaint.setAlpha((int) (255 * mAnimator.getPhaseX()));
                canvas.drawPath(path, linePaint);

                start += sweep;
            }
        }
    }


    public String formatFloat(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public void setPieItems(List<PieItemBean> pieItems) {
        this.mPieItems = pieItems;
        for (PieItemBean totalPay : pieItems) {
            totalValue += totalPay.getItemValue();
        }
        invalidate();
    }

    public void startAnim() {
        AnimalXY(2000, 2000, new DecelerateInterpolator());
    }

    public void startAnim(int durationMillisX, int durationMillisY) {
        AnimalXY(durationMillisX, durationMillisY, new DecelerateInterpolator());
    }

    public void startAnim(int durationMillisX, int durationMillisY, TimeInterpolator timeInterpolator) {
        AnimalXY(durationMillisX, durationMillisY, timeInterpolator);
    }

    private void AnimalXY(int durationMillisX, int durationMillisY,
                          TimeInterpolator interpolator) {
        mAnimator.animateXY(durationMillisX, durationMillisY, interpolator);
    }

    public void AnimalY(int durationMillis, TimeInterpolator interpolator) {
        mAnimator.animateY(durationMillis, interpolator);
    }

    public void AnimalX(int durationMillis, TimeInterpolator interpolator) {
        mAnimator.animateX(durationMillis, interpolator);
    }

    public void setText(String text) {
        this.mText = text;
    }
}
