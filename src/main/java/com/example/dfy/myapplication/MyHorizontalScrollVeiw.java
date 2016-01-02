package com.example.dfy.myapplication;

/**
 * 经过改装后的HorizontalScrollView,再也不用担心OOM
 * 里面通过一个HashMap维护一个View与position的关系，并通过滑动方向，判断增加和删除相应的图片，使Map里只维持一定数量的View
 * View和图片数据和信息的匹配是通过HorzintalScrollViewAdapter完成适配。
 *
 *
 * 设置了两个回调接口OnItemClickListener,CurrentImageChangeListener,(具体实现在MainActivity)
 * 来响应滑动和点击两个事件
 *
 */
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/1.
 */
public class MyHorizontalScrollVeiw extends HorizontalScrollView implements View.OnClickListener {

    /**
     * 图片滚动时的回调接口
     */
    public interface CurrentImageChangeListener {
        void onCurrentChanged(int position, View viewIndicator);
    }

    /**
     * 条目点击时的回调接口
     */

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private CurrentImageChangeListener mListener;
    private OnItemClickListener mOnClickListener;

    private static final String TAG = "MyHoriziontalScrollView";

    /**
     * HorizontalScrollView 中的LinearLayout
     */
    private LinearLayout mContainer;
    /**
     * 子元素的宽
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 当前最后一张图片的下标
     */
    private int mCurrentIndex;

    /**
     * 当前第一张图片的下标
     */
    private int mFirstIndex;
    /**
     * 当前第一个View
     */
    private View mFirstView;

    /**
     * 数据适配器
     */
    private HorizontalScrollViewAdapter mAdapter;

    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen;
    /**
     * 屏幕的宽度
     */
    private int mScreenWidth;
    /**
     * 保存View与Position的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();


    public MyHorizontalScrollVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mContainer = (LinearLayout) getChildAt(0);

    }

    /**
     * 加载下一张图片
     */
    protected void loadNextImg() {
        //边界的判定，是否是最后一张图片
        if (mCurrentIndex == mAdapter.getCount()-1) {
            return;
        }
        //移除第一张图片，且将水平滚动位置置0
        scrollTo(0, 0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);

        //获取下一张图片，并且给图片设置onClick事件，并加入容器
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);

        //当前第一张图片脚标加1
        mFirstIndex++;

        //如果设置了滚动监听则触发，回调
        if (mListener != null) {
            notifyCurrentImgChanged();
        }
    }

    protected void loadPreImg() {
        if(mFirstIndex==0)
            return;

        //计算当前显示的第一个view的下标
        int index = mCurrentIndex - mCountOneScreen;
        if (index >= 0) {
            //移除最后一张图片
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            //将此View放入第一个位置
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);

            scrollTo(mChildWidth, 0);
            // 当前最后一张图片下标--，当前第一个显示的图片下标--
            mCurrentIndex--;
            mFirstIndex--;
            //回调
            if (mListener != null) {
                notifyCurrentImgChanged();

            }

        }
    }

    /**
     * 滑动时的回调
     */
    public void notifyCurrentImgChanged() {
        //先清除所有的背景色,滑动时都设置为白色
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);

        }
        mListener.onCurrentChanged(mFirstIndex,mContainer.getChildAt(0));
    }

    /**
     * 初始化数据，
     * @param mAdapter
     */
    public void initData(HorizontalScrollViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        //获取适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
       // mContainer.addView(view);

        //强制计算当前的View的宽和高

        if (mChildWidth == 0 && mChildHeight == 0) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();

            Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());

            //每屏加载多少个View
            mCountOneScreen = mScreenWidth / mChildWidth +1;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen +
                    ", mChildWidth = " + mChildWidth);

        }

        initFirstScreenChildren(mCountOneScreen);
    }

    /**
     * 初始化View显示
     * @param mCountOneScreen
     */
    private void initFirstScreenChildren(int mCountOneScreen) {
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();

        for (int i = 0; i < mCountOneScreen; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mCurrentIndex = i;
        }
       if (mListener!= null) {
           notifyCurrentImgChanged();
       }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                //如果当前scrollX为view的宽度，加载下一张，移除第一张
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                //如果当前scrollX=0，往前设置一张，移除最后一张
                if(scrollX==0){
                    loadPreImg();
                }
                break;

        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (mOnClickListener != null) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);

            }
            mOnClickListener.onClick(view, mViewPos.get(view));

        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setOnCurrentImageChangeListener(CurrentImageChangeListener mListener) {
        this.mListener = mListener;
    }

}
