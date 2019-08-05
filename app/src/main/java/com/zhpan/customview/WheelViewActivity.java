package com.zhpan.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cypoem.wheel_view.WheelView;

import java.util.ArrayList;

public class WheelViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            dataList.add("" + i);
        WheelView wheelView = findViewById(R.id.wv_main);
        wheelView.setDataList(dataList);
        wheelView.setCircle(true);
    }
}
