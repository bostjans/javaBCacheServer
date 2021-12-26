package com.stupica.bcacheclient;

import com.stupica.ConstGlobal;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Logger;


public class BCacheService {

    public static final int      nPERIOD_01min_InMILLIS = 1000 * 60 * 01;
    public static final int      nPERIOD_05min_InMILLIS = 1000 * 60 * 05;
    public static final int      nPERIOD_10min_InMILLIS = 1000 * 60 * 10;
    public static final int      nPERIOD_20min_InMILLIS = 1000 * 60 * 20;
    public static final int      nPERIOD_30min_InMILLIS = 1000 * 60 * 30;
    public static final int      nPERIOD_01hour_InMILLIS = 1000 * 60 * 60;
    public static final int      nPERIOD_02hour_InMILLIS = 1000 * 60 * 60 * 2;
    public static final int      nPERIOD_03hour_InMILLIS = 1000 * 60 * 60 * 3;
    public static final int      nPERIOD_06hour_InMILLIS = 1000 * 60 * 60 * 6;
    public static final int      nPERIOD_01day_InMILLIS = 1000 * 60 * 60 * 24;
    public static final int      nPERIOD_01week_InMILLIS = 1000 * 60 * 60 * 24 * 7;

    public String       sHost = "localhost";
    public String       sPort = "13111";

    private long        nCountConnect = 0L;
    private long        nCountPingCall = 0L;
    private long        nTsPingLast = 0L;
    private final long  nTsPingDelta = 1000 * 7L;

    private BCacheMap   objCache = null;
    private BCacheList  objCacheList = null;

    private static Logger logger = Logger.getLogger(BCacheService.class.getName());


    public long getCountConnect() {
        return nCountConnect;
    }
    public long getCountPingCall() {
        return nCountPingCall;
    }

    private int pingCall(BCacheMap aobjCache, BCacheList aobjCacheList) {
        // Local variables
        int         iResult = ConstGlobal.RETURN_OK;

        if (System.currentTimeMillis() - nTsPingLast > nTsPingDelta) {
            nTsPingLast = System.currentTimeMillis();
            try {
                nCountPingCall++;
                if (aobjCache != null)
                    iResult = aobjCache.ping();
                if (aobjCacheList != null)
                    iResult = aobjCacheList.ping();
            } catch (RemoteException e) {
                if (aobjCache != null)
                    logger.severe("pingCall(): service(map/list): BCacheMap; Msg.: " + e.getMessage());
                if (aobjCache != null)
                    logger.severe("pingCall(): service(map/list): BCacheList; Msg.: " + e.getMessage());
                //if (GlobalVar.bIsModeVerbose)
                e.printStackTrace();
            }
        }
        return iResult;
    }


    public BCacheMap getCache() {
        // Local variables
        int         iResult;

        if (objCache != null) {
            if (pingCall(objCache, null) == ConstGlobal.RETURN_OK)
                return objCache;
        }
        iResult = connect(BCacheMap.sUrlRmiNameBCache);
        if (iResult != ConstGlobal.RETURN_OK) {
            objCache = null;
        }
        return objCache;
    }
    public BCacheList getCacheList() {
        // Local variables
        int         iResult;

        if (objCacheList != null) {
            if (pingCall(null, objCacheList) == ConstGlobal.RETURN_OK)
                return objCacheList;
        }
        iResult = connect(BCacheList.sUrlRmiNameBCache);
        if (iResult != ConstGlobal.RETURN_OK) {
            objCacheList = null;
        }
        return objCacheList;
    }

    private int connectService(String asService) {
        // Local variables
        int         iResult;
        String      sURI;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        sURI = "//" + sHost + ":" + sPort + "/";

        nCountConnect++;
        try {
            if (asService.contentEquals(BCacheMap.sUrlRmiNameBCache))
                objCache = (BCacheMap) Naming.lookup(sURI + asService);
            else if (asService.contentEquals(BCacheList.sUrlRmiNameBCache))
                objCacheList = (BCacheList) Naming.lookup(sURI + asService);
            else
                logger.severe("connectService(): service: " + asService + "; Msg.: /");
        } catch (NotBoundException e) {
            logger.severe("connect(): URI: " + sURI
                    + "; service: " + asService
                    + "; Msg.: " + e.getMessage());
            e.printStackTrace();
        } catch (ConnectException e) {
            iResult = ConstGlobal.RETURN_NOCONNECTION;
            logger.severe("connect(): URI: " + sURI
                    + "; service: " + asService
                    + "; Msg.: " + e.getMessage());
        } catch (MalformedURLException e) {
            logger.severe("connect(): URI: " + sURI
                    + "; service: " + asService
                    + "; Msg.: " + e.getMessage());
        } catch (RemoteException e) {
            logger.severe("connect(): URI: " + sURI
                    + "; service: " + asService
                    + "; Msg.: " + e.getMessage());
            e.printStackTrace();
        }
        if (objCache == null) {
            if (iResult == ConstGlobal.RETURN_OK)
                iResult = ConstGlobal.RETURN_ERROR;
        }
        return iResult;
    }
    public int connect(String asService) {
        return connectService(asService);
    }

    public int disconnect() {
        objCache = null;
        return ConstGlobal.RETURN_OK;
    }
}
