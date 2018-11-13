package com.artem.datadownloader.factory;

import java.util.concurrent.ThreadFactory;

import static java.lang.Thread.MAX_PRIORITY;

public class BackgroundThreadFactory implements ThreadFactory {
    private static int sTag = 1;

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("CustomThread" + sTag);
        thread.setPriority(MAX_PRIORITY);

        // A exception handler is created to log the exception from threads
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

            }
        });
        return thread;
    }
}
