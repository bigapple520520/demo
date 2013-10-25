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
 * �ǵü�����·���ʵ�Ȩ��<br>
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
		progressDialog.setTitle("���ڼ���");
		progressDialog.show();

		AnHttp http = new AnHttp();
		http.post("http://jxhl.edu88.com", null, new AjaxCallBack() {
			@Override
			public void callBack(final boolean isSuccess, final String result,
					Exception e) {
				// ���ڿ��Կ��ǰ�handler���Ͻ�ȥ���������̣߳�����̲߳��ܶ�UI���в���
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (isSuccess) {
							textView.setText(result);
						} else {
							Toast.makeText(Main.this, "���������",
									Toast.LENGTH_SHORT).show();
						}

						progressDialog.dismiss();
					}
				});
			}
		});

		Toast.makeText(Main.this, "��Ϊ���Ѳ��ģ������ڼ��ػ�û���ǰ���ҾͿ��Ա�ִ�е�",
				Toast.LENGTH_SHORT).show();
	}

}
