package com.stupica.test.hz;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CacheTest01 {

    //private long aiPeriodInMillis;
    private final long      aiNumberOfElementMax = 10000000;
    private HazelcastInstance client = null;


    @BeforeEach
    public void setUp() throws Exception {
        ClientConfig clientConfig = null;

        client = HazelcastClient.newHazelcastClient();
        if (client != null) {
            MapConfig mCfg = new MapConfig("testCache01");
            mCfg.setTimeToLiveSeconds(1 * 60 * 10);
            client.getConfig().addMapConfig(mCfg);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * https://ria101.wordpress.com/2011/12/12/concurrenthashmap-avoid-a-common-misuse/
     * https://www.fatalerrors.org/a/0N581Do.html
     */
    @DisplayName("Add large number of Objects - #11")
    @Test
    public void add11() {
        boolean             bResult = false;
        long                dtStart, dtStartLoop;
        String              objReturn;
        Map<String, String> objMap = null;

        // Initialization
        System.out.println("--");
        System.out.println("Test: testAdd11() - " + this.getClass().getName());

        objMap = client.getMap("testCache01");
        assertNotNull(objMap);

        dtStart = System.currentTimeMillis();
        dtStartLoop = dtStart;
        System.out.println(".. before add > TS: " + Long.valueOf(dtStart).toString() + "");
        for (long i = 1L; i < aiNumberOfElementMax + 1; i++) {
            objReturn = objMap.put(Long.valueOf(i).toString(), "String" + Long.valueOf(i).toString());

            if ((i % 10000) == 0) {
                System.out.println(".. after add: " + i + " > size: " + objMap.size()
                        + "; elapse(ms): " + (System.currentTimeMillis() - dtStartLoop));
                dtStartLoop = System.currentTimeMillis();
            }
        }
        System.out.println("TS: " + Long.valueOf(System.currentTimeMillis()).toString() + ".. after add > size: " + objMap.size()
                + "\n\telapse(ms): " + (System.currentTimeMillis() - dtStart)
                //+ "\n\tCache: " + objMap.toString()
        );
        //assertEquals(0, objCache.size());
    }
}
