package com.stupica.bcache;

import com.stupica.bcacheclient.BCacheMap;
import com.stupica.cache.BCache;
import com.stupica.cache.MemoryBCache;

import java.rmi.RemoteException;
import java.util.Map;
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

        if (objCache == null)
            return false;
        return objCache.add(atKey, aobjValue, aiPeriodInMillis);
    }

    public <T> boolean addNotExist(String asId, T atKey, Object aobjValue, long aiPeriodInMillis) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return false;
        return objCache.addNotExist(atKey, aobjValue, aiPeriodInMillis);
    }

    public <T> void remove(String asId, T atKey) {
        BCache objCache = getCache(asId);

        if (objCache != null)
            objCache.remove(atKey);
    }

    public <T> Object get(String asId, T atKey) {
        BCache objCache = getCache(asId);

        if (objCache == null)
            return null;
        return objCache.get(atKey);
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
