/* 
 * @(#)FrameAnimationView.java    Created on 2013-7-17
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.animation.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;

/**
 * 播放帧动画的一个UI控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-17 上午9:04:35 $
 */
public class FrameImageView extends ImageView {
    private int[] framePicResids;// 要播放的帧的图片
    private boolean isOneShot = true;// 是否只播放一遍，默认是
    private int interval = 500;// 每帧动画播放的间隔，单位毫秒级

    private AnimationDrawable frameAnimation = null;// 用来播放动画的对象
    private Drawable bitAnimation = null;// 其中每一帧的资源

    private boolean isFirst = true;

    public FrameImageView(Context context) {
        super(context);
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 传入图片资源，并初始化
     * 
     * @param framePicResids
     */
    public void init(int[] framePicResids) {
        this.framePicResids = framePicResids;

        frameAnimation = new AnimationDrawable();
        for (int i = 0, n = framePicResids.length; i < n; i++) {
            bitAnimation = getResources().getDrawable(framePicResids[i]);
            frameAnimation.addFrame(bitAnimation, interval);
        }
        frameAnimation.setOneShot(isOneShot);
        this.setBackgroundDrawable(frameAnimation);
    }

    /**
     * 启动动画，注意AnimationDrawable.start()的方法不能在OnCreate方法里面调的
     */
    public void start() {
        isFirst = true;
        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isFirst) {
                    frameAnimation.start();
                    isFirst = false;
                }
                return true;
            }
        });
    }

    /**
     * 停止
     */
    public void stop() {
        frameAnimation.stop();
    }

    /**
     * 是否正在跑动
     * 
     * @return
     */
    public boolean isRunning() {
        return frameAnimation.isRunning();
    }

    public int[] getFramePicResids() {
        return framePicResids;
    }

    public void setFramePicResids(int[] framePicResids) {
        this.framePicResids = framePicResids;
    }

    public boolean isOneShot() {
        return isOneShot;
    }

    public void setOneShot(boolean isOneShot) {
        this.isOneShot = isOneShot;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public AnimationDrawable getFrameAnimation() {
        return frameAnimation;
    }

    public void setFrameAnimation(AnimationDrawable frameAnimation) {
        this.frameAnimation = frameAnimation;
    }

}
