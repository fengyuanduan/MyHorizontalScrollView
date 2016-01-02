package com.example.dfy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public class HorizontalScrollViewAdapter  {

    private Context context;
    private LayoutInflater mInflater;
    private List<Integer> mDatas;
    public HorizontalScrollViewAdapter(Context context,List<Integer>mDatas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;

    }

    public int getCount() {
        return mDatas.size();

    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

   public View getView (int position,View convertView,ViewGroup parent){
       ViewHolder viewHolder=null;
       if (convertView == null) {
           viewHolder = new ViewHolder();
           convertView = mInflater.inflate(R.layout.layout_item, parent, false);
           viewHolder.mImag = (ImageView) convertView.findViewById(R.id.id_item_image);
           viewHolder.mText = (TextView) convertView.findViewById(R.id.id_item_text);
            convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();

       }
       viewHolder.mImag.setImageResource(mDatas.get(position));

       viewHolder.mText.setText("some info");
       return convertView;
   }



    private class ViewHolder{
        ImageView mImag;
        TextView mText;
    }
}
