package com.stupica.bcache;

import com.stupica.bcacheclient.BCacheMap;
import com.stupica.cache.BCache;
import com.stupica.cache.MemoryBCache;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class BCacheMapImpl extends BCacheBase implements BCacheMap {
//public class BCacheMapImpl extends UnicastRemoteObject implements BCacheMap {

    public Map<String, BCache> mapCache = null;


    public BCacheMapImpl() throws RemoteException {
        super();
    }
//    public BCacheMapImpl(int port) throws RemoteException {
//        super(port);
//    }


    private BCache getCache(String asId) {
        BCache objCache = null;

        if (mapCache == null)
            mapCache = new ConcurrentHashMap<>();
        objCache = mapCache.get(asId);
        if (objCache == null) {
            objCache = new MemoryBCache();
            mapCache.put(asId, objCache);
        }
        return objCache;
    }


    public void setMaxSize(String asId, long anCountOfElementsMax) {
        BCache objCache = getCache(asId);

        if (objCache != null)
            objCache.setCountOfElementsMax(anCountOfElementsMax);
    }


    public <T> boolean add(String asId, T atKey, Object aobjValue, long aiPeriodInMillis) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache == null)
            return false;
        return objCache.add(atKey, aobjValue, aiPeriodInMillis);
    }

    public <T> boolean addNotExist(String asId, T atKey, Object aobjValue, long aiPeriodInMillis) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache == null)
            return false;
        return objCache.addNotExist(atKey, aobjValue, aiPeriodInMillis);
    }

    public <T> void remove(String asId, T atKey) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache != null)
            objCache.remove(atKey);
    }

    public <T> Object get(String asId, T atKey) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache == null)
            return null;
        return objCache.get(atKey);
    }
    public Object[] getKeyList(String asId) {
        Set objKeys = null;
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache != null)
            objKeys = objCache.getKeyAll();
        if (objKeys != null)
            if (objKeys.size() > 0)
                return objKeys.toArray();
        return null;
    }
    public Map getMap(String asId) {
        Map objMap = null;
        Set objKeys = null;
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache != null)
            objKeys = objCache.getKeyAll();
        if (objKeys != null) {
            objMap = new HashMap();
            for (Object objLoop : objKeys) {
                objMap.put(objLoop, objCache.get(objLoop));
            }
        }
        return objMap;
    }

    public void clear(String asId) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache != null)
            objCache.clear();
    }

    public long size(String asId) {
        BCache objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountMapCalls.incrementAndGet();
        if (objCache == null)
            return 0L;
        return objCache.size();
    }
}
