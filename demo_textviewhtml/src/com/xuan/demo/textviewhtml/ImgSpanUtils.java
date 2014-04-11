/* 
 * @(#)ImageSpanUtils.java    Created on 2013-4-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.textviewhtml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * 可以处理图标和文字添加到EditText中
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-4-16 下午7:50:24 $
 */
public abstract class ImgSpanUtils {

    /**
     * 图片代替文字
     * 
     * @param context
     * @param text
     * @param resId
     * @return
     */
    public static SpannableString getSpannableStrByTextReplaceImg(Context context, String text, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        BitmapDrawable bd = (BitmapDrawable) drawable;

        return getSpannableStrByTextReplaceImg(context, text, bd.getBitmap());
    }

    /**
     * 图片代替文字
     * 
     * @param context
     * @param text
     * @param bitmap
     * @return
     */
    public static SpannableString getSpannableStrByTextReplaceImg(Context context, String text, Bitmap bitmap) {
        ImageSpan imageSpan = new ImageSpan(context, bitmap);

        SpannableString spannableStr = new SpannableString(text);
        spannableStr.setSpan(imageSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStr;
    }

}
