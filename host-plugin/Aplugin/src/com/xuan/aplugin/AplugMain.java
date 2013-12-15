package com.xuan.aplugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AplugMain extends Activity {

	private Activity ahostActivity;

	public void setActivity(Activity ahostActivity) {
		this.ahostActivity = ahostActivity;
	}

	public void test() {
		Log.i("--------------------", "测试方法执行了");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (null == ahostActivity) {
			super.onCreate(savedInstanceState);
			ahostActivity = this;
		}

		TextView t = new TextView(ahostActivity);
		t.setText("我是测试");
		ahostActivity.setContentView(t);
	}

	@Override
	public void onStart() {
		Log.i("--------------------", "onStart被调了");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.i("--------------------", "onResume被调了");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i("--------------------", "onPause被调了");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.i("--------------------", "onStop被调了");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("--------------------", "onDestroy被调了");
		super.onDestroy();
	}

}
