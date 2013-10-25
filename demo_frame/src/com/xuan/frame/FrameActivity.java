package com.xuan.frame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FrameActivity extends Activity {

    private ImageButton[] tabBtns;
    private final Integer[] normalIcons = new Integer[] { R.drawable.tabbtn1_normal, R.drawable.tabbtn2_normal,
            R.drawable.tabbtn3_normal, R.drawable.tabbtn4_normal };
    private final Integer[] pressedIcons = new Integer[] { R.drawable.tabbtn1_pressed, R.drawable.tabbtn2_pressed,
            R.drawable.tabbtn3_pressed, R.drawable.tabbtn4_pressed };
    private View[] tabViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        initWidgits();
        gotoTab(0);
    }

    private void initWidgits() {
        // tab页面
        tabViews = new View[4];
        tabViews[0] = (View) findViewById(R.id.tab1);
        tabViews[1] = (View) findViewById(R.id.tab2);
        tabViews[2] = (View) findViewById(R.id.tab3);
        tabViews[3] = (View) findViewById(R.id.tab4);

        // 框架的四个tab按钮
        tabBtns = new ImageButton[tabViews.length];
        tabBtns[0] = (ImageButton) findViewById(R.id.tabBtn1);
        tabBtns[1] = (ImageButton) findViewById(R.id.tabBtn2);
        tabBtns[2] = (ImageButton) findViewById(R.id.tabBtn3);
        tabBtns[3] = (ImageButton) findViewById(R.id.tabBtn4);

        // 四个按钮事件
        for (int i = 0; i < tabBtns.length; i++) {
            tabBtns[i].setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = 0;
                    for (int i = 0; i < tabBtns.length; i++) {
                        if (tabBtns[i].equals(v)) {
                            position = i;
                            break;
                        }
                    }

                    gotoTab(position);
                }
            });
        }
    }

    // 框架四个按钮的跳转方向
    private void gotoTab(int position) {
        // tab按钮选中
        for (int i = 0; i < tabBtns.length; i++) {
            if (i != position) {
                tabBtns[i].setImageResource(normalIcons[i]);
            }
        }
        tabBtns[position].setImageResource(pressedIcons[position]);

        // tab界面显示
        for (int i = 0; i < tabBtns.length; i++) {
            if (i != position) {
                tabViews[i].setVisibility(View.INVISIBLE);
            }
        }

        tabViews[position].setVisibility(View.VISIBLE);
    }

}
