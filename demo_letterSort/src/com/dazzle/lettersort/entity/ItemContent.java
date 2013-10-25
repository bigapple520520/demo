/* 
 * @(#)LetterItemContent.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.lettersort.entity;

/**
 * 列表项目的内容
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午3:33:31 $
 */
public class ItemContent extends BaseItem {
    private String name;// 内容项显示的名字
    private Object object;// 内容的其他值对象

    public ItemContent(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
