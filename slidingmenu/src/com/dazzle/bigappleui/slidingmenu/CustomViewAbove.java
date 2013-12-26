package com.dazzle.bigappleui.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnClosedListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenedListener;

/**
 * 主界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-13 下午7:14:53 $
 */
public class CustomViewAbove extends ViewGroup {
    private static final String TAG = "CustomViewAbove";

    private static final boolean DEBUG = false;

    private static final int MAX_SETTLE_DURATION = 600; // ms
    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
    private int mFlingDistance;// MIN_DISTANCE_FOR_FLING的px值

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    // 主界面view
    private View mContent;

    // 当前item：0左侧滑界面、1主界面、2右侧滑界面
    private int mCurItem;

    private Scroller mScroller;

    private boolean mScrolling;

    private boolean mIsBeingDragged;// 是否正在被拖动
    private boolean mIsUnableToDrag;// 是否不能被拖动
    private int mTouchSlop;
    private float mInitialMotionX;

    private boolean mQuickReturn = false;// 是否快速的返回定位

    private float mLastMotionX;
    private float mLastMotionY;

    // 记录被激活的点的索引，可能有多点被使用
    private static final int INVALID_POINTER = -1;
    protected int mActivePointerId = INVALID_POINTER;

    protected VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    protected int mMaximumVelocity;

    private CustomViewBehind mViewBehind;

    // 是否能侧滑
    private boolean mEnabled = true;

    // item有切换变化监听
    private OnPageChangeListener mOnPageChangeListener;
    private OnPageChangeListener mInternalPageChangeListener;

    // 侧滑菜单是否被打开监听
    private OnClosedListener mClosedListener;
    private OnOpenedListener mOpenedListener;

    // 侧滑操作时忽略侧滑的view
    private final List<View> mIgnoredViews = new ArrayList<View>();

    protected int mTouchMode = SlidingMenu.TOUCHMODE_MARGIN;

    // ////////////////////////////////////////CustomViewAbove构造初始化///////////////////////////////////////////////
    public CustomViewAbove(Context context) {
        this(context, null);
    }

    public CustomViewAbove(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomViewAbove();
    }

    void initCustomViewAbove() {
        // android 中重写surfaceview 调用invalidate会发现程序不会自动执行ondraw函数，<br>
        // 只要在函数中设置setWillNotDraw(false)，再调用invalidate程序就会自动执行ondraw函数
        setWillNotDraw(false);

        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);

