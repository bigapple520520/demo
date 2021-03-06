package com.xuan.demo.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.xuan.demo.launcher.event.OnScrollCompleteListener;
import com.xuan.demo.launcher.event.ScrollEvent;
import com.xuan.demo.launcher.event.ScrollEventAdapter;

/**
 * 模仿launcher左右滑动的布局控件哦，亲，很好用的，有点想viewPager这个玩样
 * 
 * @author xuan
 */
public class LauncherLayout extends ViewGroup {
	private final ScrollEventAdapter scrollEventAdapter;// 存放一个事件列表

	private final Scroller scroller;// 使切view的时候效果比较平滑
	private VelocityTracker velocityTracker;// 计算手势的一些速率等的工具类

	private int curScreen;// 记录当前屏幕的位置，从0开始
	private final int defaultScreen = 0;// 默认从0开始

	private static final int TOUCH_STATE_REST = 0;// 手势空闲状态
	private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态
	private int touchState = TOUCH_STATE_REST;// 当前滚动触摸状态

	private static final int SNAP_VELOCITY = 600;// 速率

	private final int touchSlop;// 触发后小于改距离的，不移动
	private float lastMotionX;// 记录最后一次x坐标值

	public LauncherLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LauncherLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		scrollEventAdapter = new ScrollEventAdapter();
		scroller = new Scroller(context);
		curScreen = defaultScreen;
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			// 控件的宽模式必须是设置成500dp或者FILL_PARENT这样大小已经决定的参数
			throw new IllegalStateException(
					"ScrollLayout width only can run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			// 控件的高模式必须是设置成500dp或者FILL_PARENT这样大小已经决定的参数
			// 想不通，我把高宽都设置成500dp这样，这里的高就不算精确了，求高人指点？？？
			throw new IllegalStateException(
					"ScrollLayout heigh only can run at EXACTLY mode!");
		}

		// 给他的子View设置同样的高和宽
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		scrollTo(curScreen * width, 0);
	}

	/**
	 * 更具当前停留的位置，决定跳去那边
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	/**
	 * 平滑的切换到指定屏幕
	 * 
	 * @param whichScreen
	 */
	public void snapToScreen(int whichScreen) {
		// 保证号在[0,getChildCount() - 1]范围内
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		// 如果没有滚动到位，就滚动余下的距离
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();

			// 从(getScrollX(),0)-->x滚动delta，y滚动0，到达指定位置的时间（以毫秒为单位）
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);

			if (curScreen != whichScreen) {
				ScrollEvent scrollEvent = new ScrollEvent(whichScreen);
				scrollEventAdapter.notifyEvent(scrollEvent);
			}
			curScreen = whichScreen;

			// 重绘布局
			invalidate();
		}
	}

	/**
	 * 直接切换到指定屏幕
	 * 
	 * @param whichScreen
	 */
	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		curScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
		ScrollEvent scrollEvent = new ScrollEvent(whichScreen);
		scrollEventAdapter.notifyEvent(scrollEvent);
		invalidate();
	}

	public int getCurScreen() {
		return curScreen;
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (lastMotionX - x);
			lastMotionX = x;
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker vt = velocityTracker;
			vt.computeCurrentVelocity(1000);
			int velocityX = (int) vt.getXVelocity();

			if (velocityX > SNAP_VELOCITY && curScreen > 0) {
				// 手势结束转到靠左的界面
				snapToScreen(curScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& curScreen < getChildCount() - 1) {
				// 手势结束转到靠右的界面
				snapToScreen(curScreen + 1);
			} else {
				snapToDestination();
			}

			if (velocityTracker != null) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
			touchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			touchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		// 如果事件是正在移动，且触发状态不在空闲状态，就拦截事件，不让事件往下传递
		if ((action == MotionEvent.ACTION_MOVE)
				&& (touchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(lastMotionX - x);
			if (xDiff > touchSlop) {
				touchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			this.postInvalidate();
			lastMotionX = x;
			touchState = scroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			this.postInvalidate();
			touchState = TOUCH_STATE_REST;
			break;
		}

		return touchState != TOUCH_STATE_REST;
	}

	/**
	 * 设置滚动后屏幕变化的监听事件
	 * 
	 * @param listener
	 */
	public void setOnScrollCompleteLinstenner(OnScrollCompleteListener listener) {
		scrollEventAdapter.addListener(listener);
	}

}
