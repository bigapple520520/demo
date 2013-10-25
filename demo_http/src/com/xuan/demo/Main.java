package com.xuan.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.xuan.demo.http.AnHttp;
import com.xuan.demo.http.helper.AjaxCallBack;

/**
 * 记得加上网路访问的权限<br>
 * <uses-permission android:name="android.permission.INTERNET" /><br>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author xuan
 */
public class Main extends Activity {

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TextView textView = (TextView) findViewById(R.id.textView);

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("正在加载");
		progressDialog.show();

		AnHttp http = new AnHttp();
		http.post("http://jxhl.edu88.com", null, new AjaxCallBack() {
			@Override
			public void callBack(final boolean isSuccess, final String result,
					Exception e) {
				// 后期可以考虑吧handler整合进去，除了主线程，别的线程不能对UI进行操作
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (isSuccess) {
							textView.setText(result);
						} else {
							Toast.makeText(Main.this, "请求出错了",
									Toast.LENGTH_SHORT).show();
						}

						progressDialog.dismiss();
					}
				});
			}
		});

		Toast.makeText(Main.this, "因为是已步的，所以在加载还没完成前，我就可以被执行的",
				Toast.LENGTH_SHORT).show();
	}

}