        final Context context = getContext();
        mScroller = new Scroller(context, sInterpolator);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        setInternalPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mViewBehind != null) {
                    switch (position) {
                    case 0:
                    case 2:
                        mViewBehind.setChildrenEnabled(true);
                        break;
                    case 1:
                        mViewBehind.setChildrenEnabled(false);
                        break;
                    }
                }
            }
        });

        final float density = context.getResources().getDisplayMetrics().density;
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
    }

    // /////////////////////////////////////各个子界面之间的切换/////////////////////////////////////////////////////
    public void setCurrentItem(int item) {
        setCurrentItemInternal(item, true, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (!always && mCurItem == item) {
            return;
        }

        item = mViewBehind.getMenuPage(item);

        final boolean dispatchSelected = mCurItem != item;
        mCurItem = item;
        final int destX = getDestScrollX(mCurItem);

        if (dispatchSelected && mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(item);
        }

        if (dispatchSelected && mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageSelected(item);
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
        }
        else {
            completeScroll();
            scrollTo(destX, 0);
        }
    }

    // //////////////////////////////////////////////设置界面切换监听器/////////////////////////////////////////////////
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnOpenedListener(OnOpenedListener onOpenedListener) {
        mOpenedListener = onOpenedListener;
    }

    public void setOnClosedListener(OnClosedListener onClosedListener) {
        mClosedListener = onClosedListener;
    }

    private void setInternalPageChangeListener(OnPageChangeListener listener) {
        this.mInternalPageChangeListener = listener;
    }

    // ////////////////////////////////子界面黑名单（被设置的子界面，在他上滑动不会发生侧滑效果）/////////////////////////
    public void addIgnoredView(View v) {
        if (!mIgnoredViews.contains(v)) {
            mIgnoredViews.add(v);
        }
    }

    public void removeIgnoredView(View v) {
        mIgnoredViews.remove(v);
    }

    public void clearIgnoredViews() {
        mIgnoredViews.clear();
    }

    // We want the duration of the page snap animation to be influenced by the distance that
    // the screen has to travel, however, we don't want this duration to be effected in a
    // purely linear fashion. Instead, we use this method to moderate the effect that the distance
    // of travel has on the overall snap duration.
    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return FloatMath.sin(f);
    }

    public int getDestScrollX(int page) {
        switch (page) {
        case 0:
        case 2:
            return mViewBehind.getMenuLeft(mContent, page);
        case 1:
            return mContent.getLeft();
        }
        return 0;
    }

    private int getLeftBound() {
        return mViewBehind.getAbsLeftBound(mContent);
    }

    private int getRightBound() {
        return mViewBehind.getAbsRightBound(mContent);
    }

    public int getContentLeft() {
        return mContent.getLeft() + mContent.getPaddingLeft();
    }

    public boolean isMenuOpen() {
        return mCurItem == 0 || mCurItem == 2;
    }

    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : mIgnoredViews) {
            v.getHitRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    public int getBehindWidth() {
        if (null == mViewBehind) {
            return 0;
        }
        else {
            return mViewBehind.getBehindWidth();
        }
    }

    public int getChildWidth(int i) {
        switch (i) {
        case 0:
            return getBehindWidth();
        case 1:
            return mContent.getWidth();
        default:
            return 0;
        }
    }

    public boolean isSlidingEnabled() {
        return mEnabled;
    }

    public void setSlidingEnabled(boolean b) {
        mEnabled = b;
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (getChildCount() == 0) {
            // Nothing to do.
            return;
        }

        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll();
            if (isMenuOpen()) {
                if (mOpenedListener != null) {
                    mOpenedListener.onOpened();
                }
            }
            else {
                if (mClosedListener != null) {
                    mClosedListener.onClosed();
                }
            }
            return;
        }

        mScrolling = true;

        final int width = getBehindWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
        final float distance = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatio);

        int duration = 0;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        }
        else {
            final float pageDelta = (float) Math.abs(dx) / width;
            duration = (int) ((pageDelta + 1) * 100);
            duration = MAX_SETTLE_DURATION;
        }
        duration = Math.min(duration, MAX_SETTLE_DURATION);

        mScroller.startScroll(sx, sy, dx, dy, duration);
        invalidate();
    }

    public void setContent(View v) {
        if (mContent != null) {
            this.removeView(mContent);
        }
        mContent = v;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    public void setCustomViewBehind(CustomViewBehind cvb) {
        mViewBehind = cvb;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContent.measure(contentWidth, contentHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Make sure scroll position is set correctly.
        if (w != oldw) {
            // [ChrisJ] - This fixes the onConfiguration change for orientation issue..
            // maybe worth having a look why the recomputeScroll pos is screwing
            // up?
            completeScroll();
            scrollTo(getDestScrollX(mCurItem), getScrollY());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mContent.layout(0, 0, width, height);
    }

    public void setAboveOffset(int i) {
        mContent.setPadding(i, mContent.getPaddingTop(), mContent.getPaddingRight(), mContent.getPaddingBottom());
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();

                if (oldX != x || oldY != y) {
                    scrollTo(x, y);
                    pageScrolled(x);
                }

                // Keep on drawing until the animation has finished.
                invalidate();
                return;
            }
        }

        // Done with scroll, clean up state.
        completeScroll();
    }

    private void pageScrolled(int xpos) {
        final int widthWithMargin = getWidth();
        final int position = xpos / widthWithMargin;
        final int offsetPixels = xpos % widthWithMargin;
        final float offset = (float) offsetPixels / widthWithMargin;

        onPageScrolled(position, offset, offsetPixels);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part of a programmatically initiated
     * smooth scroll or a user initiated touch scroll. If you override this method you must call through to the
     * superclass implementation (e.g. super.onPageScrolled(position, offset, offsetPixels)) before onPageScrolled
     * returns.
     * 
     * @param position
     *            Position index of the first page currently being displayed. Page position+1 will be visible if
     *            positionOffset is nonzero.
     * @param offset
     *            Value from [0, 1) indicating the offset from the page at position.
     * @param offsetPixels
     *            Value in pixels indicating the offset from position.
     */
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (mInternalPageChangeListener != null) {
            mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    // 结束滚动，如果正在滚动，取消之，回到原来的位置
    private void completeScroll() {
        boolean needPopulate = mScrolling;
        if (needPopulate) {
            // Done with scroll, no longer want to cache view drawing.
            mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }

            if (isMenuOpen()) {
                if (mOpenedListener != null) {
                    mOpenedListener.onOpened();
                }
            }
            else {
                if (mClosedListener != null) {
                    mClosedListener.onClosed();
                }
            }
        }
        mScrolling = false;
    }

    public void setTouchMode(int i) {
        mTouchMode = i;
    }

    public int getTouchMode() {
        return mTouchMode;
    }

    // 判断这个事件是否需要拖动
    private boolean thisTouchAllowed(MotionEvent ev) {
        int x = (int) (ev.getX() + mScrollX);
        if (isMenuOpen()) {
            return mViewBehind.menuOpenTouchAllowed(mContent, mCurItem, x);
        }
        else {
            switch (mTouchMode) {
            case SlidingMenu.TOUCHMODE_FULLSCREEN:
                return !isInIgnoredView(ev);
            case SlidingMenu.TOUCHMODE_NONE:
                return false;
            case SlidingMenu.TOUCHMODE_MARGIN:
                return mViewBehind.marginTouchAllowed(mContent, x);
            }
        }
        return false;
    }

    private boolean thisSlideAllowed(float dx) {
        boolean allowed = false;
        if (isMenuOpen()) {
            allowed = mViewBehind.menuOpenSlideAllowed(dx);
        }
        else {
            allowed = mViewBehind.menuClosedSlideAllowed(dx);
        }
        if (DEBUG) {
            Log.v(TAG, "this slide allowed " + allowed + " dx: " + dx);
        }
        return allowed;
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1) {
            mActivePointerId = INVALID_POINTER;
        }
        return activePointerIndex;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnabled) {
            return false;
        }

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        if (DEBUG) {
            if (action == MotionEvent.ACTION_DOWN) {
                Log.v(TAG, "Received ACTION_DOWN");
            }
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP
                || (action != MotionEvent.ACTION_DOWN && mIsUnableToDrag)) {
            endDrag();
            return false;
        }

        switch (action) {
        case MotionEvent.ACTION_MOVE:
            determineDrag(ev);
            break;
        case MotionEvent.ACTION_DOWN:
            int index = MotionEventCompat.getActionIndex(ev);
            mActivePointerId = MotionEventCompat.getPointerId(ev, index);
            if (mActivePointerId == INVALID_POINTER) {
                break;
            }
            mLastMotionX = mInitialMotionX = MotionEventCompat.getX(ev, index);
            mLastMotionY = MotionEventCompat.getY(ev, index);
            if (thisTouchAllowed(ev)) {
                mIsBeingDragged = false;
                mIsUnableToDrag = false;
                if (isMenuOpen() && mViewBehind.menuTouchInQuickReturn(mContent, mCurItem, ev.getX() + mScrollX)) {
                    mQuickReturn = true;
                }
            }
            else {
                mIsUnableToDrag = true;
            }
            break;
        case MotionEventCompat.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            break;
        }

        if (!mIsBeingDragged) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);
        }
        return mIsBeingDragged || mQuickReturn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mEnabled) {
            return false;
        }

        if (!mIsBeingDragged && !thisTouchAllowed(ev)) {
            return false;
        }

        final int action = ev.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action & MotionEventCompat.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            /*
             * If being flinged and user touches, stop the fling. isFinished will be false if being flinged.
             */
            completeScroll();

            // Remember where the motion event started
            int index = MotionEventCompat.getActionIndex(ev);
            mActivePointerId = MotionEventCompat.getPointerId(ev, index);
            mLastMotionX = mInitialMotionX = ev.getX();
            break;
        case MotionEvent.ACTION_MOVE:
            if (!mIsBeingDragged) {
                determineDrag(ev);
                if (mIsUnableToDrag) {
                    return false;
                }
            }
            if (mIsBeingDragged) {
                // Scroll to follow the motion event
                final int activePointerIndex = getPointerIndex(ev, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final float x = MotionEventCompat.getX(ev, activePointerIndex);
                final float deltaX = mLastMotionX - x;
                mLastMotionX = x;
                float oldScrollX = getScrollX();
                float scrollX = oldScrollX + deltaX;
                final float leftBound = getLeftBound();
                final float rightBound = getRightBound();
                if (scrollX < leftBound) {
                    scrollX = leftBound;
                }
                else if (scrollX > rightBound) {
                    scrollX = rightBound;
                }
                // Don't lose the rounded component
                mLastMotionX += scrollX - (int) scrollX;
                scrollTo((int) scrollX, getScrollY());
                pageScrolled((int) scrollX);
            }
            break;
        case MotionEvent.ACTION_UP:
            if (mIsBeingDragged) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, mActivePointerId);
                final int scrollX = getScrollX();
                // final int widthWithMargin = getWidth();
                // final float pageOffset = (float) (scrollX % widthWithMargin) / widthWithMargin;
                // TODO test this. should get better flinging behavior
                final float pageOffset = (float) (scrollX - getDestScrollX(mCurItem)) / getBehindWidth();
                final int activePointerIndex = getPointerIndex(ev, mActivePointerId);
                if (mActivePointerId != INVALID_POINTER) {
                    final float x = MotionEventCompat.getX(ev, activePointerIndex);
                    final int totalDelta = (int) (x - mInitialMotionX);
                    int nextPage = determineTargetPage(pageOffset, initialVelocity, totalDelta);
                    setCurrentItemInternal(nextPage, true, true, initialVelocity);
                }
                else {
                    setCurrentItemInternal(mCurItem, true, true, initialVelocity);
                }
                mActivePointerId = INVALID_POINTER;
                endDrag();
            }
            else if (mQuickReturn && mViewBehind.menuTouchInQuickReturn(mContent, mCurItem, ev.getX() + mScrollX)) {
                setCurrentItem(1);
                endDrag();
            }
            break;
        case MotionEvent.ACTION_CANCEL:
            if (mIsBeingDragged) {
                setCurrentItemInternal(mCurItem, true, true);
                mActivePointerId = INVALID_POINTER;
                endDrag();
            }
            break;
        case MotionEventCompat.ACTION_POINTER_DOWN: {
            final int indexx = MotionEventCompat.getActionIndex(ev);
            mLastMotionX = MotionEventCompat.getX(ev, indexx);
            mActivePointerId = MotionEventCompat.getPointerId(ev, indexx);
            break;
        }
        case MotionEventCompat.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            int pointerIndex = getPointerIndex(ev, mActivePointerId);
            if (mActivePointerId == INVALID_POINTER) {
                break;
            }
            mLastMotionX = MotionEventCompat.getX(ev, pointerIndex);
            break;
        }
        return true;
    }

    private void determineDrag(MotionEvent ev) {
        final int activePointerId = mActivePointerId;
        final int pointerIndex = getPointerIndex(ev, activePointerId);
        if (activePointerId == INVALID_POINTER) {
            return;
        }
        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float dx = x - mLastMotionX;
        final float xDiff = Math.abs(dx);
        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float dy = y - mLastMotionY;
        final float yDiff = Math.abs(dy);
        if (xDiff > (isMenuOpen() ? mTouchSlop / 2 : mTouchSlop) && xDiff > yDiff && thisSlideAllowed(dx)) {
            startDrag();
            mLastMotionX = x;
            mLastMotionY = y;
            // TODO add back in touch slop check
        }
        else if (xDiff > mTouchSlop) {
            mIsUnableToDrag = true;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        mScrollX = x;
        mViewBehind.scrollBehindTo(mContent, x, y);
        ((SlidingMenu) getParent()).manageLayers(getPercentOpen());
    }

    private int determineTargetPage(float pageOffset, int velocity, int deltaX) {
        int targetPage = mCurItem;
        if (Math.abs(deltaX) > mFlingDistance && Math.abs(velocity) > mMinimumVelocity) {
            if (velocity > 0 && deltaX > 0) {
                targetPage -= 1;
            }
            else if (velocity < 0 && deltaX < 0) {
                targetPage += 1;
            }
        }
        else {
            targetPage = Math.round(mCurItem + pageOffset);
        }
        return targetPage;
    }

    protected float getPercentOpen() {
        return Math.abs(mScrollX - mContent.getLeft()) / getBehindWidth();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // Draw the margin drawable if needed.
        mViewBehind.drawShadow(mContent, canvas);
        mViewBehind.drawFade(mContent, canvas, getPercentOpen());
        mViewBehind.drawSelector(mContent, canvas, getPercentOpen());
    }

    // variables for drawing
    private float mScrollX = 0.0f;

    private void onSecondaryPointerUp(MotionEvent ev) {
        if (DEBUG) {
            Log.v(TAG, "onSecondaryPointerUp called");
        }
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private void startDrag() {
        mIsBeingDragged = true;
        mQuickReturn = false;
    }

    private void endDrag() {
        mQuickReturn = false;
        mIsBeingDragged = false;
        mIsUnableToDrag = false;
        mActivePointerId = INVALID_POINTER;

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * Tests scrollability within child views of v given a delta of dx.
     * 
     * @param v
     *            View to test for horizontal scrollability
     * @param checkV
     *            Whether the view v passed should itself be checked for scrollability (true), or just its children
     *            (false).
     * @param dx
     *            Delta scrolled in pixels
     * @param x
     *            X coordinate of the active touch point
     * @param y
     *            Y coordinate of the active touch point
     * @return true if child views of v can be scrolled by delta of dx.
     */
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop()
                        && y + scrollY < child.getBottom()
                        && canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollHorizontally(v, -dx);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    /**
     * You can call this function yourself to have the scroll view perform scrolling from a key event, just as if the
     * event had been dispatched to it by the view hierarchy.
     * 
     * @param event
     *            The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                handled = arrowScroll(FOCUS_LEFT);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                handled = arrowScroll(FOCUS_RIGHT);
                break;
            case KeyEvent.KEYCODE_TAB:
                if (Build.VERSION.SDK_INT >= 11) {
                    // The focus finder had a bug handling FOCUS_FORWARD and FOCUS_BACKWARD
                    // before Android 3.0. Ignore the tab key on those devices.
                    if (KeyEventCompat.hasNoModifiers(event)) {
                        handled = arrowScroll(FOCUS_FORWARD);
                    }
                    else if (KeyEventCompat.hasModifiers(event, KeyEvent.META_SHIFT_ON)) {
                        handled = arrowScroll(FOCUS_BACKWARD);
                    }
                }
                break;
            }
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }

        boolean handled = false;

        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused != null && nextFocused != currentFocused) {
            if (direction == View.FOCUS_LEFT) {
                handled = nextFocused.requestFocus();
            }
            else if (direction == View.FOCUS_RIGHT) {
                // If there is nothing to the right, or this is causing us to
                // jump to the left, then what we really want to do is page right.
                if (currentFocused != null && nextFocused.getLeft() <= currentFocused.getLeft()) {
                    handled = pageRight();
                }
                else {
                    handled = nextFocused.requestFocus();
                }
            }
        }
        else if (direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) {
            // Trying to move left and nothing there; try to page.
            handled = pageLeft();
        }
        else if (direction == FOCUS_RIGHT || direction == FOCUS_FORWARD) {
            // Trying to move right and nothing there; try to page.
            handled = pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    boolean pageLeft() {
        if (mCurItem > 0) {
            setCurrentItem(mCurItem - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        if (mCurItem < 1) {
            setCurrentItem(mCurItem + 1, true);
            return true;
        }
        return false;
    }

    // //////////////////////////////////////item变化监听//////////////////////////////////////////////////////////////
    /**
     * 侧滑发生变化监听
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-11-14 下午7:45:59 $
     */
    public interface OnPageChangeListener {

        /**
         * 侧滑滚动时
         * 
         * @param position
         * @param positionOffset
         * @param positionOffsetPixels
         */
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        /**
         * 侧滑状态改变时
         * 
         * @param position
         */
        public void onPageSelected(int position);
    }

}
