/*
 * @(#)Base64Utils.java    Created on 2014-5-28
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.util.base64;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * base64编码，可以把字节流编码成一种可见字符
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-28 下午3:42:50 $
 */
public abstract class Base64Utils {

    /**
     * 字节数组编码成可见base64字符串
     *
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return null;
        }

        try {
            return Base64Encoder.encode(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 字符串解码成字符串
     *
     * @param str
     * @return
     */
    public static byte[] decode(String str) {
        if (null == str || str.length() == 0) {
            return null;
        }

        try {
            return Base64Decoder.decodeToBytes(str);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        byte[] dd = null;
        try {
            dd = FileUtils.readFileToByteArray(new File("D://1.jpg"));
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String test = "1";
        String encodeTest = encode(dd);
        // System.out.println(test);
        System.out.println(encodeTest);
        // System.out.println(new String(decode(encodeTest)));
    }

}
