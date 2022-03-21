package com.example.juc.yewei.第四章java并发编程基础.一个简单的数据库连接池示例;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    static ConnectionPool connectionPool = new ConnectionPool(10);
    // 保证所有ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);
    // main线程将会等待所有ConnectionRunner结束后才能继续执行
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 100;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 0; i < threadCount ; i++){
            Thread thread = new Thread(new ConnectionRunner(count,got,notGot),"ConnectionRunnerThread");
            thread.start();
        }
        start.countDown();
        end.await();
        System.out.println("total invoke: "+(threadCount * count));
        System.out.println("got connection: "+got);
        System.out.println("not got connection: "+notGot);
    }

    static class ConnectionRunner implements Runnable{
        int count;
        AtomicInteger got;
        AtomicInteger notGot;
        public ConnectionRunner(int count,AtomicInteger got,AtomicInteger notGot){
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (count >0 ){
                try {
                    Connection connection = connectionPool.fetchConnection(1000);
                    if (connection != null){
                        try {
                            connection.createStatement();
                            connection.commit();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }finally {
                            connectionPool.releaseConnection(connection);
                            got.incrementAndGet();
                        }
                    }else {
                        notGot.getAndIncrement();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    count --;
                }
            }
            end.countDown();
        }
    }
}
