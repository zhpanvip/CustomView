package com.android.countdown;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhpan.library.DensityUtils;

/**
 * <pre>
 *   Created by zhangpan on 2019-08-20.
 *   Description:
 * </pre>
 */
public class CountdownView extends View {

    int MILL_IN_ONE_SEC = 1000;
    int MILL_IN_ONE_MIN = 60 * MILL_IN_ONE_SEC;
    long MILL_IN_ONE_HOUR = MILL_IN_ONE_MIN * 60;
    long MILL_IN_ONE_DAY = MILL_IN_ONE_HOUR * 24;
    int SEC_IN_ONE_MIN = 60;
    int MIN_IN_ONE_HOUR = 60;

    private String countdownPrefix;

    private String countdownText;

    private long timestamp;

    private Paint mPaint;

    private int countdownTextColor;

    private int prefixColor;

    private String mDaySuffix;

    private String mHourSuffix;

    private String mMinuteSuffix;

    private String mSecondSuffix;

    private boolean showDay;

    private boolean showHours;

    private boolean showSecond;

    private long interval = MILL_IN_ONE_SEC;

    private CountDownTimer mCountDownTimer;

    private int textGap;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        countdownTextColor = Color.BLACK;
        prefixColor = Color.BLACK;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textGap = DensityUtils.dp2px(getContext(), 5);
        setTextSize(DensityUtils.dp2px(getContext(), 15));
        mDaySuffix = getResources().getString(R.string.countdown_day);
        mHourSuffix = getResources().getString(R.string.countdown_hour);
        mMinuteSuffix = getResources().getString(R.string.countdown_minute);
        mSecondSuffix = getResources().getString(R.string.countdown_second);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMeasureSpecMode == MeasureSpec.AT_MOST
                && heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                    getContentMeasureWidth(countdownPrefix + countdownText, widthMeasureSpecSize),
                    getContentMeasureHeight());
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                    (getContentMeasureWidth(countdownPrefix + countdownText, widthMeasureSpecSize)),
                    heightMeasureSpecSize);
        } else if (heightMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpecSize, getContentMeasureHeight());
        }
    }

    private int getContentMeasureWidth(String content, int widthMeasureSpecSize) {
        return TextUtils.isEmpty(content)
                ? widthMeasureSpecSize
                : (int) mPaint.measureText(content) + textGap;
    }

    private int getContentMeasureHeight() {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        return (int) (fontMetrics.bottom - fontMetrics.top) + 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(countdownPrefix)) {
            Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            mPaint.setTypeface(typeface);
            mPaint.setColor(prefixColor);
            canvas.drawText(countdownPrefix, 0, getHeight() / 2f + 10, mPaint);
        }
        if (!TextUtils.isEmpty(countdownText)) {
            Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
            mPaint.setTypeface(typeface);
            mPaint.setColor(countdownTextColor);
            float startX = TextUtils.isEmpty(countdownPrefix)
                    ? 0
                    : mPaint.measureText(countdownPrefix) + textGap;
            canvas.drawText(countdownText, startX, getHeight() / 2f + 10, mPaint);
        }
    }

    private String getTimeString(long timestamp) {
        if (!showSecond) {
            timestamp += MILL_IN_ONE_MIN;
        }
        int day = (int) (timestamp / (MILL_IN_ONE_HOUR * 24));
        int hour = (int) ((timestamp % MILL_IN_ONE_DAY) / MILL_IN_ONE_HOUR);
        int minute = (int) ((timestamp % MILL_IN_ONE_HOUR) / MILL_IN_ONE_MIN);
        int second = (int) ((timestamp % MILL_IN_ONE_MIN) / MILL_IN_ONE_SEC);
        String timeString = minute + mMinuteSuffix;
        if (day > 0 || hour > 0 || showHours) {
            timeString = hour + mHourSuffix + timeString;
        }
        if (day > 0 || showDay) {
            timeString = day + mDaySuffix + timeString;
        }
        if (showSecond) {
            timeString = timeString + second + mSecondSuffix;
        }
        return timeString;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        stopCountdown();
        startCountdown();
    }

    public void startCountdown() {
        mCountDownTimer = new CountDownTimer(timestamp + 100, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTimestamp(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (mOnCountDownFinishListener != null) {
                    mOnCountDownFinishListener.onCountDownFinish();
                }
                setTimestamp(0);
            }
        }.start();
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
    }

    public void setCountdownPrefix(String countdownPrefix) {
        this.countdownPrefix = countdownPrefix;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        countdownText = getTimeString(timestamp);
        requestLayout();
        invalidate();
    }


    public void stopCountdown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void setDaySuffix(String daySuffix) {
        mDaySuffix = daySuffix;
    }

    public void setHourSuffix(String hourSuffix) {
        mHourSuffix = hourSuffix;
    }

    public void setMinuteSuffix(String minuteSuffix) {
        mMinuteSuffix = minuteSuffix;
    }

    public void setSecondSuffix(String secondSuffix) {
        mSecondSuffix = secondSuffix;
    }

    public void setShowDay(boolean showDay) {
        this.showDay = showDay;
    }

    public void setShowHours(boolean showHours) {
        this.showHours = showHours;
    }

    public void setShowSecond(boolean showSecond) {
        this.showSecond = showSecond;
    }

    public void setPrefixColor(@ColorInt int prefixColor) {
        this.prefixColor = prefixColor;
    }

    public void setCountdownTextColor(@ColorInt int countdownTextColor) {
        this.countdownTextColor = countdownTextColor;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    private OnCountDownFinishListener mOnCountDownFinishListener;

    public void setOnCountDownFinishListener(OnCountDownFinishListener onCountDownFinishListener) {
        mOnCountDownFinishListener = onCountDownFinishListener;
    }

    public void removeCountDownFinishListener() {
        mOnCountDownFinishListener = null;
    }

    public interface OnCountDownFinishListener {
        void onCountDownFinish();
    }
}
