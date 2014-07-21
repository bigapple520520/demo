package com.xuan.weixinserver.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.wx.action.ActionInvoker;
import com.xuan.weixinserver.wx.interceptor.Interceptor;

/**
 * 给messagedeal中的带注解的属性注入，Spring的Bean
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-19 下午6:30:03 $
 */
public class AutowireInterceptor implements Interceptor {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 缓存反射出来的属性
    private final ConcurrentMap<Class<?>, List<Field>> class2FieldsMap = new ConcurrentHashMap<Class<?>, List<Field>>();

    @Override
    public void init() {
        System.out.println("init AutowireInterceptor success!");
    }

    @Override
    public void destroy() {
    }

    @Override
    public void intercept(ActionInvoker actionInvoker) throws Exception {
        log.debug("call AutowireInterceptor!");

        long time = System.currentTimeMillis();

        // 这里可以注入Spring的bean
        Class<?> actionClass = actionInvoker.getAction().getClass();

        List<Field> fields = class2FieldsMap.get(actionClass);

        // 第一次遍历类，查找带有Resource注解的属性缓存
        if (fields == null) {
            List<Field> newFields = new ArrayList<Field>();

            Field[] _fields = actionClass.getDeclaredFields();
            if (_fields != null) {
                for (Field f : _fields) {
                    if (isFieldInjectable(f)) {
                        newFields.add(f);
                    }
                }
            }

            if (newFields.isEmpty()) {
                fields = Collections.emptyList();
            }
            else {
                fields = class2FieldsMap.putIfAbsent(actionClass, newFields);
                if (fields == null) {
                    fields = newFields;
                }
            }
        }

        // 注入SpringBean
        if (!fields.isEmpty()) {
            for (Field field : fields) {
                injectBean(actionInvoker, field);
            }

            log.debug("Action[{}] autowire bean elapsed {} ms", actionClass, (System.currentTimeMillis() - time));
        }

        actionInvoker.invoke();// 调用下一个拦截器
    }

    // 判断字段是否需要Spring Bean
    private boolean isFieldInjectable(Field field) {
        return field.isAnnotationPresent(Resource.class);
    }

    // 注入Spring Bean
    private void injectBean(ActionInvoker actionInvoker, Field beanField) {
        Object bean = ServiceLocator.getBean(beanField.getType());
        if (null == bean) {
            log.error("Could not inject bean.cause: bean is null.");
            return;
        }

        String beanName = beanField.getName();
        try {
            beanField.setAccessible(true);
            beanField.set(actionInvoker.getAction(), bean);

            log.debug("Field[{}.{}] inject bean[{}]", new Object[] {
                    actionInvoker.getAction().getClass().getSimpleName(), beanName, bean });
        }
        catch (Exception e) {
            log.debug("Field[{}.{}] inject bean[{}]", new Object[] {
                    actionInvoker.getAction().getClass().getSimpleName(), beanName, bean }, e);
        }
    }

}
