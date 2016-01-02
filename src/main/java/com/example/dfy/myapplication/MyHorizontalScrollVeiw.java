package com.example.dfy.myapplication;

/**
 * ������װ���HorizontalScrollView,��Ҳ���õ���OOM
 * ����ͨ��һ��HashMapά��һ��View��position�Ĺ�ϵ����ͨ�����������ж����Ӻ�ɾ����Ӧ��ͼƬ��ʹMap��ֻά��һ��������View
 * View��ͼƬ���ݺ���Ϣ��ƥ����ͨ��HorzintalScrollViewAdapter������䡣
 *
 *
 * �����������ص��ӿ�OnItemClickListener,CurrentImageChangeListener,(����ʵ����MainActivity)
 * ����Ӧ�����͵�������¼�
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
     * ͼƬ����ʱ�Ļص��ӿ�
     */
    public interface CurrentImageChangeListener {
        void onCurrentChanged(int position, View viewIndicator);
    }

    /**
     * ��Ŀ���ʱ�Ļص��ӿ�
     */

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private CurrentImageChangeListener mListener;
    private OnItemClickListener mOnClickListener;

    private static final String TAG = "MyHoriziontalScrollView";

    /**
     * HorizontalScrollView �е�LinearLayout
     */
    private LinearLayout mContainer;
    /**
     * ��Ԫ�صĿ�
     */
    private int mChildWidth;
    /**
     * ��Ԫ�صĸ߶�
     */
    private int mChildHeight;
    /**
     * ��ǰ���һ��ͼƬ���±�
     */
    private int mCurrentIndex;

    /**
     * ��ǰ��һ��ͼƬ���±�
     */
    private int mFirstIndex;
    /**
     * ��ǰ��һ��View
     */
    private View mFirstView;

    /**
     * ����������
     */
    private HorizontalScrollViewAdapter mAdapter;

    /**
     * ÿ��Ļ�����ʾ�ĸ���
     */
    private int mCountOneScreen;
    /**
     * ��Ļ�Ŀ��
     */
    private int mScreenWidth;
    /**
     * ����View��Position�ļ�ֵ��
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();


    public MyHorizontalScrollVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
        //��ȡ��Ļ���
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
     * ������һ��ͼƬ
     */
    protected void loadNextImg() {
        //�߽���ж����Ƿ������һ��ͼƬ
        if (mCurrentIndex == mAdapter.getCount()-1) {
            return;
        }
        //�Ƴ���һ��ͼƬ���ҽ�ˮƽ����λ����0
        scrollTo(0, 0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);

        //��ȡ��һ��ͼƬ�����Ҹ�ͼƬ����onClick�¼�������������
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);

        //��ǰ��һ��ͼƬ�ű��1
        mFirstIndex++;

        //��������˹��������򴥷����ص�
        if (mListener != null) {
            notifyCurrentImgChanged();
        }
    }

    protected void loadPreImg() {
        if(mFirstIndex==0)
            return;

        //���㵱ǰ��ʾ�ĵ�һ��view���±�
        int index = mCurrentIndex - mCountOneScreen;
        if (index >= 0) {
            //�Ƴ����һ��ͼƬ
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            //����View�����һ��λ��
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);

            scrollTo(mChildWidth, 0);
            // ��ǰ���һ��ͼƬ�±�--����ǰ��һ����ʾ��ͼƬ�±�--
            mCurrentIndex--;
            mFirstIndex--;
            //�ص�
            if (mListener != null) {
                notifyCurrentImgChanged();

            }

        }
    }

    /**
     * ����ʱ�Ļص�
     */
    public void notifyCurrentImgChanged() {
        //��������еı���ɫ,����ʱ������Ϊ��ɫ
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);

        }
        mListener.onCurrentChanged(mFirstIndex,mContainer.getChildAt(0));
    }

    /**
     * ��ʼ�����ݣ�
     * @param mAdapter
     */
    public void initData(HorizontalScrollViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        //��ȡ�������е�һ��View
        final View view = mAdapter.getView(0, null, mContainer);
       // mContainer.addView(view);

        //ǿ�Ƽ��㵱ǰ��View�Ŀ�͸�

        if (mChildWidth == 0 && mChildHeight == 0) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();

            Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());

            //ÿ�����ض��ٸ�View
            mCountOneScreen = mScreenWidth / mChildWidth +1;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen +
                    ", mChildWidth = " + mChildWidth);

        }

        initFirstScreenChildren(mCountOneScreen);
    }

    /**
     * ��ʼ��View��ʾ
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
                //�����ǰscrollXΪview�Ŀ�ȣ�������һ�ţ��Ƴ���һ��
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                //�����ǰscrollX=0����ǰ����һ�ţ��Ƴ����һ��
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
