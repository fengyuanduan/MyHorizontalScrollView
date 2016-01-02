package com.example.dfy.myapplication;

import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private MyHorizontalScrollVeiw mHorizontalScrollVeiw;
    private HorizontalScrollViewAdapter mAdapter;
    private ImageView mImg;
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.mainpage_tab_discovery_selected,R.drawable.mainpage_tab_message_selected,
            R.drawable.mainpage_tab_mycircle_selected,R.drawable.mainpage_tab_setting_selected,
            R.drawable.mainpage_tab_discovery_normal,R.drawable.mainpage_tab_mycircle_normal,
            R.drawable.mainpage_tab_message_normal,R.drawable.mainpage_tab_setting_normal,
            R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d

    ));

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImg = (ImageView) findViewById(R.id.id_content);
        mHorizontalScrollVeiw = (MyHorizontalScrollVeiw) findViewById(R.id.id_horizontalscrollview);
        mAdapter = new HorizontalScrollViewAdapter(this, mDatas);

        //添加滚动回调
        mHorizontalScrollVeiw.setOnCurrentImageChangeListener(new MyHorizontalScrollVeiw.CurrentImageChangeListener() {
            @Override
            public void onCurrentChanged(int position, View viewIndicator) {
                mImg.setImageResource(mDatas.get(position));
                viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });

        //添加点击回调
        mHorizontalScrollVeiw.setOnItemClickListener(new MyHorizontalScrollVeiw.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mImg.setImageResource(mDatas.get(position));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));

            }

        });
        //设置适配器
        mHorizontalScrollVeiw.initData(mAdapter);
    }


}

