package com.artem.mindvalleysample.downloader;

import android.app.Application;
import android.content.Context;

import com.artem.datadownloader.core.MemoryCache;

public class AppObj extends Application {

    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

         // init memory cache with max capacity
        MemoryCache.init(10);
    }

    public static Context getContext() {
        return mContext;
    }
}
