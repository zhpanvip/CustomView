package com.zhpan.custom_circle_progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhpan.library.DensityUtils;

/**
 * create by zhpan on 2018/8/24 18:49
 * Describe:
 */
public class RingProgressView extends View {
    private int centerX;
    private int centerY;
    private int mRadius;
    private int progress;
    private int progressColor;
    private int ringColor;
    private int ringWidth;
    private int mTextColor;
    private float mTextSize;
    private int maxProgress;
    private boolean showText;
    private Paint mPaint;
    private Context mContext;
    private String mText;
    private Rect bounds;
    private Paint mPaintText;
    private int mRingWidthPercent;

    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        mPaintText = new Paint();
        bounds = new Rect();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView);
        progressColor = typedArray.getColor(R.styleable.RingProgressView_rpv_ring_color, context.getResources().getColor(R.color.green));
        ringColor = typedArray.getColor(R.styleable.RingProgressView_rpv_ring_color, context.getResources().getColor(R.color.ring_color));
        mTextColor = typedArray.getColor(R.styleable.RingProgressView_rpv_text_color, context.getResources().getColor(R.color.green));
        maxProgress = typedArray.getInteger(R.styleable.RingProgressView_rpv_max_progress, 100);
        showText = typedArray.getBoolean(R.styleable.RingProgressView_rpv_text_visible, false);
        mTextSize = typedArray.getDimension(R.styleable.RingProgressView_rpv_text_size, DensityUtils.dp2px(context, 14));
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(mTextColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mRingWidthPercent = 9;
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(progressColor);
        RectF rectF = new RectF(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 270, 360 * progress / maxProgress, false, mPaint);
    }

    private void drawRing(Canvas canvas) {
        mPaint.setColor(ringColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(ringWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        int min = Math.min(getWidth() / 2, getHeight() / 2);
        mRadius = min - min / 10;
        ringWidth = mRadius / mRingWidthPercent;
    }

    /**
     * 画圆中的文字
     */
    private void drawText(Canvas canvas) {
        if (!showText || progress < 0) return;
        mText = progress + "%";
        mPaintText.getTextBounds(mText, 0, mText.length(), bounds);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetricsInt.bottom) / 2 - fontMetricsInt.top;
        canvas.drawText(mText, centerX, baseline, mPaintText);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = DensityUtils.dp2px(mContext, textSize);
    }

    public void setRingWidthPercent(int ringWidthPercent) {
        mRingWidthPercent = ringWidthPercent;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
