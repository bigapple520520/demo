package org.loon.game.sample.llk;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.resource.LPKResource;
import org.loon.framework.android.game.utils.GraphicsUtils;

/**
 * 图片资源仓库，单例模式
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-24 下午8:00:28 $
 */
public class Images {
    private static Images instance;
    private static LImage images[];

    private Images() {
        images = new LImage[17];
        for (int i = 0; i < 8; i++) {
            images[i] = GraphicsUtils.loadImage("res/" + i + ".jpg", false);
        }

        final String res = "res/res.lpk";
        images[8] = LPKResource.openImage(res, "a0.png");
        images[9] = LPKResource.openImage(res, "dot.png");
        images[10] = LPKResource.openImage(res, "background.jpg");
        images[11] = LPKResource.openImage(res, "role0.png");
        images[12] = LPKResource.openImage(res, "role1.png");
        images[13] = LPKResource.openImage(res, "role2.png");
        images[14] = LPKResource.openImage(res, "win.png");
        images[15] = LPKResource.openImage(res, "start.png");
        images[16] = LPKResource.openImage(res, "gameover.png");
    }

    /**
     * 获取指定的图片资源
     * 
     * @param i
     * @return
     */
    public LImage getImage(int i) {
        return images[i];
    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static synchronized Images getInstance() {
        if (null != instance) {
            return instance;
        }
        else {
            instance = new Images();
            return instance;
        }
    }

}
