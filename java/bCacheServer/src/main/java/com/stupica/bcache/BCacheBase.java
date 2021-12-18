package com.stupica.bcache;

import com.stupica.ConstGlobal;

import java.util.concurrent.atomic.AtomicLong;


public class BCacheBase {

    protected AtomicLong    nCountCalls = new AtomicLong();
    protected AtomicLong    nCountCallPing = new AtomicLong();


    public long getCountCalls() {
        return nCountCalls.get();
    }
    public long getCountPing() {
        return nCountCallPing.get();
    }

    public int ping() {
        nCountCalls.incrementAndGet();
        nCountCallPing.incrementAndGet();
        return ConstGlobal.RETURN_OK;
    }
    public int ping(int aiVal) {
        nCountCalls.incrementAndGet();
        nCountCallPing.incrementAndGet();
        return aiVal;
    }
}
