package com.stupica.bcacheserver;

import com.stupica.cache.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


public class BCacheListService extends BCacheServiceBase {

    private final boolean bIsFeatureCacheCopy = true;

    private final long nCacheCopyMaxOld = 1000 * 33;    // 33 sec

    protected AtomicLong nCountCacheCopyHit = new AtomicLong();

    public Map<String, BStoreList> mapCache = null;
    private BCache mapCacheCopy = null;

    private static BCacheListService objInstance;

    private static Logger logger = Logger.getLogger(BCacheListService.class.getName());


    public static BCacheListService getInstance() {
        if (objInstance == null)
            objInstance = new BCacheListService();
        return objInstance;
    }


    public long getCountCacheCopyHit() {
        return nCountCacheCopyHit.get();
    }


    private BStoreList getCache(String asId) {
        BStoreList objCache = null;

        if (mapCache == null) {
            mapCache = new ConcurrentHashMap<>();
            if (bIsFeatureCacheCopy) {
                mapCacheCopy = new MemoryBCache(1000);
            }
        }
        objCache = mapCache.get(asId);
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
        Object      objReturn = null;
        BStoreList  objCache = getCache(asId);

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (objCache == null)
            return null;

        objReturn = objCache.get(aiIndex);
        if (objReturn == null)
            nCountCacheMiss.incrementAndGet();
        else
            nCountCacheHit.incrementAndGet();
        return objReturn;
    }
    private List getListInternal(String asId) {
        List        objReturn = null;
        BStoreList  objCache = getCache(asId);

        if (objCache == null)
            return null;
        objReturn = objCache.getList();
        if (objReturn == null)
            nCountCacheMiss.incrementAndGet();
        else
            nCountCacheHit.incrementAndGet();
        return objReturn;
    }
    public List getList(String asId) {
        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        return getListInternal(asId);
    }
    public List getValueAsList(String asId) {
        List<Object>    arrListCache = null;
        List<Object>    arrList = null;

        nCountCalls.incrementAndGet();
        nCountListCalls.incrementAndGet();
        if (mapCacheCopy != null)
            arrList = (List) mapCacheCopy.get(asId);
        if (arrList != null) {
            nCountCacheCopyHit.incrementAndGet();
        } else {
            arrListCache = getListInternal(asId);
            if (arrListCache != null) {
                arrList = new ArrayList<>();
                synchronized(arrListCache) {
                    for (Iterator<Object> iterator = arrListCache.iterator(); iterator.hasNext();) {
                        CacheObject objInCache = (CacheObject) iterator.next();
                        arrList.add(objInCache.getValue());
                    }
                }
                if (mapCacheCopy != null) {
                    if (!mapCacheCopy.add(asId, arrList, nCacheCopyMaxOld)) {
                        logger.warning("getValueAsList(): add to cacheCopy failed!"
                                + " list: " + asId
                                + "; size(cacheCopy): " + mapCacheCopy.size());
                    }
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


    public void cleanUpCopy() {
        if (mapCacheCopy != null) {
            mapCacheCopy.clear();
        }
    }
}
