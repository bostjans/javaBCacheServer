package com.stupica.bcacheclient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public interface BCacheMap extends Remote {

    static String	sUrlRmiNameBCache = "BCacheMap";


    void setMaxSize(String asId, long anCountOfElementsMax) throws RemoteException;

    int ping() throws RemoteException;
    int ping(int aiVal) throws RemoteException;

    <T> boolean add(String asId, T atKey, Object aobjValue, long aiPeriodInMillis) throws RemoteException;
    <T> boolean addNotExist(String asId, T atKey, Object aobjValue, long aiPeriodInMillis) throws RemoteException;

    <T> void remove(String asId, T atKey) throws RemoteException;

    <T> Object get(String asId, T atKey) throws RemoteException;
    Object[] getKeyList(String asId) throws RemoteException;
    Map getMap(String asId) throws RemoteException;

    void clear(String asId) throws RemoteException;

    long size(String asId) throws RemoteException;
}
