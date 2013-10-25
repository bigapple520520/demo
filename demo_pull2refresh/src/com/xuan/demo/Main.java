package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xuan.demo.pull2refresh.PullToRefreshListView;

public class Main extends Activity {

	// 下拉刷新控件和设配器和数据
	private PullToRefreshListView listView;
	private ArrayAdapter<String> arrayAdapter;
	private final String[] data = new String[] { "aaa", "bbb", "ccc", "ddd",
			"eee", "fff", "ggg", "nnn", "mmm", "yyy", "iii" };

	// 底部内容
	private View listView_footer;
	private TextView listView_foot_more;
	private ProgressBar listView_foot_progress;

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 初始化底部
		listView_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		listView_foot_more = (TextView) listView_footer
				.findViewById(R.id.listview_foot_more);
		listView_foot_progress = (ProgressBar) listView_footer
				.findViewById(R.id.listview_foot_progress);

		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.addFooterView(listView_footer);// 添加底部必须在设置设配器之前
		arrayAdapter = new ArrayAdapter<String>(Main.this,
				android.R.layout.simple_list_item_1, data);
		listView.setAdapter(arrayAdapter);

		// 下拉刷新通知，用于监听下拉刷新哦
		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 模拟加载用了5s
						try {
							Thread.sleep(5000);
						} catch (Exception e) {
						}

						// 更新好后用handler更新列表的状态为done
						handler.post(new Runnable() {
							@Override
							public void run() {
								// 设置最近刷新提示给用户看的
								listView.onRefreshComplete("最新更新时间：2012-12-12 13：45");
							}
						});
					}
				}).start();
			}
		});

		// 滚动事件，主要监听下滚动到最后时，触发更多的数据添加
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listView.onScrollStateChanged(view, scrollState);

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(listView_footer) == view
							.getLastVisiblePosition()) {
						scrollEnd = true;
					}
				} catch (Exception e) {
					scrollEnd = false;
				}

				if (scrollEnd) {
					listView_foot_more
							.setText(R.string.pull_to_refresh_refreshing_label);
					listView_foot_progress.setVisibility(View.VISIBLE);

					new Thread(new Runnable() {
						@Override
						public void run() {
							// 模拟加载用了5s
							try {
								Thread.sleep(5000);
							} catch (Exception e) {
							}

							// 更新好后用handler更新列表的状态为done
							handler.post(new Runnable() {
								@Override
								public void run() {
									listView_foot_more
											.setText(R.string.footbar_more);
									listView_foot_progress
											.setVisibility(View.GONE);
								}
							});
						}
					}).start();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listView.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
	}
}
