/* 
 * @(#)ImageContextUtils.java    Created on 2013-3-13
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.cutimg;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 获取系统图片的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-13 上午9:27:14 $
 * 
 *          crop String 发送裁剪信号<br>
 *          aspectX int X方向上的比例<br>
 *          aspectY int Y方向上的比例<br>
 *          outputX int 裁剪区的宽<br>
 *          outputY int 裁剪区的高<br>
 *          scale boolean 是否保留比例<br>
 *          return-data boolean 是否将数据保留在Bitmap中返回<br>
 *          data Parcelable 相应的Bitmap数据<br>
 *          circleCrop String 圆形裁剪区域？<br>
 *          MediaStore.EXTRA_OUTPUT ("output") URI 将URI指向相应的file:///...<br>
 * 
 *          详见：http://my.oschina.net/ryanhoo/blog/86843
 */
public abstract class CutImgUtils {

    /**
     * 相册获取图片
     * 
     * @param context
     * @param requestCode
     */
    public static void getMediaStoreImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 去相机拍照返回图片，返回取到的图片路劲
     * 
     * @param activity
     * @param requestCode
     */
    public static Uri getImageFromCamera(Activity activity, int requestCode) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String tempFileName = Environment.getExternalStorageDirectory() + "/xuandemo/temp.png";

        // 判断文件夹是否存在
        File tempFile = new File(tempFileName);
        checkFileParent(tempFile);

        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * 调去裁剪图片，Uri可以是系统相册返回的地址
     * 
     * @param activity
     * @param uri
     *            截图后的图片路劲
     * @param requestCode
     * @param output
     */
    public static void getCutImage(Activity activity, Uri uri, int requestCode, String output) {
        if (null == uri) {
            return;
        }

        // 如果没有文件夹，先创建之
        File file = new File(output);
        checkFileParent(file);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("return-data", false);

        activity.startActivityForResult(intent, requestCode);
    }

    // 检查有没有文件夹，没有就创建一个
    private static void checkFileParent(File file) {
        File fileDir = file.getParentFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

}
