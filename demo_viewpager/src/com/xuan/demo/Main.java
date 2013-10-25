package com.xuan.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

/**
 * 实现页面滑动切换
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-20 下午4:13:10 $
 */
public class Main extends Activity {

    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;

    private PagerAdapter pagerAdapter;// 设配器
    private ArrayList<View> viewList;// 数据源
    private ArrayList<String> titleList;// 标题的数据源，当布局中布局了PagerTitleStrip，才会体现出有效果

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 初始化控件
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagerTitleStrip);

        // 初始化数据
        viewList = new ArrayList<View>();
        ImageView view1 = new ImageView(Main.this);
        view1.setBackgroundResource(R.drawable.pic1);
        viewList.add(view1);

        ImageView view2 = new ImageView(Main.this);
        view2.setBackgroundResource(R.drawable.pic2);
        viewList.add(view2);

        ImageView view3 = new ImageView(Main.this);
        view3.setBackgroundResource(R.drawable.pic3);
        viewList.add(view3);

        ImageView view4 = new ImageView(Main.this);
        view4.setBackgroundResource(R.drawable.pic4);
        viewList.add(view4);

        // 标题数据准备
        titleList = new ArrayList<String>();
        titleList.add("view1");
        titleList.add("view2");
        titleList.add("view3");
        titleList.add("view4");

        // 填充ViewPager的数据适配器
        pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(viewList.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(viewList.get(position));
                return viewList.get(position);
            }
        };

        viewPager.setAdapter(pagerAdapter);

        // 设置页面切换事件
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // 切换后触发
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // 正在滚动时触发
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // 当滚动条状态发生变化时，可以确定用户开始滚动，或者滚动结束
            }
        });
    }

}
