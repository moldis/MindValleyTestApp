package com.artem.datadownloader.core;

import com.artem.datadownloader.core.CoreParams;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > CoreParams.MAX_CACHE_SIZE;
    }
}
