/* 
 * @(#)GifView.java    Created on 2013-7-18
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.animation.view;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 可以显示gif动画的控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-18 下午5:00:11 $
 */
public class GifImageView extends ImageView {
    private final Context context;
    private Movie mMovie = null;
    private long mMovieStart = 0;

    public GifImageView(Context context) {
        super(context);
        this.context = context;
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    public void setImageResource(int resId) {
        initMovie(resId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mMovie) {
            // 图片还没set之前，就别画先
            return;
        }

        // 当前时间
        long now = android.os.SystemClock.uptimeMillis();
        // 如果第一帧，记录起始时间
        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }

        // 取出动画的时长
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = 1000;
        }

        // 算出需要显示第几帧
        int relTime = (int) ((now - mMovieStart) % dur);

        // 设置要显示的帧，绘制即可
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);
        invalidate();
    }

    // 读取gif，初始化movie
    private void initMovie(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        mMovie = Movie.decodeStream(inputStream);
    }

}
