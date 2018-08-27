package com.zhpan.custom_circle_progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhpan.library.DensityUtils;

/**
 * create by zhpan on 2018/8/24 18:49
 * Describe:
 */
public class CircleProgress extends View {

    private int centerX;
    private int centerY;
    private int mRadius;
    private int circleColor;
    private int progress;
    private int progressColor;
    private int ringColor;
    private int ringWidth;
    private int textColor;
    private float textSize;
    private int maxProgress;
    private boolean showText;
    private Paint mPaint;
    private Context mContext;
    private int dp_10;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
        progressColor = typedArray.getColor(R.styleable.CircleProgress_ring_color, context.getResources().getColor(R.color.green));
        ringColor = typedArray.getColor(R.styleable.CircleProgress_ring_color, context.getResources().getColor(R.color.ring_color));
        textColor = typedArray.getColor(R.styleable.CircleProgress_text_color, context.getResources().getColor(R.color.black));
        maxProgress = typedArray.getInteger(R.styleable.CircleProgress_maxProgress, 100);
        showText = typedArray.getBoolean(R.styleable.CircleProgress_text_visible, false);
        textSize = typedArray.getDimension(R.styleable.CircleProgress_text_size, DensityUtils.dp2px(context, 14));
        dp_10 = DensityUtils.dp2px(mContext, 10);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(progressColor);
        RectF rectF = new RectF(centerX-mRadius, centerY-mRadius, centerX+mRadius , centerY+mRadius);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF,270,360*progress/maxProgress,false,mPaint);
    }

    private void drawRing(Canvas canvas) {
        mPaint.setColor(ringColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(ringWidth);
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        int min = Math.min(getWidth() / 2, getHeight() / 2);
        mRadius = min - min / 10;
        ringWidth=mRadius/9;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
