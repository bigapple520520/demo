/* 
 * @(#)MediaPlayerUtils.java    Created on 2012-12-13
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.media;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * 播放器工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-12-13 下午12:20:12 $
 */
public class MediaPlayerModel {

    // 保持单例
    private MediaPlayer mediaPlayer;

    /**
     * 播放音频
     * 
     * @param id
     *            文件名
     */
    public void playVoice(String fileName) {
        prepareMediaPlayer();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取MediaPlayer对象
     * 
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        prepareMediaPlayer();

        return mediaPlayer;
    }

    // 懒加载模式，第一次使用时才实例化对象
    private void prepareMediaPlayer() {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }
    }

}
