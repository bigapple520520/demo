package com.xuan.viewpage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuan.viewpage.event.OnScrollCompleteListener;
import com.xuan.viewpage.event.ScrollEvent;

public class ViewPlayMain extends Activity {
    private ViewPlay viewPlay;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_play_main);

        viewPlay = (ViewPlay) findViewById(R.id.viewPlay);

        // 设置滚动后的监听器
        viewPlay.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                Toast.makeText(ViewPlayMain.this, "1我滑动到了屏幕：" + scrollEvent.curScreen, Toast.LENGTH_SHORT).show();
            }
        });

        viewPlay.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                Toast.makeText(ViewPlayMain.this, "2我滑动到了屏幕：" + scrollEvent.curScreen, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置要切屏显示的view
        ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);

        viewPlay.addView(pic1);
        viewPlay.addView(pic2);
        viewPlay.addView(pic3);
        viewPlay.addView(pic4);

        pic1.setImageResource(R.drawable.pic1);
        pic2.setImageResource(R.drawable.pic2);
        pic3.setImageResource(R.drawable.pic3);
        pic4.setImageResource(R.drawable.pic4);

        // 开始轮播
        viewPlay.startAutoPlay(3000);

        button = (Button) findViewById(R.id.button);
        button.setText("暂停播放");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPlay.isPause()) {
                    viewPlay.setPause(false);
                    button.setText("暂停播放");
                }
                else {
                    viewPlay.setPause(true);
                    button.setText("开始播放");
                }
            }
        });
    }

}
