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

public class ViewPageMain extends Activity {
    private ViewPage viewPage;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_main);

        viewPage = (ViewPage) findViewById(R.id.viewPage);

        // 设置偏移量
        // viewPage.setOffset(50);

        // 设置滚动后的监听器
        viewPage.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                Toast.makeText(ViewPageMain.this, "1我滑动到了屏幕：" + scrollEvent.curScreen, Toast.LENGTH_SHORT).show();
            }
        });

        viewPage.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                Toast.makeText(ViewPageMain.this, "2我滑动到了屏幕：" + scrollEvent.curScreen, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置要切屏显示的view
        ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);
        ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.image_layout, null);

        viewPage.addView(pic1);
        viewPage.addView(pic2);
        viewPage.addView(pic3);
        viewPage.addView(pic4);

        pic1.setImageResource(R.drawable.pic1);
        pic2.setImageResource(R.drawable.pic2);
        pic3.setImageResource(R.drawable.pic3);
        pic4.setImageResource(R.drawable.pic4);

        // 设置按钮，暴力跳转到指定界面
        button = (Button) findViewById(R.id.button);
        button.setText("跳转到第1个界面");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPage.setToScreen(0);
            }
        });
    }

}
