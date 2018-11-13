package com.artem.datadownloader.core;

import com.artem.datadownloader.factory.BackgroundThreadFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CoreParams {

    // threading for download tasks
    static int DEFAULT_THREAD_POOL_SIZE = 10;
    static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    static final int KEEP_ALIVE_TIME = 4;
    static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    public static ExecutorService mExecutorService = new ThreadPoolExecutor(CoreParams.NUMBER_OF_CORES,
            CoreParams.NUMBER_OF_CORES*2,
            CoreParams.KEEP_ALIVE_TIME,
            CoreParams.KEEP_ALIVE_TIME_UNIT,
            taskQueue,
            new BackgroundThreadFactory());

    // memory cache
    public static int MAX_CACHE_SIZE = 5;
    public static MemoryCache mMemoryCache;

}
