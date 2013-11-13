package com.xuan.slidingmenu.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuItem;

import com.xuan.slidingmenu.R;
import com.xuan.slidingmenu.lib.SlidingMenu;
import com.xuan.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

    private final int mTitleRes;
    protected ListFragment mFrag;

    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new SampleListFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        }
        else {
            mFrag = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
        getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setFadeDegree(0.35f);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
