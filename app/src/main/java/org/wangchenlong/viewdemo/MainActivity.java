package org.wangchenlong.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG-WCL: " + MainActivity.class.getSimpleName();

    @BindView(R.id.main_rl_container) RelativeLayout mRlContainer;
    @BindView(R.id.main_mtv_view_text) TextView mMtvViewText;

    @BindView(R.id.main_tv_text) TextView mTvText;
    @BindView(R.id.main_tv_view_group) RelativeLayout mTvViewGroup;
    @BindView(R.id.main_tv_view_group_text) TextView mTvViewGroupText;

    private String mContent; // 内容;

    private VelocityTracker mVelocityTracker; // 最终滑动速度
    private int mXVelocity; // X轴的移动速度;
    private int mYVelocity; // Y轴的移动速度;

    private GestureDetector mGestureDetector; // 手势检测器

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mVelocityTracker = VelocityTracker.obtain(); // 初始化滑动速度追踪器
        showTotal();

        mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {
            @Override public boolean onDown(MotionEvent e) {
                Log.e(TAG, "手指轻触屏幕");
                return false;
            }

            @Override public void onShowPress(MotionEvent e) {
                Log.e(TAG, "手指轻触屏幕, 尚未松开");
            }

            @Override public boolean onSingleTapUp(MotionEvent e) {
                Log.e(TAG, "单击屏幕");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e(TAG, "手指拖动");
                return false;
            }

            @Override public void onLongPress(MotionEvent e) {
                Log.e(TAG, "长按");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e(TAG, "轻滑");
                return false;
            }
        });

        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e(TAG, "严格的单击行为");
                return false;
            }

            @Override public boolean onDoubleTap(MotionEvent e) {
                Log.e(TAG, "双击");
                return false;
            }

            @Override public boolean onDoubleTapEvent(MotionEvent e) {
                Log.e(TAG, "发生双击行为");
                return false;
            }
        });
    }

    /**
     * get需要完全显示, onWindowFocusChanged是最好的Activity显示指示器
     * 参考: http://stackoverflow.com/questions/12052570/getright-getleft-gettop-returning-zero
     *
     * @param hasFocus 焦点
     */
    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        showViewGroup();
        showInsideView();
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                mXVelocity = (int) mVelocityTracker.getXVelocity();
                mYVelocity = (int) mVelocityTracker.getYVelocity();
                showTotal();
                mVelocityTracker.clear();

                break;
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override protected void onDestroy() {
        mVelocityTracker.recycle(); // 回收运动速度跟踪器
        super.onDestroy();
    }

    /**
     * 显示整体的位置
     */
    private void showTotal() {
        mContent = "";

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = getResources().getDisplayMetrics().density;

        mContent += "屏幕宽度:" + metrics.widthPixels + "(像素)"
                + (metrics.widthPixels / density) + "(DP);\n";
        mContent += "屏幕高度:" + metrics.heightPixels + "(像素)"
                + (metrics.heightPixels / density) + "(DP);";
        mContent += "\nXVelocity: " + mXVelocity + ", YVelocity: " + mYVelocity;

        mTvText.setText(mContent);
    }


    /**
     * 显示ViewGroup的位置
     */
    private void showViewGroup() {
        String content = "ViewGroup\n";
        content += "Left: " + mTvViewGroup.getLeft() + ", Right: " + mTvViewGroup.getRight()
                + "\nTop: " + mTvViewGroup.getTop() + ", Bottom: " + mTvViewGroup.getBottom()
                + "\nX: " + mTvViewGroup.getX() + ", Y: " + mTvViewGroup.getY()
                + "\nTranslationX: " + mTvViewGroup.getTranslationX() + ", TranslationY: " + mTvViewGroup.getTranslationY();

        mTvViewGroupText.setText(content);
    }

    /**
     * 显示View的位置
     */
    private void showInsideView() {
        mMtvViewText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                return true;
            }
        });
        String content = "View\n";
        content += "Left: " + mMtvViewText.getLeft() + ", Right: " + mMtvViewText.getRight()
                + "\nTop: " + mMtvViewText.getTop() + ", Bottom: " + mMtvViewText.getBottom()
                + "\nX: " + mMtvViewText.getX() + ", Y: " + mMtvViewText.getY()
                + "\nTranslationX: " + mMtvViewText.getTranslationX() + ", TranslationY: " + mMtvViewText.getTranslationY();

        mMtvViewText.setText(content);
    }
}
