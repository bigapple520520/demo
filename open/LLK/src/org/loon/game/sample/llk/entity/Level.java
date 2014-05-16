/* 
 * @(#)Level.java    Created on 2014-5-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package org.loon.game.sample.llk.entity;

/**
 * 不同等级方块数
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-12 下午4:15:59 $
 */
public class Level {
    private int xBound;
    private int yBound;

    public Level() {
        xBound = 8;
        yBound = 6;
    }

    public Level(int x, int y) {
        xBound = x;
        yBound = y;
    }

    public int getXBound() {
        return xBound;
    }

    public int getYBound() {
        return yBound;
    }

}
