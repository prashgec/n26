package com.n26.cache;

import com.n26.constants.Constant;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class LocalCache<K, V> {
    private final Logger LOGGER = LoggerFactory.getLogger(LocalCache.class);
    private static Map cacheMap;
    private static boolean isCalcRequired= false;

    private static Map<String, DoubleSummaryStatistics> statisticMap = new HashMap();

    /**
     * Object to store in cache
     */
    protected class CacheObject {
        public V value;

        protected CacheObject(V value) {
            this.value = value;
        }
    }

    public LocalCache() {
        cacheMap = new ConcurrentHashMap();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(isCalcRequired)
                    {calculateStatistics();}
                    long now = System.currentTimeMillis();
                   //  LOGGER.info("Current State "+cacheMap);
                    List<K> expiredKeys;
                    for(Object keys: cacheMap.keySet())
                    {
                        if(((Long)keys)<=now)
                        {
                           
                            cacheMap.remove(keys);
                            isCalcRequired=true;
                        }
                    }

                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void cleanUp(long key) {

        cacheMap.remove(key);


    }

    public DoubleSummaryStatistics getStatistics() {
        if (statisticMap.get(Constant.STATISTICS) == null)
            return new DoubleSummaryStatistics();
        return statisticMap.get(Constant.STATISTICS);

    }

    public void put(K key, V value) {

        cacheMap.put(key, value);
        LOGGER.info("adding key " + key);
        isCalcRequired= true;
       // calculateStatistics();

    }

    public V get(K key) {
        if (cacheMap.get(key) == null)
            return null;
        return (V) cacheMap.get(key);
    }

    public void remove(K key) {

        cacheMap.remove(key);

    }

    public int size() {

        return cacheMap.size();

    }



    public void calculateStatistics() {
        List<Transaction> lst = new ArrayList<>();
        cacheMap.values().forEach(item -> {
            lst.addAll((List<Transaction>) item);
        });
        statisticMap.put(Constant.STATISTICS, lst.stream().mapToDouble(txn -> txn.getAmount()).summaryStatistics());// bad way to handle this scenario, need to separate this logic
    }

}
