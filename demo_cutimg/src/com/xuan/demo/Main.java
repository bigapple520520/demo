package com.xuan.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuan.demo.cutimg.CutImgUtils;

/**
 * 注意要加上外部存储的权限<br>
 * 
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-2 下午5:58:34 $
 */
public class Main extends Activity {
    private static final int LOAD_IMAGE_FROM_ALBUM = 1;// 从相册获取图片
    private static final int LOAD_IMAGE_FROM_CAMERA = 3;// 从拍照获取图片
    private static final int REQUEST_CUT_IMAGE_4_ALBUM = 2;// 相册截图
    private static final int REQUEST_CUT_IMAGE_4_CAMERA = 4;// 拍照截图

    private static final String ALBUM_CUT = Environment.getExternalStorageDirectory() + "/xuandemo/album_cut.png";
    private static final String CAMERA_CUT = Environment.getExternalStorageDirectory() + "/xuandemo/camera_cut.png";

    private Button button1;
    private Button button2;
    private TextView textview;

    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        textview = (TextView) findViewById(R.id.textview);

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CutImgUtils.getMediaStoreImage(Main.this, LOAD_IMAGE_FROM_ALBUM);
            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = CutImgUtils.getImageFromCamera(Main.this, LOAD_IMAGE_FROM_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        // 从相册获取照片，保存大图，并走去截图
        if (LOAD_IMAGE_FROM_ALBUM == requestCode) {
            CutImgUtils.getCutImage(Main.this, data.getData(), REQUEST_CUT_IMAGE_4_ALBUM, ALBUM_CUT);
            return;
        }

        // 从拍照获取图片，保存大图，并走去截图
        if (LOAD_IMAGE_FROM_CAMERA == requestCode) {
            CutImgUtils.getCutImage(Main.this, uri, REQUEST_CUT_IMAGE_4_CAMERA, CAMERA_CUT);
            return;
        }

        if (REQUEST_CUT_IMAGE_4_ALBUM == requestCode) {
            textview.setText("图片已经保存在：" + ALBUM_CUT + "不信你去看看");
            return;
        }

        if (REQUEST_CUT_IMAGE_4_CAMERA == requestCode) {
            textview.setText("图片已经保存在：" + CAMERA_CUT + "不信你去看看");
            return;
        }
    }

}
