package com.example.juc.yewei.第四章java并发编程基础.一个简单的数据库连接池示例;

import java.lang.reflect.*;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class ConnectionDriver {
    static class ConnectionHandler implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("commit")){
                TimeUnit.MILLISECONDS.sleep(100);
            }
            return null;
        }
    }

    //connection是接口，交由各数据库的驱动去实现的，这里就不做实现，简单用动态代理返回一个代理对象
    public static final Connection createConnection() {
        return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionHandler());
    }

}
