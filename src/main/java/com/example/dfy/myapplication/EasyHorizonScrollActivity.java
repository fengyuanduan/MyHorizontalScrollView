package com.example.dfy.myapplication;

/**
 * HorizontalScrollView 应用
 *
 * 初级代码
 *
 * 与其对应的UI是activity_easy_main
 *
 */

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


/**
 * Created by Administrator on 2016/1/1.
 */
public class EasyHorizonScrollActivity extends ActionBarActivity{
    private LayoutInflater mLayout;
    private LinearLayout mGallery;
    private int[] imgIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_main);
        initData();
        initView();

    }

    private void initView() {

        mLayout = LayoutInflater.from(this);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

        for (int i = 0; i < imgIds.length; i++) {
            View view=  mLayout.inflate(R.layout.layout_item, mGallery, false);
            ImageView image= (ImageView) view.findViewById(R.id.id_item_image);
            image.setImageResource(imgIds[i]);
            TextView text = (TextView) view.findViewById(R.id.id_item_text);
            text.setText("图片信息");
            mGallery.addView(view);
        }
    }

    private void initData() {
        imgIds = new int[]{
                R.drawable.mainpage_tab_discovery_selected,R.drawable.mainpage_tab_message_selected,
                R.drawable.mainpage_tab_mycircle_selected,R.drawable.mainpage_tab_setting_selected,
                R.drawable.mainpage_tab_discovery_normal,R.drawable.mainpage_tab_mycircle_normal,
                R.drawable.mainpage_tab_message_normal,R.drawable.mainpage_tab_setting_normal,
                R.drawable.mainpage_tab_discovery_selected,R.drawable.mainpage_tab_message_selected,
        };
    }


}
