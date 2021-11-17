package com.stupica.bcache;

import com.stupica.ConstGlobal;
import com.stupica.bcacheclient.BCacheMap;
import com.stupica.cache.BCache;
import com.stupica.cache.MemoryBCache;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BCacheMapImpl implements BCacheMap {
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
        objCache = (BCache) mapCache.get(asId);
        if (objCache == null) {
            objCache = new MemoryBCache();
            mapCache.put(asId, objCache);
        }
        return objCache;
    }


    public int ping() {
        return ConstGlobal.RETURN_OK;
    }
    public int ping(int aiVal) {
        return aiVal;
    }


    public void setMaxSize(String asId, long anCountOfElementsMax) {
        BCache objCache = getCache(asId);

        if (objCache != null)
            objCache.setCountOfElementsMax(anCountOfElementsMax);
    }


    public boolean add(String asId, String asKey, Object aobjValue, long aiPeriodInMillis) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return false;
        return objCache.add(asKey, aobjValue, aiPeriodInMillis);
    }

    public boolean addNotExist(String asId, String asKey, Object aobjValue, long aiPeriodInMillis) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return false;
        return objCache.addNotExist(asKey, aobjValue, aiPeriodInMillis);
    }

    public void remove(String asId, String asKey) {
        BCache objCache = getCache(asId);

        if (objCache != null)
            objCache.remove(asKey);
    }

    public Object get(String asId, String asKey) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return null;
        return objCache.get(asKey);
    }

    public void clear(String asId) {
        BCache objCache = getCache(asId);

        if (objCache != null)
            objCache.clear();
    }

    public long size(String asId) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return 0L;
        return objCache.size();
    }
}
