package com.zhpan.customview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


import com.zhpan.custom_line_chart.LineChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {
    private LineChartView mLineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        initView();
        initData();
    }

    private void initData() {
        //  初始化折线数据
        List<Float> listValues = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            float v = random.nextFloat() * 100;
            Log.e("lll", v + "");
            listValues.add(v);
        }

        List<Integer> listShadeColors = new ArrayList<>();
        listShadeColors.add(Color.argb(100, 255, 86, 86));
        listShadeColors.add(Color.argb(15, 255, 86, 86));
        listShadeColors.add(Color.argb(0, 255, 86, 86));
        //  设置折线数据
        mLineChartView.setValues(listValues);
        //  设置渐变颜色
        mLineChartView.setShadeColors(listShadeColors);
        //  设置动画插值器
        mLineChartView.setInterpolator(new DecelerateInterpolator());
        //  开启动画
        mLineChartView.startAnim(2500);
    }

    public void onClick(View view) {
        mLineChartView.startAnim(2000);
    }

    private void initView() {
        mLineChartView = findViewById(R.id.lcv);
    }
}
