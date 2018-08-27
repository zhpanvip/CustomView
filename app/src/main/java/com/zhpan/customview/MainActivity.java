package com.zhpan.customview;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhpan.custom_circle_progress.CircleProgress;

public class MainActivity extends AppCompatActivity {
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
                mHandler.postDelayed(this, 300);
            } else {
                isRunning = false;
                mHandler.removeCallbacks(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleProgress = findViewById(R.id.cp_view);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (!isRunning) {
                    mHandler.postDelayed(mRunnable, 30);
                }
                isRunning = true;
                break;

            case R.id.btn_stop:
                mHandler.removeCallbacks(mRunnable);
                isRunning = false;
                break;
            case R.id.btn_reset:
                progress=0;
                mCircleProgress.setProgress(0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
