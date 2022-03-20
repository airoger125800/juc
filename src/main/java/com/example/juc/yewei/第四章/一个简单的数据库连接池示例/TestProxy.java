package com.example.juc.yewei.第四章.一个简单的数据库连接池示例;

import java.lang.reflect.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 测试动态代理
 */
public class TestProxy {
    //需要被代理的实现类
    static class targetObj implements ProxyInterface {

        @Override
        public String A(String a) {
            return a;
        }

        @Override
        public String B(String b) {
            return b+b;
        }
    }

    static class proxyInvokeHandler<T> implements InvocationHandler{
        T targetObj ;
        public proxyInvokeHandler(T t){
            targetObj = t;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("A")) {
                System.out.println("A方法被增强");
                return method.invoke(targetObj, args);
            }
            System.out.println("B方法未被增强");
            return method.invoke(targetObj, args);
        }
    }


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //方法一
//        targetObj targetObj = new targetObj();
//        final Class<?> proxyClass = Proxy.getProxyClass(TestProxy.class.getClassLoader(), ProxyInterface.class);
//        final Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
//        final proxyInterface proxyObj = (proxyInterface)constructor.newInstance(new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                if (method.getName().equals("A")) {
//                    System.out.println("A方法被增强");
//                    return method.invoke(targetObj, args);
//                }
//                System.out.println("B方法未被增强");
//                return method.invoke(targetObj, args);
//            }
//        });

        //方法二
        final ProxyInterface proxyObj = (ProxyInterface)Proxy.newProxyInstance(TestProxy.class.getClassLoader()
                , new Class[]{ProxyInterface.class}
                , new proxyInvokeHandler<targetObj>(new targetObj()));

        System.out.println(proxyObj.A("a"));
        System.out.println(proxyObj.B("b"));
    }
}
