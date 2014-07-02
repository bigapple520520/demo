package com.xuan.zxingutils.lib.decoding;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.zxing.Result;
import com.xuan.zxingutils.lib.camera.CameraManager;
import com.xuan.zxingutils.lib.view.ViewfinderView;

/**
 * 扫描二维码界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-1 下午6:28:28 $
 */
public class ScanActivity extends Activity implements Callback {
    public static final String BARCODE_FORMAT = "barcode.format";
    public static final String RESULT_TEXT = "result.text";

    private static final String TAG = ScanActivity.class.getName();

    private ScanActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private String characterSet;

    // 扫描成功后的声音和震动
    private boolean playBeep;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.10f;

    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);

        surfaceView = new SurfaceView(this);
        frameLayout.addView(surfaceView);

        viewfinderView = new ViewfinderView(this, null);
        frameLayout.addView(viewfinderView);

        setContentView(frameLayout);
        CameraManager.init(getApplication());

        hasSurface = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        }
        else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        }
        catch (IOException ioe) {
            return;
        }
        catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new ScanActivityHandler(this, null, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 得到结果
     * 
     * @param obj
     * @param barcode
     */
    public void handleDecode(Result obj, Bitmap barcode) {
        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();

        if (!onHandleDecode(obj, barcode)) {
            Intent intent = new Intent();
            intent.putExtra(BARCODE_FORMAT, obj.getBarcodeFormat().toString());
            intent.putExtra(RESULT_TEXT, obj.getText());
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    // 子类可以复写这个方法自己处理返回的结果
    protected boolean onHandleDecode(Result obj, Bitmap barcode) {
        return false;
    }

    // 初始化扫描成功后的提示音
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });

            try {
                AssetFileDescriptor file = getAssets().openFd("beep.ogg");
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (Exception e) {
                mediaPlayer = null;
                Log.e(TAG, "初始化声音出错，请在assets目录下添加音频文件beep.ogg", e);
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

}
