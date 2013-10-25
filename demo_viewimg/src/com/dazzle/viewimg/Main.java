package com.dazzle.viewimg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.dazzle.viewimg.view.ZoomImageView;

public class Main extends Activity {

    private ZoomImageView zoomImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
        zoomImageView.setImageResource(R.drawable.pic);

        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main.this.finish();
            }
        });
    }

}
