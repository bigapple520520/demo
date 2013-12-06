package com.dazzle.bigappleui.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnCloseListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnClosedListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenListener;
import com.dazzle.bigappleui.slidingmenu.SlidingMenu.OnOpenedListener;

/**
 * 侧滑测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-12-3 下午7:01:04 $
 */
public class Main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SlidingMenu slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);

        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        View above = LayoutInflater.from(this).inflate(R.layout.above, null);
        View menu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        slidingMenu.setContent(above);
        slidingMenu.setMenu(menu);
        slidingMenu.setSecondaryMenu(R.layout.menu2);
        slidingMenu.setBehindOffset(50);

        // 侧边的渐变效果
        slidingMenu.setShadowDrawable(R.drawable.view_left_bg);
        slidingMenu.setShadowWidth(30);

        // 选中view的效果
        slidingMenu.setSelectorEnabled(true);
        slidingMenu.setSelectedView(menu);
        slidingMenu.setSelectorDrawable(R.drawable.menu_bg);

        // 各种事件
        slidingMenu.setOnOpenListener(new OnOpenListener() {
            @Override
            public void onOpen() {
                Toast.makeText(Main.this, "setOnOpenListener", Toast.LENGTH_SHORT).show();
            }
        });

        slidingMenu.setSecondaryOnOpenListner(new OnOpenListener() {
            @Override
            public void onOpen() {
                Toast.makeText(Main.this, "setSecondaryOnOpenListner", Toast.LENGTH_SHORT).show();
            }
        });

        slidingMenu.setOnOpenedListener(new OnOpenedListener() {
            @Override
            public void onOpened() {
                Toast.makeText(Main.this, "setOnOpenedListener", Toast.LENGTH_SHORT).show();
            }
        });

        slidingMenu.setOnCloseListener(new OnCloseListener() {
            @Override
            public void onClose() {
                Toast.makeText(Main.this, "setOnCloseListener", Toast.LENGTH_SHORT).show();
            }
        });

        slidingMenu.setOnClosedListener(new OnClosedListener() {
            @Override
            public void onClosed() {
                Toast.makeText(Main.this, "setOnClosedListener", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
