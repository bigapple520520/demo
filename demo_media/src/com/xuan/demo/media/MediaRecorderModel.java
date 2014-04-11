/* 
 * @(#)AudioRecordModel.java    Created on 2012-9-24
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id: MediaRecordModel.java 31004 2012-09-28 11:33:03Z huangwq $
 */
package com.xuan.demo.media;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

/**
 * 录音器工具类，依赖FileUtils和UUIDUtils
 * 
 * @author xuan
 * @version $Revision: 31004 $, $Date: 2012-09-28 19:33:03 +0800 (周五, 28 九月 2012) $
 */
public class MediaRecorderModel {
    private static final String TAG = "xuan.media.recorder.tag";

    private MediaRecorder mediaRecorder;// 录音器

    private volatile long lastStartTimeMillis;// 记录开始录音时间，跟结束录音做对比看是否超过某个时间
    private boolean isStarted = false;// 标记是否正在录音

    private final Handler handler = new Handler();

    private String voiceFileName;// 录音文件存放的位置

    public String getVersionId() {
        return mediaRecorder.toString();
    }

    /**
     * 开始录制
     * 
     * @param voiceFileName
     * @param onRecordStartedListener
     */
    public void startRecord(String fileName, final OnRecordStartedListener onRecordStartedListener) {
        voiceFileName = fileName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isStarted) {
                    Log.d(TAG, "startRecord isStarted");
                    return;
                }

                try {
                    isStarted = true;
                    Log.d(TAG, "startRecord1");
                    prepareMediaRecorder();

                    mediaRecorder.setOnInfoListener(new OnInfoListener() {
                        @Override
                        public void onInfo(MediaRecorder mr, int what, int extra) {
                            Log.d(TAG, "info:" + what + "," + extra);
                        }
                    });

                    mediaRecorder.setOnErrorListener(new OnErrorListener() {
                        @Override
                        public void onError(MediaRecorder mr, int what, int extra) {
                            Log.d(TAG, "error:" + what + "," + extra);
                        }
                    });

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

                    // Preparing状态
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    mediaRecorder.setOutputFile(voiceFileName);

                    try {
                        mediaRecorder.prepare();
                    }
                    catch (Exception e) {
                        Log.e(TAG, "", e);
                    }

                    // Prepared状态
                    mediaRecorder.start(); // 开始录音

                    // 通知录音开始
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onRecordStartedListener.onRecordStarted();
                        }
                    });

                    lastStartTimeMillis = System.currentTimeMillis();
                    Log.d(TAG, "startRecord2");
                }
                catch (Exception e) {
                    Log.e(TAG, "", e);
                }

            }
        }).start();
    }

    /**
     * 结束录制
     * 
     * @param onRecordStopedListener
     */
    public void stopRecord(final OnRecordStopedListener onRecordStopedListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isStarted) {
                    Log.d(TAG, "stopRecord !isStarted");
                    return;
                }

                try {
                    Log.d(TAG, "stopRecord1");
                    long x = System.currentTimeMillis() - lastStartTimeMillis;
                    boolean success = true;

                    // 录音时间如果没超过1S进行onTooShort通知
                    if (x < 1000) {
                        success = false;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onRecordStopedListener.onTooShort();
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e) {
                            Log.e(TAG, "", e);
                        }
                    }

                    mediaRecorder.stop();
                    mediaRecorder.reset(); // 重置mediaRecorder对象，使其为空闲状态
                    mediaRecorder.release();// 释放mediaRecorder对象

                    // 删除录音时间太短的文件
                    if (!success) {
                        deleteFolderFile(voiceFileName, true);
                    }

                    final boolean temp = success;

                    // 通知录音结束
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onRecordStopedListener.onRecordStoped(temp, voiceFileName);
                        }
                    });

                    Log.d(TAG, "stopRecord2");
                }
                catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                finally {
                    isStarted = false;
                }
            }
        }).start();
    }

    /**
     * 结束录音监听器
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2012-11-27 下午2:26:10 $
     */
    public static interface OnRecordStopedListener {
        // 结束
        void onRecordStoped(boolean success, String fileName);

        // 录音时间太短
        void onTooShort();
    }

    /**
     * 开始录音监听器
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2012-11-27 下午2:08:06 $
     */
    public static interface OnRecordStartedListener {
        // 开始
        void onRecordStarted();
    }

    /**
     * 删除指定目录下文件及目录
     * 
     * @param filepath
     *            文件或者文件夹路径
     * 
     * @param deleteThisPath
     *            是否需要删除这个本身指定的文件或者文件夹
     * @return
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {
                // 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }

            if (deleteThisPath) {
                // 如果是文件，删除
                if (!file.isDirectory()) {
                    file.delete();
                }
                else {
                    // 目录下没有文件或者目录，删除
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        }
    }

    // 检查录音文件夹是否存在-不存在就新建一个
    public void checkFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    // 懒加载模式，第一次使用时才实例化对象
    private void prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();
    }

}
