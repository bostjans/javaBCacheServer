package com.stupica.bcacheclient;

import com.stupica.ConstGlobal;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class BCacheService {

    private BCacheMap   objCache = null;


    public BCacheMap getCache() {
        // Local variables
        int         iResult;

        if (objCache != null)
            return objCache;
        iResult = connect();
        return objCache;
    }

    public int connect() {
        // Local variables
        int         iResult;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;

        try {
            objCache = (BCacheMap) Naming.lookup("//localhost:13211/" + BCacheMap.sUrlRmiBCacheMap);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (objCache == null) {
            iResult = ConstGlobal.RETURN_ERROR;
        }
        return iResult;
    }
}
