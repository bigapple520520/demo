/* 
 * @(#)DefaultLetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.lettersort.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazzle.lettersort.entity.ItemContent;
import com.dazzle.lettersort.entity.ItemDivide;

/**
 * 默认实现，如果要自定义自己的列表项目，就自己继承LetterSortAdapter这个来写吧
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class DefaultLetterSortAdapter extends LetterSortAdapter {
    private final Context context;

    public DefaultLetterSortAdapter(List<ItemContent> fromList, Context context) {
        super(fromList);
        this.context = context;
    }

    @Override
    public View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent) {
        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setText(itemContent.getName());
        return textView;
    }

    @Override
    public View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide) {
        TextView textView = new TextView(context);
        textView.setTextSize(40);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);
        textView.setText(itemDivide.getLetter());
        return textView;
    }

}
