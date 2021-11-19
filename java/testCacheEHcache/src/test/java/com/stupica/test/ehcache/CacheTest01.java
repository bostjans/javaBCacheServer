package com.stupica.test.ehcache;



import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CacheTest01 {

    //private long aiPeriodInMillis;
    private final long      aiNumberOfElementMax = 10000000;
    private CacheManager    cacheManager = null;


    @BeforeEach
    public void setUp() throws Exception {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                        ResourcePoolsBuilder.heap(100))
                                .build())
                .build(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        cacheManager.close();
    }

    /**
     * https://www.baeldung.com/ehcache
     */
    @DisplayName("Ehcache: Add large number of Objects - #11")
    @Test
    public void add11() {
        long                dtStart, dtStartLoop;
        Cache<String, String> objCache;

        // Initialization
        System.out.println("--");
        System.out.println("Test: testAdd11() - " + this.getClass().getName());

        //objCache = cacheManager.getCache("preConfigured", String.class, String.class);
        //assertNotNull(objCache);

        objCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());
        assertNotNull(objCache);

        dtStart = System.currentTimeMillis();
        dtStartLoop = dtStart;
        System.out.println(".. before add > TS: " + Long.valueOf(dtStart).toString() + "");
        for (long i = 1L; i < aiNumberOfElementMax + 1; i++) {
            objCache.put(Long.valueOf(i).toString(), "String" + Long.valueOf(i).toString());

            if ((i % 10000) == 0) {
                System.out.println(".. after add: " + i + " > size: ?"
                        + "; elapse(ms): " + (System.currentTimeMillis() - dtStartLoop));
                dtStartLoop = System.currentTimeMillis();
            }
        }
        System.out.println("TS: " + Long.valueOf(System.currentTimeMillis()).toString() + ".. after add > size: ?"
                + "\n\telapse(ms): " + (System.currentTimeMillis() - dtStart)
                //+ "\n\tCache: " + objMap.toString()
        );
        //assertEquals(0, objCache.size());
    }
}
