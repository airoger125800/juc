package com.example.juc.yewei.第8章java中的并发工具类.控制线程并发数的Semaphore;

import java.util.concurrent.Semaphore;

/**
 * semaphore本质上就是一个共享锁
 */
public class SemaphoreTest {
    static  Semaphore s = new Semaphore(10);
    public static void main(String[] args) {
        for (int i = 0;i < 30;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("save data");
                    s.release();
                }
            }).start();
        }
    }
}
