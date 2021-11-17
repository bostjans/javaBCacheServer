package com.stupica.bcacheserver;


import com.stupica.ConstGlobal;
import com.stupica.GlobalVar;
import com.stupica.bcache.BCacheMapImpl;
import com.stupica.bcacheclient.BCacheMap;
import com.stupica.cache.BCache;
import com.stupica.core.UtilCommon;
import com.stupica.core.UtilString;
import com.stupica.mainRunner.MainRunBase;
import jargs.gnu.CmdLineParser;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MainRun extends MainRunBase {

    /**
     * ..
     */
    private long    iTimeElapsedStopLimit = 0;

    private int     iPort = 13111;

    /**
     * Main object instance variable;
     */
    private static MainRun objInstance;

    CmdLineParser.Option obj_op_max = null;
    CmdLineParser.Option obj_op_loopPause = null;
    CmdLineParser.Option obj_op_maxTime = null;
    CmdLineParser.Option obj_op_port = null;

    //private String	        sUrlRmi = "BCacheMap";
    private BCacheMapImpl   objServer = null;
    private BCacheMap       objStub = null;
    private Registry        objRegistry = null;


    /**
     * @param a_args
     */
    public static void main(String[] a_args) {
        // Initialization
        GlobalVar.getInstance().sProgName = "bCacheServer";
        GlobalVar.getInstance().sVersionBuild = "011";

        // Generate main program class
        objInstance = new MainRun();

        iReturnCode = objInstance.invokeApp(a_args);
        System.out.println("main(): ReturnCode: " + iReturnCode);

        // Return
        UtilCommon.sleepFoxMillis(1000 * 1);
        if (iReturnCode != ConstGlobal.PROCESS_EXIT_SUCCESS)
            System.exit(iReturnCode);
    }


    protected void printUsage() {
        super.printUsage();
        System.err.println("            [{-m,--maxLoops} max loops to execute");
        System.err.println("            [{--loopPause} a_time_to_wait_between_loop");
        System.err.println("            [{-t,--maxTime} max time to run - in seconds");
        System.err.println("            [{-p,--port} Port");
    }


    /**
     * Method: initialize
     *
     * ..
     */
    protected void initialize() {
        super.initialize();
        bShouldReadConfig = false;
        bShouldEnableShutdownHook = true;
        // Allow more instances .. as needed for CurrPair retrieval.
        //bLockFileAllowMoreInstance = true;
        bIsProcessInLoop = true;
        iMaxNumOfLoops = 0;
        iPauseBetweenLoop = 1000 * 11;
        iPauseAtStart = 1000;               // Pause before (actual) start processing;
    }


    /**
     * Method: defineArguments
     *
     * ..
     *
     * @return int iResult	1 = AllOK;
     */
    protected int defineArguments() {
        // Local variables
        int         iResult;

        // Initialization
        iResult = super.defineArguments();

        // Create a CmdLineParser, and add to it the appropriate Options.
        obj_op_max = obj_parser.addLongOption('m', "maxLoops");
        obj_op_loopPause = obj_parser.addIntegerOption("loopPause");
        obj_op_maxTime = obj_parser.addLongOption('t', "maxTime");
        obj_op_port = obj_parser.addIntegerOption('p', "port");
        return iResult;
    }

    /**
     * Method: readArguments
     *
     * ..
     *
     * @return int iResult	1 = AllOK;
     */
    protected int readArguments() {
        // Local variables
        int         iResult;

        // Initialization
        iResult = super.readArguments();

        // Check the configuration was read
        //
//        // Check previous step
//        if (iResult == ConstGlobal.RETURN_OK) {
//            if (objInstance.objPropSett == null) {
//                iResult = ConstGlobal.RETURN_ERROR;
//                logger.severe("main(): Error at readConfig() operation!"
//                        + "\n\tPrepare config file from sample .. as this is required!");
//                this.msgWarn("main(): Error at readConfig() operation!"
//                        + "\n\tPrepare config file from sample .. as this is required!");
//            }
//        }
//        // Check previous step
//        if (iResult == ConstGlobal.RETURN_OK) {
//            if (objInstance.objPropSett.isEmpty()) {
//                iResult = ConstGlobal.RETURN_ERROR;
//                logger.severe("main(): Error at readConfig() operation!"
//                        + "\n\tPrepare config file from sample .. as this is required!");
//                this.msgWarn("main(): Error at readConfig() operation!"
//                        + "\n\tPrepare config file from sample .. as this is required!");
//            }
//        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            Long iTemp = (Long)obj_parser.getOptionValue(obj_op_max, 0L);
            iMaxNumOfLoops = iTemp;

            iTemp = (Long)obj_parser.getOptionValue(obj_op_maxTime, 0L);
            iTimeElapsedStopLimit = iTemp * 1000;
            Integer iTempInt = (Integer)obj_parser.getOptionValue(obj_op_loopPause, iPauseBetweenLoop);
            iPauseBetweenLoop = iTempInt.intValue();
            if (iPauseBetweenLoop < 1000 * 4) {
                iPauseBetweenLoop = 1000 * 4;
            }
        }
        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            Integer iTemp = (Integer)obj_parser.getOptionValue(obj_op_port, iPort);
            iPort = iTemp.intValue();
        }
        return iResult;
    }


    /**
     * Method: runBefore
     *
     * Run ..
     *
     * @return int	1 = AllOK;
     */
    protected int runBefore() {
        // Local variables
        int         iResult;
        String      sTemp = null;

        // Initialization
        iResult = super.runBefore();

        // Init ..
        System.setProperty("java.rmi.server.hostname", "localhost");

        // Check ..
        //

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            try {
                objServer = new BCacheMapImpl();
            } catch (RemoteException e) {
                sTemp = "Error at server initialization ..  -  Result: " + iResult + ";"
                        + " Msg.: " + e.getMessage();
                iResult = ConstGlobal.RETURN_NOCONNECTION;
                msgInfo("runBefore(): " + sTemp);
                logger.severe("runBefore(): " + sTemp);
                e.printStackTrace();
            }
        }
        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            try {
                objStub = (BCacheMap) UnicastRemoteObject.exportObject((BCacheMap)objServer, iPort + 1);
                objRegistry = LocateRegistry.createRegistry(iPort);
            } catch (RemoteException e) {
                sTemp = "Error at server initialization ..  -  Result: " + iResult + ";"
                        + " Msg.: " + e.getMessage();
                iResult = ConstGlobal.RETURN_NOCONNECTION;
                msgInfo("runBefore(): " + sTemp);
                logger.severe("runBefore(): " + sTemp);
                e.printStackTrace();
            }
        }
        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            if (objRegistry != null) {
                try {
                    objRegistry.rebind(BCacheMap.sUrlRmiBCacheMap, objStub);
                } catch (RemoteException e) {
                    sTemp = "Error at server initialization - reBind ..  -  Result: " + iResult + ";"
                            + " Msg.: " + e.getMessage();
                    iResult = ConstGlobal.RETURN_NOCONNECTION;
                    msgInfo("runBefore(): " + sTemp);
                    logger.severe("runBefore(): " + sTemp);
                    e.printStackTrace();
                }
            }
        }
        return iResult;
    }

    /**
     * Method: runAfter
     *
     * Run ..
     *
     * @return int	1 = AllOK;
     */
    protected int runAfter() {
        // Local variables
        int         iResult;
        String      sTemp;

        // Initialization
        iResult = super.runAfter();

        if (objRegistry != null)
            try {
                objRegistry.unbind(BCacheMap.sUrlRmiBCacheMap);
            } catch (RemoteException e) {
                iResult = ConstGlobal.RETURN_ERROR;
                sTemp = "Error at server cleanUp - unBind ..  -  Result: " + iResult + ";"
                        + " Msg.: " + e.getMessage();
                msgInfo("runAfter(): " + sTemp);
                logger.severe("runAfter(): " + sTemp);
                e.printStackTrace();
            } catch (NotBoundException e) {
                iResult = ConstGlobal.RETURN_ERROR;
                sTemp = "Error at server cleanUp - unBind ..  -  Result: " + iResult + ";"
                        + " Msg.: " + e.getMessage();
                msgInfo("runAfter(): " + sTemp);
                logger.severe("runAfter(): " + sTemp);
                e.printStackTrace();
            }
        if (objServer != null)
            try {
                UnicastRemoteObject.unexportObject(objServer, true);
            } catch (NoSuchObjectException e) {
                iResult = ConstGlobal.RETURN_ERROR;
                sTemp = "Error at server cleanUp - unExport ..  -  Result: " + iResult + ";"
                        + " Msg.: " + e.getMessage();
                msgInfo("runAfter(): " + sTemp);
                logger.severe("runAfter(): " + sTemp);
                e.printStackTrace();
            }
        return iResult;
    }

    /**
     * Method: runShutdownHook
     *
     * Run/Initiate Shutdown procedure.
     *
     * @return int	1 = AllOK;
     */
    protected int runShutdownHook() {
        // Local variables
        int     iResult;

        // Initialization
        iResult = super.runShutdownHook();
        msgInfo("runShutdownHook(21): Shutdown initiated ..  -  ReturnCode: " + iReturnCode + ";");

        return iResult;
    }

    /**
     * Method: runLoopCycle
     *
     * Run_Loop_cycle ..
     *
     * @return int	1 = AllOK;
     */
    protected int runLoopCycle(RefDataInteger aobjRefCountData) {
        // Local variables
        int         iResult;
        String      sTemp;

        // Initialization
        iResult = super.runLoopCycle(aobjRefCountData);

        // Check ..

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            if (objServer == null) {
                msgWarn("runLoopCycle(): No server running (null).");
            }
            if (objServer.mapCache == null) {
                msgInfo("runLoopCycle(): No data (null).");
            } else {
                sTemp = "Num. of maps: " + objServer.mapCache.size()
                        + "; maps: " + objServer.mapCache.keySet();
                if (objServer.mapCache.keySet().size() > 0) {
                    sTemp += "";
                }
                for (String sLoop : objServer.mapCache.keySet()) {
                    BCache objCache = objServer.mapCache.get(sLoop);
                    sTemp += "\n> ";
                    sTemp += "{" + sLoop + ": " + objCache.toStringShort() + "}";
                }
                msgInfo("runLoopCycle(): " + sTemp);
            }
        }
        return iResult;
    }
}
