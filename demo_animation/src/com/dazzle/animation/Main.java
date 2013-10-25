package com.dazzle.animation;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.animation.view.FrameImageView;
import com.dazzle.animation.view.GifImageView;

public class Main extends Activity {

    private FrameImageView frameAnimationView;
    private GifImageView gifImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // testFrameAnimationView();
        testGifImageView();
    }

    private void testGifImageView() {
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.pic);
    }

    private void testFrameAnimationView() {
        // frameAnimationView = (FrameAnimationView) findViewById(R.id.frameAnimationView);
        //
        // // 参数设置
        // frameAnimationView.setInterval(200);
        // frameAnimationView.setOneShot(false);
        //
        // // 设置资源
        // int[] resids = new int[] { R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4 };
        // frameAnimationView.init(resids);
        //
        // // 开始
        // frameAnimationView.start();
    }

}
