package com.artem.datadownloader.core;

import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {

    private static final String TAG = "MemoryCache";

    static Map<String, byte[]> cache;

    private long size = 0;
    private long limit = 1000000; // memory limit for now leave as hardcoded

    public static void init(int capacity){
        cache = Collections.synchronizedMap(
                new LinkedHashMap<String, byte[]>(capacity, 1.5f, true));
        CoreParams.mMemoryCache = new MemoryCache();
    }

    public MemoryCache() {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
    }

    public byte[] get(String id) {
        try {
            if (!cache.containsKey(id))
                return null;
            return cache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, byte[] bytes) {
        try {
            if (cache.containsKey(id)) {
                size -= getSizeInBytes(cache.get(id));
            }
            cache.put(id, bytes);
            size += getSizeInBytes(bytes);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if (size > limit) {
            Iterator<Map.Entry<String, byte[]>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, byte[]> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + cache.size());
        }
    }

    public void clear() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(byte[] arr) {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }
}
