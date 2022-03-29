package com.stupica.testClient;

import com.stupica.ConstGlobal;
import com.stupica.GlobalVar;
import com.stupica.bcacheclient.BCacheList;
import com.stupica.bcacheclient.BCacheMap;
import com.stupica.bcacheclient.BCacheService;
import com.stupica.core.UtilCommon;

import java.util.List;
import java.util.logging.Logger;


public class MainRun {

    int     aiNumberOfElementMax = 1000000;
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
        GlobalVar.getInstance().sProgName = "bCacheDumper";
        GlobalVar.getInstance().sVersionBuild = "011";

        // Generate main program class
        objInstance = new MainRun();

        iReturnCode = objInstance.run();

        // Return
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
        objBCache.sPort = "13111";

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
            dumpList(objCacheList);
        }
        return iResult;
    }

    public int dumpList(BCacheList aobjCache) {
        // Local variables
        int         iResult;
        List        arrList = null;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        System.out.println("--");
        //System.out.println("Test: testAdd11() - " + this.getClass().getName());

        try {
            arrList = aobjCache.getValueAsList("eurusd");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (arrList == null)
            System.out.println(".. no data (null)!");
        else {
            System.out.println(".. size: " + arrList.size());
            if (arrList.size() > 0) {
                boolean bIsFirst = true;
                int     iCount = 0;

                System.out.println("Data: ");
                for (Object objLoop : arrList) {
                    if (bIsFirst) {
                        //if (objLoop instanceof CurrencyTicker)
                        try {
                            if (Class.forName("com.stupica.lenkoTr.model.CurrencyTicker").isInstance(objLoop))
                                System.out.println(".. data is of proper Type.");
                            else
                                System.err.println(".. data is NOT of proper Type!");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        bIsFirst = false;
                    }
                    com.stupica.lenkoTr.model.CurrencyTicker objCurrTicker = (com.stupica.lenkoTr.model.CurrencyTicker) objLoop;
                    //System.out.println(iCount++ + ": " + objCurrTicker.toStringShort());
                    System.out.println(iCount++ + ": " + objCurrTicker.toStringShortFixed());
                }
            }
        }
        return iResult;
    }
}
