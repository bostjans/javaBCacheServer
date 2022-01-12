package com.stupica.bcacheserver;

import java.util.concurrent.atomic.AtomicLong;


public class BCacheServiceBase {

    protected AtomicLong    nCountCalls = new AtomicLong();
    protected AtomicLong    nCountMapCalls = new AtomicLong();
    protected AtomicLong    nCountListCalls = new AtomicLong();
    protected AtomicLong    nCountCallPing = new AtomicLong();

    protected AtomicLong    nCountCacheHit = new AtomicLong();
    protected AtomicLong    nCountCacheMiss = new AtomicLong();

    public long getCountCalls() {
        return nCountCalls.get();
    }
    public long getCountMapCalls() {
        return nCountMapCalls.get();
    }
    public long getCountListCalls() {
        return nCountListCalls.get();
    }
    public long getCountPing() {
        return nCountCallPing.get();
    }

    public long getCountCacheHit() {
        return nCountCacheHit.get();
    }
    public long getCountCacheMiss() {
        return nCountCacheMiss.get();
    }
}
