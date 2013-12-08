/* 
 * @(#)DefaultLetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.lettersort;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;
import com.dazzle.bigappleui.lettersort.view.LetterSortAdapter;
import com.dazzle.lettersort.SwipeView.SwipeCompleteListener;

/**
 * 默认实现，如果要自定义自己的列表项目，就自己继承LetterSortAdapter这个来写吧
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class DemoLetterSortAdapter extends LetterSortAdapter {
	private final Context context;

	public DemoLetterSortAdapter(List<ItemContent> fromList, Context context) {
		super(fromList);
		this.context = context;
	}

	@Override
	public View drawItemContent(int position, View convertView,
			ViewGroup parent, ItemContent itemContent) {
		View content = LayoutInflater.from(context).inflate(
				R.layout.letter_show_content, null);
		TextView leftText = (TextView) content.findViewById(R.id.leftText);
		leftText.setText(itemContent.getName());

		Button button = (Button) content.findViewById(R.id.btn);

		View behind = LayoutInflater.from(context).inflate(
				R.layout.letter_show_behind, null);

		View temp = LayoutInflater.from(context).inflate(
				R.layout.swipeview_item, null);
		final SwipeView swipeView = (SwipeView) temp
				.findViewById(R.id.swipeview);
		swipeView.addContentAndBehind(content, behind);
		swipeView.setBehindWidthRes(R.dimen.behind_eidth);
		swipeView.setSwipeCompleteListener(new SwipeCompleteListener() {
			@Override
			public void whichScreen(int which) {
				Toast.makeText(context, "" + which, Toast.LENGTH_SHORT).show();
			}
		});

		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (1 == swipeView.getCurScreen()) {
					swipeView.snapToScreen(0);
				}
			}
		});

		return temp;
	}

	@Override
	public View drawItemDivide(int position, View convertView,
			ViewGroup parent, ItemDivide itemDivide) {
		TextView textView = (TextView) LayoutInflater.from(context).inflate(
				R.layout.letter_show_split, null);
		textView.setText(itemDivide.getLetter());
		return textView;
	}

}
