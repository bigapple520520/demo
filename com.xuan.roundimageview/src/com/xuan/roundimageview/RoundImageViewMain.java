package com.xuan.roundimageview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * 圆角图片demo
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-12-26 下午9:25:37 $
 */
public class RoundImageViewMain extends Activity {

    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_image_view_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setTag(100);// 这里设置圆角

        AnBitmapUtilsFace
                .instance(this)
                .display(
                        imageView,
                        "http://g.hiphotos.baidu.com/image/w%3D2048/sign=8fb0b92a57fbb2fb342b5f127b7221a4/37d3d539b6003af3ecd96d17372ac65c1038b662.jpg");
        AnBitmapUtilsFace.instance(this).clearCache();
    }

}
