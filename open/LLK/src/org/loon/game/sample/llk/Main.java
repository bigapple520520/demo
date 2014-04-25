package org.loon.game.sample.llk;

import org.loon.framework.android.game.LGameAndroid2DActivity;

/**
 * 主界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-24 下午8:06:44 $
 */
public class Main extends LGameAndroid2DActivity {
    @Override
    public void onMain() {
        initialization(true);
        setFPS(40);
        setScreen(new LLKScreen());
        setShowLogo(false);
        setShowFPS(true);
        showScreen();
    }

}
