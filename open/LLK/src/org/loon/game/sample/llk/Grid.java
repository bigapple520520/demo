package org.loon.game.sample.llk;

import org.loon.framework.android.game.action.sprite.Animation;
import org.loon.framework.android.game.action.sprite.Picture;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;

/**
 * 显示图片的组件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-24 下午8:08:58 $
 */
public class Grid extends Picture {
    private static final long serialVersionUID = 8691783836025759797L;

    private Animation animation;
    private int type;
    private int xpos;
    private int ypos;

    public Grid(LImage img) {
        super(img);
    }

    public Grid(int x, int y) {
        super(x, y);
        xpos = x;
        ypos = y;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public boolean isPassable() {
        return !isVisible();
    }

    @Override
    public void createUI(LGraphics g) {
        super.createUI(g);
        if (null == animation) {
            return;
        }

        if (type == 0 || type == 2) {
            LImage img = animation.getSpriteImage().getImage();
            if (null != img) {
                g.drawImage(img, x() + (getWidth() - img.getWidth()) / 2, y() + (getHeight() - img.getHeight()) / 2);
            }
        }
    }

    @Override
    public void update(long t) {
        super.update(t);
        if (animation != null) {
            animation.update(t);
        }
    }

    public void setBorder(int type) {
        this.type = type;
        switch (type) {
        case 0:
            animation = Animation.getDefaultAnimation("res/s.png", 3, 48, 48, 100);
            break;
        case 2:
            animation = Animation.getDefaultAnimation("res/s1.png", 48, 48, 100);
            break;
        default:
            break;
        }
    }

}
