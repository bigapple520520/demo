package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuan.demo.launcher.LauncherLayout2;
import com.xuan.demo.launcher.event.OnScrollCompleteListener;
import com.xuan.demo.launcher.event.ScrollEvent;

/**
 * 模仿launcher左右滑动的效果哦
 * 
 * @author xuan
 */
public class Main_LauncherLayout2 extends Activity {

	private LauncherLayout2 launcherLayout2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_launcherlayout2);

		launcherLayout2 = (LauncherLayout2) findViewById(R.id.launcherLayout2);

		// 设置偏移量
		launcherLayout2.setOffsetSize(50);

		// 设置滚动后的监听器
		launcherLayout2
				.setOnScrollCompleteLinstenner(new OnScrollCompleteListener() {
					@Override
					public void onScrollComplete(ScrollEvent scrollEvent) {
						Toast.makeText(Main_LauncherLayout2.this,
								"我滑动到了屏幕：" + scrollEvent.curScreen,
								Toast.LENGTH_SHORT).show();
					}
				});

		// 设置要切屏显示的view
		ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.image_layout, null);
		pic1.setImageResource(R.drawable.pic1);

		ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.image_layout, null);
		pic2.setImageResource(R.drawable.pic2);

		ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.image_layout, null);
		pic3.setImageResource(R.drawable.pic3);

		ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.image_layout, null);
		pic4.setImageResource(R.drawable.pic4);

		launcherLayout2.addView(pic1);
		launcherLayout2.addView(pic2);
		launcherLayout2.addView(pic3);
		launcherLayout2.addView(pic4);
	}
}
