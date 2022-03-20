package com.example.juc.yewei.第四章.一个简单的数据库连接池示例;

//import org.springframework.util.Assert;

import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {

    //用于存放连接的容器
    private final LinkedList<Connection> pool = new LinkedList<>();

    //构造方法
    public ConnectionPool(int initialSize){
//        Assert.isTrue(initialSize >0,"数据库连接初始数不能小于0");
        for(int i= 0 ;i<= initialSize;i++){
            pool.addLast(ConnectionDriver.createConnection());
        }
    }

    public void releaseConnection(Connection connection){
        if (connection != null){
            synchronized (pool){
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    //在mills内无法获取则返回null
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            // 完全超时
            if (mills <= 0){
                while (pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else {
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining >0){
                    pool.wait();
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if(!pool.isEmpty()){
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }

}
