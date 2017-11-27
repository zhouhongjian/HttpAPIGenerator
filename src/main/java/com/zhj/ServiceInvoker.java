package com.zhj;

import com.alibaba.fastjson.JSON;
import com.fasterxml.classmate.GenericType;
import com.zhj.config.GeneratorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhj on 2017/11/27.
 */
@Service
public class ServiceInvoker implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    @Resource
    private GeneratorConfig config;

    private final Map<String, Class> classCacheMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public InvokeResult<String> invoke(String className, String methodName, String serviceParamsStr) {

        String finalClassName;
        String packageHead = config.getPackageHead();
        finalClassName = buildFinalClassName(className, packageHead);
        logger.info("className:{} methodName:{} serviceParamsStr:{} packageHead:{} finalClassName:{}",
                className, methodName, serviceParamsStr, finalClassName);


        try {
            //获取Class
            if (!classCacheMap.containsKey(finalClassName)) {
                synchronized (classCacheMap) {
                    if (!classCacheMap.containsKey(finalClassName)) {
                        classCacheMap.put(finalClassName, Class.forName(finalClassName));
                    }
                }
            }
            Class clazz = classCacheMap.get(finalClassName);

            //根据Class反射调用bean的接口方法
            Object bean = applicationContext.getBean(clazz);
            for (Method method : clazz.getMethods()) {
                //接口方法一般都不是重载方法，同时入参为零或者一个
                if (methodName.equals(method.getName())) {
                    Type[] types = method.getGenericParameterTypes();
                    Object result;
                    long startTime = System.currentTimeMillis();
                    if (types.length == 0) {
                        result = method.invoke(bean, (Object[]) null);
                    } else if (types.length == 1) {
                        Type argType = types[0];
                        Class argClazz = Class.forName(argType.getTypeName());
                        Object arg = JSON.parseObject(serviceParamsStr, argClazz);
                        result = method.invoke(bean, arg);
                    } else {
                        //参数不符合一般rpc调用规范
                        result = null;
                    }
                    //添加统计逻辑
                    logger.info("{}.{}接口调用耗时为:{}秒", className, method, (System.currentTimeMillis() - startTime) / 1000);
                    return InvokeResult.<String>create().success(JSON.toJSONString(result));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return InvokeResult.create();
    }

    /**
     * 1、packageHead非空，直接用packageHead+className，否则
     * 2、validatedPackageHeads非空，直接校验className前缀(校验成功直接用className)，否则
     * 3、直接用className
     */
    private String buildFinalClassName(String className, String packageHead) {

        String finalClassName;
        if (!StringUtils.isEmpty(packageHead)) {
            finalClassName = config.getPackageHead() + className;
        } else {
            if (!CollectionUtils.isEmpty(config.getValidatedPackageHeads())) {
                boolean containPrefix = false;
                for (String validate : config.getValidatedPackageHeads()) {
                    if (className.startsWith(validate)) {
                        containPrefix = true;
                        break;
                    }
                }
                if (containPrefix) {
                    finalClassName = packageHead;
                } else {
                    //调用失败
                    finalClassName = "";
                }
            } else {
                finalClassName = packageHead;
            }
        }
        return finalClassName;
    }
}
