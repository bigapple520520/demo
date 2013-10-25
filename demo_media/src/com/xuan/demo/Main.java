package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xuan.demo.media.MediaPlayerModel;
import com.xuan.demo.media.MediaRecorderModel;
import com.xuan.demo.media.MediaRecorderModel.OnRecordStartedListener;
import com.xuan.demo.media.MediaRecorderModel.OnRecordStopedListener;

/**
 * 记得要加权限<br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /><br>
 * <uses-permission android:name="android.permission.RECORD_AUDIO" /><br>
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-6-28 下午1:10:20 $
 */
public class Main extends Activity {

    private Button recorderBtn;
    private Button playBtn;
    private TextView showFileName;
    private final MediaRecorderModel mediaRecorderModel = new MediaRecorderModel();
    private final MediaPlayerModel mediaPlayerModel = new MediaPlayerModel();

    private final String testPath = Environment.getExternalStorageDirectory().getPath() + "/xuantest/";
    private final String testFile = testPath + "test.amr";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recorderBtn = (Button) findViewById(R.id.recorderBtn);
        playBtn = (Button) findViewById(R.id.playBtn);
        showFileName = (TextView) findViewById(R.id.showFileName);

        recorderBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mediaRecorderModel.checkFile(testPath);
                    mediaRecorderModel.startRecord(testFile, new OnRecordStartedListener() {
                        @Override
                        public void onRecordStarted() {
                            recorderBtn.setText(R.string.zheng_zai_ly);
                        }
                    });
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mediaRecorderModel.stopRecord(new OnRecordStopedListener() {
                        @Override
                        public void onRecordStoped(boolean success, String fileName) {
                            if (success) {
                                Toast.makeText(Main.this, "录音成功，地址看下面", Toast.LENGTH_SHORT).show();
                                showFileName.setText(testFile);
                            }
                        }

                        @Override
                        public void onTooShort() {
                            Toast.makeText(Main.this, "时间太短没法录音", Toast.LENGTH_SHORT).show();
                        }
                    });

                    recorderBtn.setText(R.string.an_zhu_wkssh);
                }

                return false;
            }
        });

        playBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerModel.playVoice(testFile);
            }
        });
    }
}
