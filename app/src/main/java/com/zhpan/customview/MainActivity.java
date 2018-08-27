package com.zhpan.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhpan.custom_line_chart.LineChartView;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick({R.id.btn_line_chart, R.id.btn_lock_view,
            R.id.btn_pie_chart, R.id.btn_progress_view})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_line_chart:
                intent = new Intent(this, LineChartActivity.class);
                break;
            case R.id.btn_pie_chart:
                intent = new Intent(this, PieChatActivity.class);
                break;
            case R.id.btn_lock_view:
                intent = new Intent(this, LockViewActivity.class);
                break;
            case R.id.btn_progress_view:
                intent = new Intent(this, ProgressViewActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }
}
