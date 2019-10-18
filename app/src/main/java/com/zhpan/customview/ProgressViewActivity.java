package com.zhpan.customview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.zhpan.custom_circle_progress.CircleProgressView;

public class ProgressViewActivity extends AppCompatActivity {

    private CircleProgressView mCircleProgress;
    private int progress;
    private static Handler mHandler = new Handler();
    private boolean isRunning;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mCircleProgress.setProgress(progress);
            if (progress < 100) {
                progress++;
                mHandler.postDelayed(this, 60);
            } else {
                isRunning = false;
                mHandler.removeCallbacks(this);
            }
        }
    };

    private Runnable mRunnableReset = new Runnable() {
        @Override
        public void run() {
            mCircleProgress.setProgress(progress);
            if (progress > 0) {
                progress--;
                mHandler.postDelayed(this, 1);
            } else {
                isRunning = false;
                mHandler.removeCallbacks(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_view);
        mCircleProgress = findViewById(R.id.cp_view);
        mCircleProgress.setShowText(true);
        mCircleProgress.setRingWidthPercent(7);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_line_chart:
                start();
                break;
            case R.id.btn_pie_chart:
                stop();
                break;
            case R.id.btn_lock_view:
                reset();
                break;
        }
    }

    private void reset() {
        stop();
        mHandler.postDelayed(mRunnableReset,1);
    }

    private void start() {
        if (!isRunning) {
            mHandler.postDelayed(mRunnable, 30);
        }
        isRunning = true;
    }

    private void stop() {
        mHandler.removeCallbacks(mRunnable);
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
