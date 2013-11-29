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
import android.widget.TextView;

import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;
import com.dazzle.bigappleui.lettersort.view.LetterSortAdapter;

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
    public View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent) {
        View view = LayoutInflater.from(context).inflate(R.layout.letter_show_content, null);
        TextView leftText = (TextView) view.findViewById(R.id.leftText);
        leftText.setText(itemContent.getName());
        return view;
    }

    @Override
    public View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.letter_show_split, null);
        textView.setText(itemDivide.getLetter());
        return textView;
    }

}
