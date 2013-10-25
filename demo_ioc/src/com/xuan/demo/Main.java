package com.xuan.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.xuan.demo.ioc.AnActivity;
import com.xuan.demo.ioc.InjectParamThis;
import com.xuan.demo.ioc.InjectView;

public class Main extends AnActivity {

	// 用了下面的注解，等价于在代码中这样初始化：helloTextView =
	// (TextView)findViewById(R.id.helloTextView)
	@InjectView(R.id.helloTextView)
	private TextView helloTextView;

	// 用了下面的注解，等价于在代码中这样初始化：progressDialog = new ProgressDialog(this);
	@InjectParamThis(ProgressDialog.class)
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helloTextView.setText("你好，我是demo_ioc的测试。并顺利的取到了TextView对象");

		progressDialog.setTitle("按返回键可以把我取消的");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}
