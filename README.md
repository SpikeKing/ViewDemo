# 详解View的基础概念

在Android中, View作为最重要的概念, 参数较多. 显示控件都继承于View, 包含ViewGroup也是继承于View. 在View中, 核心概念包含Position(位置), MotionEvent(运动事件), TouchSlop(触摸间隔), VelocityTracker(速度追踪), GestureDetector(手势检测). 本文主要介绍以下概念.

本文源码的GitHub[下载地址](https://github.com/SpikeKing/ViewDemo)

---

## Position

位置信息, 常见的属性是屏幕的宽高, 像素和DP. 像素是真实的屏幕大小, 如1080x1794; DP是Android的专属概念, 用于统一不同屏幕尺寸, 如360x598.

获取方式

``` java
DisplayMetrics metrics = getResources().getDisplayMetrics();
float density = getResources().getDisplayMetrics().density;

mContent += "屏幕宽度:" + metrics.widthPixels + "(像素)"
        + (metrics.widthPixels / density) + "(DP);\n";
mContent += "屏幕高度:" + metrics.heightPixels + "(像素)"
        + (metrics.heightPixels / density) + "(DP);";

mTvText.setText(mContent);
```

像素使用``metrics.widthPixels``和``metrics.heightPixels``; DP使用``像素除以密度(density)``, density是dpi的衡量标准.

除了像素和DP, View的位置参数是相对于父控件的位置, 都是相对坐标. 获取View的四个角的位置, 即left, right, top, bottom.

``` java
content += "Left: " + mTvViewGroup.getLeft() + ", Right: " + mTvViewGroup.getRight()
        + "\nTop: " + mTvViewGroup.getTop() + ", Bottom: " + mTvViewGroup.getBottom()
```

对于移动控件而言, 还有一些额外的参数, X, Y, TransitionX, TransitionY. X表示Left的位置, Y表示Top的位置, TransitionX的X轴偏移量, TransitionY的Y轴偏移量. 这四个参数主要是用于移动控件, 移动控件仅仅是移动相对位置, 并不改变已有的占位.

``` java
content += "X: " + mMtvViewText.getX() + ", Y: " + mMtvViewText.getY()
        + "\nTranslationX: " + mMtvViewText.getTranslationX() + ", TranslationY: " + mMtvViewText.getTranslationY();
```

关系: Left = X - TransitionX; Top = Y - TransitionY;

## MotionEvent

运动事件, 主要事件包含三种: ACTION_DOWN(接触屏幕), ACTION_MOVE(移动), ACTION_UP(移开屏幕). 触摸行为一般都可以表示为这些事件的组合.

获取点击位置的方式``getX``, ``getY``, ``getRawX``, ``getRawY``, 分别表示相对位置和绝对位置.

## TouchSlop

在触摸屏幕的过程中, 一般都会发生细微的移动, 因此判断真正的移动需要一个阈值, 这就是``TouchSlop``. 大于阈值, 判断为滑动; 小于阈值, 判断为点击.

``` java
mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); // 用户滑动的最小像素
```

## VelocityTracker

速度追踪器(VelocityTracker), 用于追踪在滑动过程中的移动速度. 需要注意三点: 第一点是在获取值前, 需要调用计算方法; 第二点是计算的速度是滑动的位移, 可能是正或负; 第三点是在计算完成后, 需要回收速度追踪器.

``` java
@Override protected void onCreate(Bundle savedInstanceState) {
	mVelocityTracker = VelocityTracker.obtain(); // 初始化滑动速度追踪器
}

@Override public boolean onTouchEvent(MotionEvent event) {
    mVelocityTracker.addMovement(event);
    switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            mVelocityTracker.computeCurrentVelocity(1000); // 计算1秒的速度
            mXVelocity = (int) mVelocityTracker.getXVelocity(); // 水平位移
            mYVelocity = (int) mVelocityTracker.getYVelocity(); // 竖直位移
            mVelocityTracker.clear(); // 回收加速度追踪器
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
```

## GestureDetector

手势检测, 是运动检测的高级形式, 自定义多种形式, 在回调中直接使用, 非常简单. 常见的9种手势, ``OnGestureListener``包含6种, 即``onDown(手指轻触屏幕)``, ``onShowPress(指轻触屏幕, 尚未松开)``, ``onSingleTapUp(单击屏幕)``, ``onScroll(手指拖动)``, ``onLongPress(长按)``, ``onFling(轻滑)``; ``OnDoubleTapListener``双击包含3种, 即``onSingleTapConfirmed(严格的单击行为)``, ``onDoubleTap(双击)``, ``onDoubleTapEvent(发生双击行为)``;

``` java
@Override protected void onCreate(Bundle savedInstanceState) {
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
```

使用方式

``` java
@Override public boolean onTouchEvent(MotionEvent event) {
    mGestureDetector.onTouchEvent(event);
    return super.onTouchEvent(event);
}
```

可能在动作中调用多个回调.

---

在Android中, 熟悉View的基础概念, 有助于我们开发自定义控件, 也可以解决View的冲突等问题, 掌握View还是很有必要的.
