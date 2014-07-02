/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuan.zxingutils.lib.camera;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.IBinder;
import android.util.Log;

/**
 * 利用反射来控制闪光灯
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-2 上午9:39:05 $
 */
public final class FlashlightManager {
    private static final String TAG = FlashlightManager.class.getSimpleName();

    private static final Object iHardwareService;
    private static final Method setFlashEnabledMethod;
    static {
        iHardwareService = getHardwareService();
        setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
        if (null != iHardwareService) {
            Log.v(TAG, "This device does supports control of a flashlight");
        }
        else {
            Log.v(TAG, "This device does not support control of a flashlight");
        }
    }

    private FlashlightManager() {
    }

    private static Object getHardwareService() {
        Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
        if (null == serviceManagerClass) {
            return null;
        }

        Method getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", String.class);
        if (null == getServiceMethod) {
            return null;
        }

        Object hardwareService = invoke(getServiceMethod, null, "hardware");
        if (null == hardwareService) {
            return null;
        }

        Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
        if (null == iHardwareServiceStubClass) {
            return null;
        }

        Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", IBinder.class);
        if (null == asInterfaceMethod) {
            return null;
        }

        return invoke(asInterfaceMethod, null, hardwareService);
    }

    private static Method getSetFlashEnabledMethod(Object iHardwareService) {
        if (null == iHardwareService) {
            return null;
        }
        Class<?> proxyClass = iHardwareService.getClass();
        return maybeGetMethod(proxyClass, "setFlashlightEnabled", boolean.class);
    }

    // -------------------------------------试图反射获取------------------------------------------
    private static Class<?> maybeForName(String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException cnfe) {
            // OK
            return null;
        }
        catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while finding class " + name, re);
            return null;
        }
    }

    private static Method maybeGetMethod(Class<?> clazz, String name, Class<?>... argClasses) {
        try {
            return clazz.getMethod(name, argClasses);
        }
        catch (NoSuchMethodException nsme) {
            // OK
            return null;
        }
        catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while finding method " + name, re);
            return null;
        }
    }

    private static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        }
        catch (IllegalAccessException e) {
            Log.w(TAG, "Unexpected error while invoking " + method, e);
            return null;
        }
        catch (InvocationTargetException e) {
            Log.w(TAG, "Unexpected error while invoking " + method, e.getCause());
            return null;
        }
        catch (RuntimeException re) {
            Log.w(TAG, "Unexpected error while invoking " + method, re);
            return null;
        }
    }

    // ----------------------------------------设置闪光-----------------------------------------
    /**
     * 启用闪光灯
     */
    public static void enableFlashlight() {
        setFlashlight(true);
    }

    /**
     * 关闭闪光灯
     */
    public static void disableFlashlight() {
        setFlashlight(false);
    }

    private static void setFlashlight(boolean active) {
        if (null != iHardwareService) {
            invoke(setFlashEnabledMethod, iHardwareService, active);
        }
    }

}
