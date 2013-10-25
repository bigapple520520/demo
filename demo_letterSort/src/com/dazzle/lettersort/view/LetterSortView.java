/* 
 * @(#)LetterSortView.java    Created on 2013-7-15
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.lettersort.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dazzle.lettersort.view.LetterSortBar.OnLetterChangeListener;
import com.dazzle.lettersort.view.LetterSortBar.OnUpEventListener;

/**
 * 按字母排序分类，并用字母做检索
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-15 下午7:12:47 $
 */
public class LetterSortView extends ViewGroup {
    private final Context context;

    private LetterSortBar letterSortBar;// 字母条控件
    private TextView letterShow;// 字母提示控件
    private ListView listView;// 数据显示列表控件

    private int LetterSortBarWidth = 40;// 字母条的宽度
    private int LetterShowWidth = 100;// 显示字母提示的宽度

    public LetterSortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSortView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    // 添加一些子控件初始化
    private void initView() {
        // 添加ListView
        if (null == listView) {
            listView = new ListView(context);
        }
        addView(listView);

        // 添加字母条
        if (null == letterSortBar) {
            letterSortBar = new LetterSortBar(context);

            letterSortBar.setOnLetterChangeListener(new OnLetterChangeListener() {
                @Override
                public void letterChange(String letter) {
                    letterShow.setText(letter);
                    if (letterShow.getVisibility() != View.VISIBLE) {
                        letterShow.setVisibility(View.VISIBLE);
                    }

                    // 定位ListView的显示区域
                    LetterSortAdapter lsa = (LetterSortAdapter) listView.getAdapter();
                    Integer indexInteger = lsa.getIndexMap().get(letter);
                    final int index = (null == indexInteger) ? -1 : indexInteger;

                    // TODO:怎么这句话不灵啊。。。。。
                    listView.requestFocusFromTouch();
                    listView.setSelection(index);
                }
            });

            letterSortBar.setOnUpEventListener(new OnUpEventListener() {
                @Override
                public void upEvent() {
                    letterShow.setVisibility(View.GONE);
                }
            });
        }
        addView(letterSortBar);

        // 添加字母提示
        if (null == letterShow) {
            letterShow = new TextView(context);
            letterShow.setTextSize(50);
            letterShow.setTextColor(Color.WHITE);
            letterShow.setBackgroundColor(Color.BLACK);
            letterShow.setGravity(Gravity.CENTER);
            letterShow.setVisibility(View.GONE);
            TextPaint tp = letterShow.getPaint();
            tp.setFakeBoldText(true);
        }
        addView(letterShow);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        final int width = getWidth();
        final int height = getHeight();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);

            if (childView instanceof ListView) {
                childView.layout(0, 0, width, height);
            }
            else if (childView instanceof LetterSortBar) {
                childView.layout(width - LetterSortBarWidth, 0, width, height);
            }
            else if (childView instanceof TextView) {
                childView.layout((width - LetterShowWidth) / 2, (height - LetterShowWidth) / 2,
                        (width + LetterShowWidth) / 2, (height + LetterShowWidth) / 2);
            }
        }
    }

    public LetterSortBar getLetterSortBar() {
        return letterSortBar;
    }

    public void setLetterSortBar(LetterSortBar letterSortBar) {
        this.letterSortBar = letterSortBar;
    }

    public TextView getLetterShow() {
        return letterShow;
    }

    public void setLetterShow(TextView letterShow) {
        this.letterShow = letterShow;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public int getLetterSortBarWidth() {
        return LetterSortBarWidth;
    }

    public void setLetterSortBarWidth(int letterSortBarWidth) {
        LetterSortBarWidth = letterSortBarWidth;
    }

    public int getLetterShowWidth() {
        return LetterShowWidth;
    }

    public void setLetterShowWidth(int letterShowWidth) {
        LetterShowWidth = letterShowWidth;
    }

}
