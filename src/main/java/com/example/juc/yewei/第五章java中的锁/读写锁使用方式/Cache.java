package com.example.juc.yewei.第五章java中的锁.读写锁使用方式;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
    static Map<String,Object> map = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock readLock = rwl.readLock();
    static Lock writeLock = rwl.writeLock();
    //获取一个key对应的value
    public static final Object get(String key){
        //读加读锁
        readLock.lock();
        try {
            return map.get(key);
        }finally {
            readLock.unlock();
        }
    }

    public static final void put(String key,Object value){
        //写加写锁
        writeLock.lock();
        try {
            map.put(key,value);
        }finally {
            writeLock.unlock();
        }
    }

    public static final void clear(){
        writeLock.lock();
        try {
            map.clear();
        }finally {
            writeLock.unlock();
        }
    }
}
