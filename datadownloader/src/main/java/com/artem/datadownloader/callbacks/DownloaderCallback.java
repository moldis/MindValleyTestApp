package com.artem.datadownloader.callbacks;

import com.artem.datadownloader.factory.ExpensiveType;

public interface DownloaderCallback {
    public void onProgress(int bytesDownloaded,int bytesSum,double progress);
    public void onFinished(ExpensiveType type);
    public void onError(Exception ex);
    public void onCanceled();
}
