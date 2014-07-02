/*
 * Copyright 2009 ZXing authors
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

package com.xuan.zxingutils.lib.camera;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

/**
 * LuminanceSource实现
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-24 下午4:18:59 $
 */
public final class PlanarYUVLuminanceSource extends LuminanceSource {
    private final byte[] yuvData;
    private final int dataWidth;
    private final int dataHeight;
    private int left;
    private int top;

    /**
     * 自动检测二维码
     * 
     * @param bitmap
     */
    public PlanarYUVLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
        this.yuvData = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

        for (int i = 0; i < data.length; i++) {
            this.yuvData[i] = (byte) data[i];
        }

        this.dataWidth = bitmap.getWidth();
        this.dataHeight = bitmap.getHeight();
    }

    public PlanarYUVLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, int left, int top, int width,
            int height) {
        super(width, height);

        if (left + width > dataWidth || top + height > dataHeight) {
            throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
        }

        this.yuvData = yuvData;
        this.dataWidth = dataWidth;
        this.dataHeight = dataHeight;
        this.left = left;
        this.top = top;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }

        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }

        int offset = (y + top) * dataWidth + left;
        System.arraycopy(yuvData, offset, row, 0, width);
        return row;
    }

    @Override
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();

        // 没有边框，整张图都是二维码数据
        if (width == dataWidth && height == dataHeight) {
            return yuvData;
        }

        int area = width * height;
        byte[] matrix = new byte[area];
        int inputOffset = top * dataWidth + left;

        // 实际二维码和数据图片的宽度一样
        if (width == dataWidth) {
            System.arraycopy(yuvData, inputOffset, matrix, 0, area);
            return matrix;
        }

        // 拷贝出中间二维码部分
        byte[] yuv = yuvData;
        for (int y = 0; y < height; y++) {
            int outputOffset = y * width;
            System.arraycopy(yuv, inputOffset, matrix, outputOffset, width);
            inputOffset += dataWidth;
        }
        return matrix;
    }

    @Override
    public boolean isCropSupported() {
        return true;
    }

    public int getDataWidth() {
        return dataWidth;
    }

    public int getDataHeight() {
        return dataHeight;
    }

    /**
     * 得到中间实际二维码部分
     * 
     * @return
     */
    public Bitmap renderCroppedGreyscaleBitmap() {
        int width = getWidth();
        int height = getHeight();
        int[] pixels = new int[width * height];
        byte[] yuv = yuvData;
        int inputOffset = top * dataWidth + left;

        for (int y = 0; y < height; y++) {
            int outputOffset = y * width;
            for (int x = 0; x < width; x++) {
                int grey = yuv[inputOffset + x] & 0xff;
                pixels[outputOffset + x] = 0xFF000000 | (grey * 0x00010101);
            }
            inputOffset += dataWidth;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
