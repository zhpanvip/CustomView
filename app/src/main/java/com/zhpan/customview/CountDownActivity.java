package com.zhpan.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.countdown.CountdownView;
import com.zhpan.library.DensityUtils;

public class CountDownActivity extends AppCompatActivity {
    CountdownView mCountdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        mCountdownView = findViewById(R.id.count_down);

        mCountdownView.setTimestamp(100*1000 * 5);
        mCountdownView.setCountdownPrefix("距离开始时间：");
        mCountdownView.setTextSize(DensityUtils.dp2px(this,16));
        mCountdownView.setPrefixColor(getResources().getColor(R.color.colorPrimary));
        mCountdownView.setShowSecond(true);
        mCountdownView.setShowHours(true);
        mCountdownView.startCountdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountdownView != null)
            mCountdownView.stopCountdown();
    }
}
