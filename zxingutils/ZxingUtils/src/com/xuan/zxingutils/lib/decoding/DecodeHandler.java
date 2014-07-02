/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuan.zxingutils.lib.decoding;

import java.util.Hashtable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.xuan.zxingutils.lib.camera.CameraManager;
import com.xuan.zxingutils.lib.camera.PlanarYUVLuminanceSource;
import com.xuan.zxingutils.lib.config.MessageWhat;

/**
 * 图片解码
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-24 下午4:21:21 $
 */
public final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final ScanActivity activity;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(ScanActivity activity, Hashtable<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
        case MessageWhat.DECODE:
            decode((byte[]) message.obj, message.arg1, message.arg2);
            break;
        case MessageWhat.QUIT:
            Looper.myLooper().quit();
            break;
        }
    }

    /**
     * 解码取景区域内的二维码
     * 
     * @param data
     * @param width
     *            扫描区域的宽
     * @param height
     *            扫描区域的高
     */
    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(data, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        }
        catch (ReaderException re) {
            // continue
        }
        finally {
            multiFormatReader.reset();
        }

        if (rawResult != null) {
            long end = System.currentTimeMillis();
            Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());

            Message message = Message.obtain(activity.getHandler(), MessageWhat.DECODE_SUCCEEDED, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        }
        else {
            Message message = Message.obtain(activity.getHandler(), MessageWhat.DECODE_FAILED);
            message.sendToTarget();
        }
    }

}
