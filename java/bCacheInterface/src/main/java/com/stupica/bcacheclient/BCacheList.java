package com.stupica.bcacheclient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;


public interface BCacheList extends Remote {

    static String	sUrlRmiNameBCache = "BCacheList";


    void setMaxSize(String asId, long anCountOfElementsMax) throws RemoteException;
    void setShouldDeleteOldest(String asId, boolean abVal) throws RemoteException;

    int ping() throws RemoteException;
    int ping(int aiVal) throws RemoteException;

    boolean add(String asId, Object aobjValue, long aiPeriodInMillis) throws RemoteException;
    boolean add2begin(String asId, Object aobjValue, long aiPeriodInMillis) throws RemoteException;
    boolean addAll(String asId, Collection aarrElement, long aiPeriodInMillis) throws RemoteException;

    void remove(String asId, int aiIndex) throws RemoteException;

    Object get(String asId, int aiIndex) throws RemoteException;
    //List getList(String asId) throws RemoteException;
    List getValueAsList(String asId) throws RemoteException;

    void clear(String asId) throws RemoteException;

    long size(String asId) throws RemoteException;
}
