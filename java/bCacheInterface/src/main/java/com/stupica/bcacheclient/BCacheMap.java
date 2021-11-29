package com.stupica.bcacheclient;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface BCacheMap extends Remote {

    static String	sUrlRmiNameBCache = "BCacheMap";


    void setMaxSize(String asId, long anCountOfElementsMax) throws RemoteException;

    int ping() throws RemoteException;
    int ping(int aiVal) throws RemoteException;

    boolean add(String asId, String asKey, Object aobjValue, long aiPeriodInMillis) throws RemoteException;
    boolean addNotExist(String asId, String asKey, Object aobjValue, long aiPeriodInMillis) throws RemoteException;

    void remove(String asId, String asKey) throws RemoteException;

    Object get(String asId, String asKey) throws RemoteException;

    void clear(String asId) throws RemoteException;

    long size(String asId) throws RemoteException;
}
