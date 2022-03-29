package com.stupica.bcache;

import com.stupica.bcacheclient.BCacheList;
import com.stupica.bcacheserver.BCacheListService;

import java.util.*;


public class BCacheListImpl extends BCacheBase implements BCacheList {

    public void setMaxSize(String asId, long anCountOfElementsMax) {
        BCacheListService.getInstance().setMaxSize(asId, anCountOfElementsMax);
    }
    public void setShouldDeleteOldest(String asId, boolean abVal) {
        BCacheListService.getInstance().setShouldDeleteOldest(asId, abVal);
    }


    public boolean add(String asId, Object aobjValue, long aiPeriodInMillis) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return BCacheListService.getInstance().add(asId, aobjValue, aiPeriodInMillis);
    }
    public boolean add2begin(String asId, Object aobjValue, long aiPeriodInMillis) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return BCacheListService.getInstance().add2begin(asId, aobjValue, aiPeriodInMillis);
    }
    public boolean addAll(String asId, Collection aarrElement, long aiPeriodInMillis) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return BCacheListService.getInstance().addAll(asId, aarrElement, aiPeriodInMillis);
    }

    public void remove(String asId, int aiIndex) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        BCacheListService.getInstance().remove(asId, aiIndex);
    }

    public Object get(String asId, int aiIndex) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return BCacheListService.getInstance().get(asId, aiIndex);
    }
//    private List getListInternal(String asId) {
//        BStoreList objCache = getCache(asId);
//
//        if (objCache == null)
//            return null;
//        return objCache.getList();
//    }
    public List getList(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        //return getListInternal(asId);
        return BCacheListService.getInstance().getList(asId);
    }
    public List getValueAsList(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();

        return BCacheListService.getInstance().getValueAsList(asId);
    }

    public void clear(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        BCacheListService.getInstance().clear(asId);
    }

    public long size(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return BCacheListService.getInstance().size(asId);
    }
}
