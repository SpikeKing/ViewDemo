package org.wangchenlong.viewdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 可移动的TextView
 * <p/>
 * Created by wangchenlong on 16/6/23.
 */
public class MoveTextView extends TextView {
    private int mScaledTouchSlop; // 最小滑动距离
    private int mLastX;
    private int mLastY;

    private Scroller mScroller = new Scroller(getContext());

    public MoveTextView(Context context) {
        super(context);
        init(context);
    }

    public MoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    @SuppressWarnings("all")
    public MoveTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); // 用户滑动的最小像素
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                int translationX = (int) getTranslationX() + deltaX;
                int translationY = (int) getTranslationY() + deltaY;
                setTranslationX((float) translationX);
                setTranslationY((float) translationY);
                showText();
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;

        // 返回值表示是否消费当前的点击事件
        return true;
    }

    // 显示信息
    private void showText() {
        String content = "View\n";
        content += "ScaledTouchSlop: " + mScaledTouchSlop + "\n";
        content += "Left: " + getLeft() + ", Right: " + getRight()
                + "\nTop: " + getTop() + ", Bottom: " + getBottom()
                + "\nX: " + getX() + ", Y: " + getY()
                + "\nTranslationX: " + getTranslationX() + "\nTranslationY: " + getTranslationY();

        setText(content);
    }
}
