package com.dazzle.lettersort;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.lettersort.entity.ItemContent;
import com.dazzle.lettersort.view.DefaultLetterSortAdapter;
import com.dazzle.lettersort.view.LetterSortView;

public class Main extends Activity {

    private LetterSortView letterSortView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // 加载数据
        ItemContent ic = new ItemContent("徐安", null);
        List<ItemContent> dataList = new ArrayList<ItemContent>();
        dataList.add(ic);

        ic = new ItemContent("大王", null);
        dataList.add(ic);
        ic = new ItemContent("小王", null);
        dataList.add(ic);
        ic = new ItemContent("红红", null);
        dataList.add(ic);
        ic = new ItemContent("黄黄", null);
        dataList.add(ic);
        ic = new ItemContent("黄小", null);
        dataList.add(ic);
        ic = new ItemContent("黄3", null);
        dataList.add(ic);
        ic = new ItemContent("妮妮", null);
        dataList.add(ic);
        ic = new ItemContent("艹但", null);
        dataList.add(ic);
        ic = new ItemContent("艹小", null);
        dataList.add(ic);
        ic = new ItemContent("而是", null);
        dataList.add(ic);
        ic = new ItemContent("ABC", null);
        dataList.add(ic);
        ic = new ItemContent("ccc", null);
        dataList.add(ic);

        letterSortView = (LetterSortView) findViewById(R.id.letterSortView);
        letterSortView.getListView().setAdapter(new DefaultLetterSortAdapter(dataList, this));
    }

}
