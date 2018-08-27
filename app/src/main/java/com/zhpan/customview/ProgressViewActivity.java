package com.zhpan.customview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhpan.custom_circle_progress.CircleProgress;

public class ProgressViewActivity extends AppCompatActivity {

    private CircleProgress mCircleProgress;
    private int progress;
    private static Handler mHandler = new Handler();
    private boolean isRunning;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            progress++;
            mCircleProgress.setProgress(progress);
            if (progress < 100) {
                mHandler.postDelayed(this, 30);
            } else {
                isRunning = false;
                mHandler.removeCallbacks(this);
            }
        }
    };

    private Runnable mRunnableReset = new Runnable() {
        @Override
        public void run() {
            progress--;
            mCircleProgress.setProgress(progress);
            if (progress > 0) {
                mHandler.postDelayed(this, 3);
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
        mHandler.postDelayed(mRunnableReset,3);
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
