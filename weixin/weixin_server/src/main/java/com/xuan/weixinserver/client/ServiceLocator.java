/*
 * @(#)ServiceLocator.java    Created on 2014-6-19
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * 获取Spring中Bean实例的接口
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-19 下午5:10:05 $
 */
public class ServiceLocator implements BeanFactoryAware {
    private static BeanFactory beanFactory = null;

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        ServiceLocator.beanFactory = factory;
    }

    /**
     * 获取bean
     *
     * @param name
     * @return
     */
    public static Object getBean(Class<?> clazz) {
        return beanFactory.getBean(clazz);
    }

}
