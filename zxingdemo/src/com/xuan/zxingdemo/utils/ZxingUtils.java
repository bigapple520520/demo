/* 
 * @(#)ZxingUtils.java    Created on 2013-10-24
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.zxingdemo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.xuan.zxingdemo.camera.PlanarYUVLuminanceSource;
import com.xuan.zxingdemo.decoding.DecodeFormatManager;

/**
 * 使用zxing生成和识别二维码
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-24 上午10:58:14 $
 */
public abstract class ZxingUtils {

    // ////////////////////////////////////////////生成二维码////////////////////////////////////////////////////////
    /**
     * 生成二维码图片
     * 
     * @param content
     * @param zxingConfig
     * @return
     * @throws WriterException
     */
    public static Bitmap encodeToBitmap(String content, ZxingEncodeConfig zxingConfig) throws WriterException,
            Exception {
        if (TextUtils.isEmpty(content)) {
            throw new Exception("content can not be empty");
        }

        if (null == zxingConfig) {
            zxingConfig = new ZxingEncodeConfig();
        }

        String encoding = zxingConfig.getEncoding();
        if (TextUtils.isEmpty(encoding)) {
            encoding = getAppropriateEncoding(content);
        }

        Hashtable<EncodeHintType, String> hints = null;
        if (null != encoding) {
            hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(content, BarcodeFormat.QR_CODE, zxingConfig.getBitmapWidth(),
                zxingConfig.getBitmapHeight(), hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? zxingConfig.getColor() : zxingConfig.getBgColor();
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        if (zxingConfig.isSaveFile()) {
            if (TextUtils.isEmpty(zxingConfig.getSaveFileName())) {
                throw new Exception("saveFileName can not be empty");
            }

            File file = new File(zxingConfig.getSaveFileName());
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
            catch (Exception e) {
                throw e;
            }
        }

        return bitmap;
    }

    public static Bitmap encodeToBitmap(String content) throws WriterException, Exception {
        return encodeToBitmap(content, (ZxingEncodeConfig) null);
    }

    public static Bitmap encodeToBitmap(String content, String saveFileName) throws WriterException, Exception {
        if (TextUtils.isEmpty(saveFileName)) {
            throw new Exception("saveFileName can not be empty");
        }

        return encodeToBitmap(content, new ZxingEncodeConfig(saveFileName));
    }

    // 含有中文类的字符就使用UTF-8编码
    private static String getAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    // ////////////////////////////////////////////读取识别二维码////////////////////////////////////////////////////////
    /**
     * 解码二维码
     * 
     * @param bitmap
     * @param zxingDecodeConfig
     * @return
     */
    public static String decodeFromBitmap(Bitmap bitmap) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);

        // 可以解析的编码类型，这里设置可扫描的类型，我这里选择了都支持
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(
                    new PlanarYUVLuminanceSource(bitmap))));
        }
        catch (NotFoundException e) {
            e.printStackTrace();
        }

        return rawResult.getText();
    }

}
