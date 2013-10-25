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

		// �������Ͳ�����ֵȴ���ʾ���ʺϺ�̨http����
		// DemoTask demoTask = new DemoTask(this, false);

		// ���������ֵȴ���ʾ��
		DemoTask demoTask = new DemoTask(this);

		demoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<String>() {
			@Override
			public void successCallback(Result<String> result) {
				String value = result.getMessage();
				Toast.makeText(Main.this, "�ɹ��Ĳ��ԣ�" + value, Toast.LENGTH_SHORT)
						.show();
			}
		});

		demoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
			@Override
			public void failCallback(Result<String> result) {
				String value = result.getMessage();
				Toast.makeText(Main.this, "ʧ�ܵĲ��ԣ�" + value, Toast.LENGTH_SHORT)
						.show();
			}
		});

		demoTask.execute(new Object[] { "xuan" });
	}
}
