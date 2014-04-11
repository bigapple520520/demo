package com.dazzle.ui.slipbutton.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dazzle.ui.slipbutton.R;

/**
 * 模仿ios的是否滑块控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-24 下午7:54:55 $
 */
public class SlipButton extends View {
    private static final int OFFSET = 5;// 这个偏移量保证，滑块按钮在滑块背景内部

    private boolean nowChoose = false;// 记录当前按钮是否打开,true为打开,flase为关闭
    private boolean onSlip = false;// 记录用户是否在滑动的变量

    private float downX;// 按下时的x
    private float nowX;// 当前的x

    private OnChangedListener onChangedListener;// 改变事件

    private Bitmap slipBg;// 游标背景
    private Bitmap slipOn;// YES游标图片
    private Bitmap slipOff;// NO游标图片
    private Bitmap slipBtn;// 绘制时接受YES还是NO

    private final Matrix matrix = new Matrix();
    private final Paint paint = new Paint();

    private int heightOffset;// 游标距背景的偏移

    public SlipButton(Context context) {
        super(context);
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 初始化
    private void init() {
        // 载入图片资源
        slipBg = BitmapFactory.decodeResource(getResources(), R.drawable.slip_bg);
        slipOn = BitmapFactory.decodeResource(getResources(), R.drawable.slip_btn_on);
        slipOff = BitmapFactory.decodeResource(getResources(), R.drawable.slip_btn_off);

        heightOffset = (slipBg.getHeight() - slipOn.getHeight()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x;

        // 画滑块背景
        canvas.drawBitmap(slipBg, matrix, paint);

        // 是否是在滑动状态
        if (onSlip) {
            if (nowX >= slipBg.getWidth()) {
                x = slipBg.getWidth() - slipOn.getWidth();
                slipBtn = slipOff;
            }
            else {
                x = nowX - slipOn.getWidth() / 2;
                slipBtn = slipOn;
            }
        }
        else {// 非滑动状态
            if (nowChoose) {
                x = OFFSET;
                slipBtn = slipOn;
            }
            else {
                x = slipBg.getWidth() - slipOn.getWidth();
                slipBtn = slipOff;
            }
        }

        if (x <= 0) {
            x = OFFSET;
        }
        else if (x > slipBg.getWidth() - slipOn.getWidth() - OFFSET) {
            x = slipBg.getWidth() - slipOn.getWidth() - OFFSET;
        }

        canvas.drawBitmap(slipBtn, x, heightOffset, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:// 移动
            nowX = event.getX();
            break;
        case MotionEvent.ACTION_DOWN:// 按下
            if (event.getX() > slipBg.getWidth() || event.getY() > slipBg.getHeight()) {
                return false;
            }

            onSlip = true;
            downX = event.getX();
            nowX = downX;
            break;
        case MotionEvent.ACTION_UP:// 松开
            onSlip = false;

            // 松开时判断是否选中
            boolean lastChoose = nowChoose;
            nowChoose = (event.getX() < (slipBg.getWidth() / 2));

            if (null != onChangedListener && (lastChoose != nowChoose)) {
                onChangedListener.OnChanged(nowChoose);
            }
            break;
        }
        invalidate();
        return true;
    }

    /**
     * 设置事件改变监听
     * 
     * @param onChangedListener
     */
    public void SetOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }

    /**
     * 判断当前开或者关状态
     * 
     * @return
     */
    public boolean isChecked() {
        return nowChoose;
    }

    /**
     * 外部手动设置开或者关
     * 
     * @param check
     */
    public void setChecked(boolean check) {
        if (check != nowChoose) {
            nowChoose = check;
            if (nowChoose) {
                nowX = OFFSET;
            }
            else {
                nowX = slipBg.getWidth() - slipOn.getWidth();
            }
            invalidate();// 重画控件

            if (null != onChangedListener) {
                onChangedListener.OnChanged(nowChoose);
            }
        }
    }

}
