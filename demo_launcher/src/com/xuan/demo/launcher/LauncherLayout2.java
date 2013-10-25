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
 * ģ��launcher���һ����Ĳ��ֿؼ�Ŷ���ף��ܺ��õģ��е���viewPager���������but�����ֹ��ˣ�ʱ�����һ���ʾһ���֣�
 * ��ǰ�Ƚ����е���ʾͼƬ�ķ�ʽ
 * 
 * @author xuan
 */
public class LauncherLayout2 extends ViewGroup {
	private final ScrollEventAdapter scrollEventAdapter;// ���һ���¼��б�

	private final Scroller scroller;// ʹ��view��ʱ��Ч���Ƚ�ƽ��
	private VelocityTracker velocityTracker;// �������Ƶ�һЩ���ʵȵĹ�����

	private int curScreen;// ��¼��ǰ��Ļ��λ�ã���0��ʼ
	private final int defaultScreen = 0;// Ĭ�ϴ�0��ʼ

	private static final int TOUCH_STATE_REST = 0;// ���ƿ���״̬
	private static final int TOUCH_STATE_SCROLLING = 1;// ���ڹ���״̬
	private int touchState = TOUCH_STATE_REST;// ��ǰ��������״̬

	private static final int SNAP_VELOCITY = 600;// ����

	private final int touchSlop;// ������С�ڸľ���ģ����ƶ�
	private float lastMotionX;// ��¼���һ��x����ֵ

	// ���־�TM�������ƫ����Ŷ�ף�Ĭ�ϲ�ƫ��
	private int offsetSize = 0;

	public LauncherLayout2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LauncherLayout2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		scrollEventAdapter = new ScrollEventAdapter();
		scroller = new Scroller(context);
		curScreen = defaultScreen;
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// ÿ�ε�һ��ƫ��һ��
		int childLeft = offsetSize;

		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				// ʵ�ʺ��ӵĿ�Ҫƫ��һ�£���ΪchildView.getMeasuredWidth()�õ���ʵ������Ļ�Ŀ�
				int childWidth = childView.getMeasuredWidth() - 2 * offsetSize;
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());

				// ��һ�ξ��븸����ߵľ���=��һ�ξ��븸���ұߵľ���
				childLeft = childLeft + childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			// �ؼ��Ŀ�ģʽ���������ó�500dp����FILL_PARENT������С�Ѿ������Ĳ���
			throw new IllegalStateException(
					"ScrollLayout width only can run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			// �ؼ��ĸ�ģʽ���������ó�500dp����FILL_PARENT������С�Ѿ������Ĳ���
			// �벻ͨ���ҰѸ߿����ó�500dp����������ĸ߾Ͳ��㾫ȷ�ˣ������ָ�㣿����
			throw new IllegalStateException(
					"ScrollLayout heigh only can run at EXACTLY mode!");
		}

		// ��������View����ͬ���ĸߺͿ�
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		scrollTo(curScreen * width, 0);
	}

	/**
	 * ���ߵ�ǰͣ����λ�ã�������ȥ�Ǳ�
	 */
	public void snapToDestination() {
		int getWidth = getWidth() - 2 * offsetSize;// ����ƫ��

		final int screenWidth = getWidth;
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	/**
	 * ƽ�����л���ָ����Ļ
	 * 
	 * @param whichScreen
	 */
	public void snapToScreen(int whichScreen) {
		int getWidth = getWidth() - 2 * offsetSize;// ����ƫ��

		// ��֤����[0,getChildCount() - 1]��Χ��
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		// ���û�й�����λ���͹������µľ���
		if (getScrollX() != (whichScreen * getWidth)) {
			// int delta = whichScreen * getWidth() - getScrollX();

			// �����������,������TM����������view�Ŀ�=getWidth() - 2 *
			// offsetSize,�����ͺ���ô��ԭ����view=getWidth()������
			int delta = whichScreen * getWidth - getScrollX();

			// ��(getScrollX(),0)-->x����delta��y����0������ָ��λ�õ�ʱ�䣨�Ժ���Ϊ��λ��
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);

			if (curScreen != whichScreen) {
				ScrollEvent scrollEvent = new ScrollEvent(whichScreen);
				scrollEventAdapter.notifyEvent(scrollEvent);
			}
			curScreen = whichScreen;

			// �ػ沼��
			invalidate();
		}
	}

	/**
	 * ֱ���л���ָ����Ļ
	 * 
	 * @param whichScreen
	 */
	public void setToScreen(int whichScreen) {
		int getWidth = getWidth() - 2 * offsetSize;// ����ƫ��

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		curScreen = whichScreen;
		scrollTo(whichScreen * getWidth, 0);
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
				// ���ƽ���ת������Ľ���
				snapToScreen(curScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& curScreen < getChildCount() - 1) {
				// ���ƽ���ת�����ҵĽ���
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
		// ����¼��������ƶ����Ҵ���״̬���ڿ���״̬���������¼��������¼����´���
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
	 * ���ù�������Ļ�仯�ļ����¼�
	 * 
	 * @param listener
	 */
	public void setOnScrollCompleteLinstenner(OnScrollCompleteListener listener) {
		scrollEventAdapter.addListener(listener);
	}

	public int getOffsetSize() {
		return offsetSize;
	}

	public void setOffsetSize(int offsetSize) {
		this.offsetSize = offsetSize;
	}

}
