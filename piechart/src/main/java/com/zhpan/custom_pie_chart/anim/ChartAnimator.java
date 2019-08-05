
package com.zhpan.custom_pie_chart.anim;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.view.animation.LinearInterpolator;


/**
 * 属性动画
 * @author ssw
 * @time 2015年12月4日16:53:42
 */

public class ChartAnimator {

    private AnimatorUpdateListener mListener;

    public ChartAnimator() {

    }

    public ChartAnimator(AnimatorUpdateListener animatorUpdateListener) {

        mListener = animatorUpdateListener;
    }

    protected float mPhaseY = 1f;

    protected float mPhaseX = 1f;

    public void animateXY(int durationMillisX, int durationMillisY, TimeInterpolator easingX,
                          TimeInterpolator easingY) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(easingY);
        animatorY.setDuration(
                durationMillisY);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(easingX);
        animatorX.setDuration(
                durationMillisX);

        // make sure only one animator produces update-callbacks (which then
        // call invalidate())
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(mListener);
        } else {
            animatorY.addUpdateListener(mListener);
        }

        animatorX.start();
        animatorY.start();
    }

    public void animateX(int durationMillis, TimeInterpolator easing) {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(mListener);
        animatorX.start();
    }


    public void animateY(int durationMillis, TimeInterpolator easing) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setInterpolator(easing);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }



    public void animateXY(int durationMillisX, int durationMillisY ,TimeInterpolator easing) {

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillisY);
        animatorY.setInterpolator(easing);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setInterpolator(easing);
        animatorX.setDuration(
                durationMillisX);
        if (durationMillisX > durationMillisY) {
            animatorX.addUpdateListener(mListener);
        } else {
            animatorY.addUpdateListener(mListener);
        }

        animatorX.start();
        animatorY.start();
    }


    public void animateX(int durationMillis) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillis);
        animatorX.addUpdateListener(mListener);
        animatorX.start();
    }


    public void animateY(int durationMillis) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillis);
        animatorY.addUpdateListener(mListener);
        animatorY.start();
    }


    public float getPhaseY() {
        return mPhaseY;
    }


    public void setPhaseY(float phase) {
        mPhaseY = phase;
    }


    public float getPhaseX() {
        return mPhaseX;
    }


    public void setPhaseX(float phase) {
        mPhaseX = phase;
    }
}
