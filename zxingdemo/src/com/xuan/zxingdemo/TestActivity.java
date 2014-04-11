/* 
 * @(#)ProduceActivity.java    Created on 2013-10-23
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.zxingdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuan.zxingdemo.utils.ZxingEncodeConfig;
import com.xuan.zxingdemo.utils.ZxingUtils;

/**
 * 二维码生成
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-23 下午7:07:53 $
 */
public class TestActivity extends Activity {
    private static final int LOAD_IMAGE_FROM_ALBUM = 1;// 从相册获取图片

    private EditText edittext;
    private Button button;
    private Button button2;
    private Button button3;
    private ImageView imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        edittext = (EditText) findViewById(R.id.edittext);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        imageview = (ImageView) findViewById(R.id.imageview);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edittext.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(TestActivity.this, "先输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String fileName = Environment.getExternalStorageDirectory() + "/xuan/test1.png";

                    ZxingEncodeConfig zxingConfig = new ZxingEncodeConfig(fileName);
                    Bitmap bitmap = ZxingUtils.encodeToBitmap(content, zxingConfig);
                    imageview.setImageBitmap(bitmap);

                    Toast.makeText(TestActivity.this, "二维码已保存在：" + fileName, Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Log.e("-------------ProduceActivity-------------", "", e);
                }
            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE_FROM_ALBUM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LOAD_IMAGE_FROM_ALBUM == requestCode) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            imageview.setImageBitmap(bitmap);

            String content = ZxingUtils.decodeFromBitmap(bitmap);
            edittext.setText(content);
        }
    }

}
