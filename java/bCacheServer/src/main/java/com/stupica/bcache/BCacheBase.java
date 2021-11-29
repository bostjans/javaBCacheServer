package com.stupica.bcache;

import com.stupica.ConstGlobal;


public class BCacheBase {

    public int ping() {
        return ConstGlobal.RETURN_OK;
    }
    public int ping(int aiVal) {
        return aiVal;
    }
}
