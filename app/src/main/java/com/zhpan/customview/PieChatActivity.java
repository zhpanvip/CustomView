package com.zhpan.customview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhpan.custom_pie_chart.PieChartView;

import java.util.ArrayList;
import java.util.List;

public class PieChatActivity extends AppCompatActivity {
    private List<PieChartView.PieItemBean> mList;
    private PieChartView mPieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chat);

        initView();
        initData();
        setData();
    }

    private void setData() {
        mPieChartView.setItems(mList);
        mPieChartView.setText("资产分配");
        mPieChartView.setTextColor(Color.parseColor("#333333"));
        mPieChartView.setTextSize(14);
    }

    private void initData() {
        mList = new ArrayList<>();
        String cArray[] = {"#FF5656", "#FFAE66", "#FF9BA4", "#519FFC", "#E15798", "#25CECB"};
        mList.add(new PieChartView.PieItemBean("股票", Color.parseColor(cArray[0]), 60));
        mList.add(new PieChartView.PieItemBean("债券", Color.parseColor(cArray[1]), 20));
        mList.add(new PieChartView.PieItemBean("活期", Color.parseColor(cArray[2]), 120));
        mList.add(new PieChartView.PieItemBean("定期", Color.parseColor(cArray[3]), 70));
        mList.add(new PieChartView.PieItemBean("商品", Color.parseColor(cArray[4]), 90));
    }

    private void initView() {
        mPieChartView = findViewById(R.id.pcv);
    }
}
