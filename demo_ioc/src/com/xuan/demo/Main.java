package com.xuan.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.xuan.demo.ioc.AnActivity;
import com.xuan.demo.ioc.InjectParamThis;
import com.xuan.demo.ioc.InjectView;

public class Main extends AnActivity {

	// ���������ע�⣬�ȼ����ڴ�����������ʼ����helloTextView =
	// (TextView)findViewById(R.id.helloTextView)
	@InjectView(R.id.helloTextView)
	private TextView helloTextView;

	// ���������ע�⣬�ȼ����ڴ�����������ʼ����progressDialog = new ProgressDialog(this);
	@InjectParamThis(ProgressDialog.class)
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		helloTextView.setText("��ã�����demo_ioc�Ĳ��ԡ���˳����ȡ����TextView����");

		progressDialog.setTitle("�����ؼ����԰���ȡ����");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}
