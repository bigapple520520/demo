package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.xuan.demo.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.demo.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.demo.asynctask.helper.Result;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 就这样就不会出现等待提示框，适合后台http请求
		// DemoTask demoTask = new DemoTask(this, false);

		// 这样会体现等待提示框
		DemoTask demoTask = new DemoTask(this);

		demoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<String>() {
			@Override
			public void successCallback(Result<String> result) {
				String value = result.getMessage();
				Toast.makeText(Main.this, "成功的测试：" + value, Toast.LENGTH_SHORT)
						.show();
			}
		});

		demoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
			@Override
			public void failCallback(Result<String> result) {
				String value = result.getMessage();
				Toast.makeText(Main.this, "失败的测试：" + value, Toast.LENGTH_SHORT)
						.show();
			}
		});

		demoTask.execute(new Object[] { "xuan" });
	}
}
