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
		Log.i("--------------------", "���Է���ִ����");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (null == ahostActivity) {
			super.onCreate(savedInstanceState);
			ahostActivity = this;
		}

		TextView t = new TextView(ahostActivity);
		t.setText("���ǲ���");
		ahostActivity.setContentView(t);
	}

	@Override
	public void onStart() {
		Log.i("--------------------", "onStart������");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.i("--------------------", "onResume������");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i("--------------------", "onPause������");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.i("--------------------", "onStop������");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("--------------------", "onDestroy������");
		super.onDestroy();
	}

}
