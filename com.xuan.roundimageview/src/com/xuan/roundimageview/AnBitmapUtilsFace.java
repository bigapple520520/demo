/* 
 * @(#)AnBitmapUtilsFactory.java    Created on 2013-9-17
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.roundimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.winupon.andframe.bigapple.bitmap.AnBitmapUtils;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.callback.ImageLoadCallBack;

/**
 * AnBitmapUtils接口门面，使其保持单例
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-17 下午4:59:56 $
 */
public abstract class AnBitmapUtilsFace {
    public static AnBitmapUtils instance;

    public static AnBitmapUtils instance(final Context context) {
        if (null == instance) {
            instance = new AnBitmapUtils(context);
            instance.configDefaultCacheExpiry(3 * 24 * 60 * 60 * 1000);// 缓存3天有效

            // 圆角图片处理在这里
            instance.configDefaultImageLoadCallBack(new ImageLoadCallBack() {
                @Override
                public void loadCompleted(ImageView imageView, Bitmap bitmap, BitmapDisplayConfig arg2) {
                    Object object = imageView.getTag();
                    if (null != object) {
                        int roundPx = (Integer) object;
                        imageView.setImageBitmap(RoundImageView.getRoundedCornerBitmap(bitmap, roundPx));
                    }
                    else {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void loadFailed(ImageView imageView, Bitmap bitmap) {
                    Object object = imageView.getTag();
                    if (null != object) {
                        int roundPx = (Integer) object;
                        imageView.setImageBitmap(RoundImageView.getRoundedCornerBitmap(bitmap, roundPx));
                    }
                    else {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
        }

        return instance;
    }

    /**
     * 显示
     * 
     * @param context
     * @param imageView
     * @param uri
     */
    public static void display(Context context, ImageView imageView, String uri) {
        instance(context).display(imageView, uri);
    }

}
