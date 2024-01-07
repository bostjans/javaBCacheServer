package com.stupica.testClient;

import com.stupica.ConstGlobal;
import com.stupica.GlobalVar;
import com.stupica.bcacheclient.BCacheList;
import com.stupica.bcacheclient.BCacheMap;
import com.stupica.bcacheclient.BCacheService;
import com.stupica.core.UtilCommon;

import java.rmi.RemoteException;
import java.util.logging.Logger;


public class MainRun {

    int     iNumberOfElementMax = 1000000;
    String  sMapName01 = "map01";

    BCacheService objBCache = null;

    /**
     * Main object instance variable;
     */
    private static MainRun objInstance;

    private static Logger logger = Logger.getLogger(MainRun.class.getName());


    /**
     * @param a_args
     */
    public static void main(String[] a_args) {
        int         iReturnCode = 0;

        // Initialization
        GlobalVar.getInstance().sProgName = "bCacheTestClient01";
        GlobalVar.getInstance().sVersionBuild = "012";

        // Generate main program class
        objInstance = new MainRun();

        iReturnCode = objInstance.run();
        //iReturnCode = objInstance.invokeApp(a_args);
        //System.out.println("main(): ReturnCode: " + iReturnCode);

        // Return
        UtilCommon.sleepFoxMillis(1000 * 1);
        if (iReturnCode != ConstGlobal.PROCESS_EXIT_SUCCESS)
            System.exit(iReturnCode);
    }


    /**
     * Method: run
     *
     * Run ..
     *
     * @return int	1 = AllOK;
     */
    public int run() {
        // Local variables
        int         iResult;
        BCacheMap   objCache = null;
        BCacheList  objCacheList = null;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        //iResult = super.run();

        objBCache = new BCacheService();
        //objBCache.sHost = "lukna.golem.si";
        objBCache.sPort = "13111";
        //iResult = objBCache.connect();
        objCache = objBCache.getCache();
        if (objCache == null) {
            logger.severe("run(): Error at getting Cache reference - map!");
            iResult = ConstGlobal.RETURN_ERROR;
        }
        UtilCommon.sleepFoxMillis(1000 * 1);

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            objCacheList = objBCache.getCacheList();
            if (objCacheList == null) {
                logger.severe("run(): Error at getting Cache reference - list!");
                iResult = ConstGlobal.RETURN_ERROR;
            }
            UtilCommon.sleepFoxMillis(1000 * 1);
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            add11(objCache, 1);
        }
        if (iResult == ConstGlobal.RETURN_OK) {
            add11(objCache, iNumberOfElementMax);
        }
        return iResult;
    }

    public void add11(BCacheMap aobjCache, int aiNumberOfElementMax) {
        // Local variables
        int         iResult;
        boolean     bResult = false;
        long        dtStart, dtStartLoop;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        System.out.println("--");
        System.out.println("Test: testAdd11() - " + this.getClass().getName());

        dtStart = System.currentTimeMillis();
        dtStartLoop = dtStart;
        System.out.println(".. before add > TS: " + Long.valueOf(dtStart).toString() + "");
        try {
            iResult = aobjCache.ping();
            if (iResult != ConstGlobal.RETURN_OK) {
                System.out.println(".. something wrong! > TS: " + Long.valueOf(dtStart).toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            try {
                aobjCache.setMaxSize(sMapName01,aiNumberOfElementMax + 1);
                aobjCache.clear(sMapName01);
                for (long i = 1L; i < aiNumberOfElementMax + 1; i++) {
                    bResult = aobjCache.add(sMapName01, Long.valueOf(i).toString(), "String" + Long.valueOf(i).toString(), 1000 * 60 * 2);
                    if (!bResult) {
                        System.out.println("Fail to add! .. after add: " + i + " > size: " + aobjCache.size(sMapName01));
                        break;
                    }
                    if ((i % 50000) == 0) {
                        System.out.println(".. after add: " + i + " > size: " + aobjCache.size(sMapName01)
                                + "; elapse(ms): " + (System.currentTimeMillis() - dtStartLoop));
                        dtStartLoop = System.currentTimeMillis();
                    }
                }
                System.out.println("TS: " + Long.valueOf(System.currentTimeMillis()).toString() + ".. after add > size: " + aobjCache.size(sMapName01)
                                + "\n\telapse(ms): " + (System.currentTimeMillis() - dtStart)
                        //        + "\n\tCache: " + aobjCache.toString()
                );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
