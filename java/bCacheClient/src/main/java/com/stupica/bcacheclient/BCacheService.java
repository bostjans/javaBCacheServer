package com.stupica.bcacheclient;

import com.stupica.ConstGlobal;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Logger;


public class BCacheService {

    public String       sHost = "localhost";
    public String       sPort = "13211";

    private BCacheMap   objCache = null;
    private BCacheList  objCacheList = null;

    private static Logger logger = Logger.getLogger(BCacheService.class.getName());


    public BCacheMap getCache() {
        // Local variables
        int         iResult;

        if (objCache != null) {
            try {
                if (objCache.ping() == ConstGlobal.RETURN_OK)
                    return objCache;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
            try {
                if (objCacheList.ping() == ConstGlobal.RETURN_OK)
                    return objCacheList;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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
    } /*
    public int connect() {
        // Local variables
        int         iResult;

        // Initialization

        iResult = connectService(BCacheMap.sUrlRmiNameBCache);

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            iResult = connectService(BCacheMap.sUrlRmiNameBCache);
        }
        return iResult;
    } */

    public int disconnect() {
        objCache = null;
        return ConstGlobal.RETURN_OK;
    }
}
