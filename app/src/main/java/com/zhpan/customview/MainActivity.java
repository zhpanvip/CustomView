package com.zhpan.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
            case R.id.btn_wheel_view:
                intent = new Intent(this, WheelViewActivity.class);
                break;
            case R.id.btn_countdown:
                intent = new Intent(this, CountDownActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }
}
