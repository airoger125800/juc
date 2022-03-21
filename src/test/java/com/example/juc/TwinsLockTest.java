package com.example.juc;

import com.example.juc.yewei.第五章java中的锁.自定义同步组件TwinsLock.TwinsLock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


@SpringBootTest
public class TwinsLockTest {
    @Test
    public void test(){
        final Lock lock = new TwinsLock();

        class Worker implements Runnable{

            @Override
            public void run() {
                lock.lock();
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }

        //启动10个线程
        for(int i = 0;i<10;i++){
            Worker w = new Worker();
            Thread thread = new Thread(w);
            thread.setDaemon(true);
            thread.start();
        }

        //每隔一秒换行
        for (int i = 0; i<10;i++){
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
