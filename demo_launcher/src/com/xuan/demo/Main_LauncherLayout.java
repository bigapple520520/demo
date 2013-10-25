package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuan.demo.launcher.LauncherLayout;
import com.xuan.demo.launcher.event.OnScrollCompleteListener;
import com.xuan.demo.launcher.event.ScrollEvent;

/**
 * 模仿launcher左右滑动的效果哦
 * 
 * @author xuan
 */
public class Main_LauncherLayout extends Activity {

	private LauncherLayout launcherLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_launcherlayout);

		launcherLayout = (LauncherLayout) findViewById(R.id.launcherLayout);

		// 设置滚动后的监听器
		launcherLayout
				.setOnScrollCompleteLinstenner(new OnScrollCompleteListener() {
					@Override
					public void onScrollComplete(ScrollEvent scrollEvent) {
						Toast.makeText(Main_LauncherLayout.this,
								"我滑动到了屏幕：" + scrollEvent.curScreen,
								Toast.LENGTH_SHORT).show();
					}
				});

		// 设置要切屏显示的view
		ImageView pic1 = new ImageView(this);
		pic1.setImageResource(R.drawable.pic1);

		ImageView pic2 = new ImageView(this);
		pic2.setImageResource(R.drawable.pic2);

		ImageView pic3 = new ImageView(this);
		pic3.setImageResource(R.drawable.pic3);

		ImageView pic4 = new ImageView(this);
		pic4.setImageResource(R.drawable.pic4);

		launcherLayout.addView(pic1);
		launcherLayout.addView(pic2);
		launcherLayout.addView(pic3);
		launcherLayout.addView(pic4);

		// 初始化到第二屏，下标是从0开始的嘛
		launcherLayout.setToScreen(1);
	}

}
