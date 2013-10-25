package com.dazzle.ui.slipbutton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dazzle.ui.slipbutton.view.OnChangedListener;
import com.dazzle.ui.slipbutton.view.SlipButton;

/**
 * SlipButton测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-24 下午8:00:10 $
 */
public class Main extends Activity {

    private SlipButton slipButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        slipButton = (SlipButton) findViewById(R.id.slipButton);
        slipButton.SetOnChangedListener(new OnChangedListener() {
            @Override
            public void OnChanged(boolean checkState) {
                if (checkState) {
                    Toast.makeText(Main.this, "我被选中", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Main.this, "我被取消", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
