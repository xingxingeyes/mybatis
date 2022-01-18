package com.kai.mybatis.dao;

import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * @description: 完成插件签名：
 * 告诉MyBatis当前插件用来拦截哪个对象的那个方法
 * @author: kai.lv
 * @date: 2022/1/18
 **/
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "parameterize", args = java.sql.Statement.class)
        })
public class MySecondPlugin implements Interceptor {

    /**
     * intercept:拦截
     * 拦截目标对象的目标方法的执行；
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println("MySecondPlugin...intercept:" + invocation.getMethod());
        // 执行目标方法
        Object proceed = invocation.proceed();
        // 返回执行后的返回值
        return proceed;
    }

    /**
     * plugin:
     * 包装目标对象的：包装：为目标对象创建一个代理对象
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {

        System.out.println("MySecondPlugin...plugin:mybatis将要包装的对象" + target);
        // 我们可以借助Plugin的wrap方法来使用当前interceptor包装我们目标对象
        Object wrap = Plugin.wrap(target, this);
        // 返回当前target创建的动态代理
        return wrap;
    }

    /**
     * setProperties:
     * 将插件注册时的property属性设置出来
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的信息：" + properties);
    }
}
