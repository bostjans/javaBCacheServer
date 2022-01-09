package com.stupica.bcache;

import com.stupica.bcacheclient.BCacheList;
import com.stupica.cache.BStoreList;
import com.stupica.cache.CacheObject;
import com.stupica.cache.MemoryBList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class BCacheListImpl extends BCacheBase implements BCacheList {

    private final boolean bIsFeatureCacheCopy = true;

    private final long nCacheCopyMaxOld = 1000 * 33;    // 33 sec
    private long nTsLastCleanUpCacheCopy = 0L;

    public Map<String, BStoreList> mapCache = null;
    private Map<String, List> mapCacheCopy = null;
    private volatile Map<String, Long> mapCacheLastRefresh = null;


    //public BCacheListImpl() throws RemoteException {
    //    super();
    //}

    private BStoreList getCache(String asId) {
        BStoreList objCache = null;

        if (mapCache == null) {
            mapCache = new ConcurrentHashMap<>();
            if (bIsFeatureCacheCopy) {
                mapCacheCopy = new ConcurrentHashMap<>();
                mapCacheLastRefresh = new ConcurrentHashMap<>();
            }
        }
        objCache = (BStoreList) mapCache.get(asId);
        if (objCache == null) {
            objCache = new MemoryBList();
            mapCache.put(asId, objCache);
        }
        return objCache;
    }


    public void setMaxSize(String asId, long anCountOfElementsMax) {
        BStoreList objCache = getCache(asId);

        if (objCache != null)
            objCache.setCountOfElementsMax(anCountOfElementsMax);
    }
    public void setShouldDeleteOldest(String asId, boolean abVal) {
        BStoreList objCache = getCache(asId);

        if (objCache != null)
            objCache.setShouldDeleteOldest(abVal);
    }


    public boolean add(String asId, Object aobjValue, long aiPeriodInMillis) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return false;
        return objCache.add(aobjValue, aiPeriodInMillis);
    }
    public boolean add2begin(String asId, Object aobjValue, long aiPeriodInMillis) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return false;
        return objCache.add2begin(aobjValue, aiPeriodInMillis);
    }
    public boolean addAll(String asId, Collection aarrElement, long aiPeriodInMillis) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return false;
        return objCache.addAll(aarrElement, aiPeriodInMillis);
    }

    public void remove(String asId, int aiIndex) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache != null)
            objCache.remove(aiIndex);
    }

    public Object get(String asId, int aiIndex) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return null;
        return objCache.get(aiIndex);
    }
    private List getListInternal(String asId) {
        BStoreList objCache = getCache(asId);

        if (objCache == null)
            return null;
        return objCache.getList();
    }
    public List getList(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return getListInternal(asId);
    }
    public List getValueAsList(String asId) {
        boolean         bShouldRebuild = false;
        Long            nTsLastCacheCopyBuild;
        List<Object>    arrListCache = null;
        List<Object>    arrList = null;
        //BStoreList      objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (mapCacheLastRefresh != null) {
            nTsLastCacheCopyBuild = mapCacheLastRefresh.get(asId);
            if (nTsLastCacheCopyBuild == null)
                bShouldRebuild = true;
            else {
                if ((nTsLastCacheCopyBuild + nCacheCopyMaxOld) < System.currentTimeMillis())
                    bShouldRebuild = true;
                else
                    arrList = mapCacheCopy.get(asId);
            }
        }
        if ((bShouldRebuild) || (arrList == null)) {
            arrListCache = getListInternal(asId);
            if (arrListCache != null) {
                arrList = new ArrayList<>();
                synchronized(arrListCache) {
                    //for (Object objLoop : objCache.getList()) {
                    for (Iterator<Object> iterator = arrListCache.iterator(); iterator.hasNext();) {
                        //CacheObject objInCache = (CacheObject) objLoop;
                        CacheObject objInCache = (CacheObject) iterator.next();
                        arrList.add(objInCache.getValue());
                    }
                }
                if (mapCacheCopy != null) {
                    mapCacheCopy.put(asId, arrList);
                    mapCacheLastRefresh.put(asId, Long.valueOf(System.currentTimeMillis()));
                }
            }
        }
        return arrList;
    }

    public void clear(String asId) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache != null)
            objCache.clear();
    }

    public long size(String asId) {
        BStoreList objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return 0L;
        return objCache.size();
    }
}
