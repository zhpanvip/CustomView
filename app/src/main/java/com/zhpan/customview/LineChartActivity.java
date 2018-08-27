package com.zhpan.customview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhpan.custom_line_chart.LineChartView;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity extends AppCompatActivity {
    private LineChartView mLineChartView;
    private List<LineChartView.ItemBean> mItems;
    private int[] shadeColors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        initView();
        initData();
    }

    private void initData() {
        //  初始化折线数据
        mItems = new ArrayList<>();
        mItems.add(new LineChartView.ItemBean(1489507200, 23));
        mItems.add(new LineChartView.ItemBean(1489593600, 88));
        mItems.add(new LineChartView.ItemBean(1489680000, 60));
        mItems.add(new LineChartView.ItemBean(1489766400, 50));
        mItems.add(new LineChartView.ItemBean(1489852800, 70));
        mItems.add(new LineChartView.ItemBean(1489939200, 10));
        mItems.add(new LineChartView.ItemBean(1490025600, 33));
        mItems.add(new LineChartView.ItemBean(1490112000, 44));
        mItems.add(new LineChartView.ItemBean(1490198400, 99));
        mItems.add(new LineChartView.ItemBean(1490284800, 17));

        shadeColors= new int[]{
                Color.argb(100, 255, 86, 86), Color.argb(15, 255, 86, 86),
                Color.argb(0, 255, 86, 86)};

        //  设置折线数据
        mLineChartView.setItems(mItems);
        //  设置渐变颜色
        mLineChartView.setShadeColors(shadeColors);
        //  开启动画
        mLineChartView.startAnim(mLineChartView,2000);
    }

    public void onClick(View view){
        //  开启动画
        mLineChartView.startAnim(mLineChartView,2000);
    }

    private void initView() {
        mLineChartView = (LineChartView) findViewById(R.id.lcv);
    }
}
