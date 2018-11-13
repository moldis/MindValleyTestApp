package com.artem.datadownloader;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.artem.datadownloader.callbacks.DownloaderCallback;
import com.artem.datadownloader.core.CoreParams;
import com.artem.datadownloader.factory.ExpensiveType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

public class DataDownloader {

    Context mContext;
    Uri mUri;
    DownloaderCallback mDownloaderCallback;
    Future mFutureService;

    public DataDownloader(Context context, String url, DownloaderCallback downloaderCallback) {
        mContext = context;
        mUri = Uri.parse(url);
        mDownloaderCallback = downloaderCallback;
    }

    public DataDownloader(Context context, Uri url, DownloaderCallback downloaderCallback) {
        mContext = context;
        mUri = url;
        mDownloaderCallback = downloaderCallback;
    }

    public void setDownloaderCallback(DownloaderCallback mDownloaderCallback) {
        this.mDownloaderCallback = mDownloaderCallback;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public Uri getUri() {
        return mUri;
    }

    public synchronized void startDownload() throws IllegalArgumentException {
        if (mContext == null
                || mUri == null) {
            throw new IllegalArgumentException("Context or Url are null");
        }

        // if by some reason developer call twice
        if (mFutureService != null) {
            mFutureService.cancel(true);
        }

        mFutureService = CoreParams.mExecutorService.submit(new DownloadRunnable());
    }

    class DownloadRunnable implements Runnable {

        @Override
        public void run() {

            // trying to get from cache first
            if (CoreParams.mMemoryCache != null) {
                Log.d("debug", "Cache not initialized, avoiding");
                if (CoreParams.mMemoryCache.get(mUri.toString()) != null) {
                    if (mDownloaderCallback != null) {
                        mDownloaderCallback.onFinished(new ExpensiveType(CoreParams.mMemoryCache.get(mUri.toString())));
                    }
                    return;
                }
            }

            // then continue with download
            InputStream input = null;
            ByteArrayOutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(mUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (mDownloaderCallback != null) {
                        mDownloaderCallback.onError(new IOException("Wrong response code"));
                    }
                    return;
                }

                // download the file
                input = connection.getInputStream();

                output = new ByteArrayOutputStream();

                byte data[] = new byte[4096];
                int total = 0;
                int contentLen = connection.getContentLength();

                int count = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);

                    if (mDownloaderCallback != null) {
                        mDownloaderCallback.onProgress(total, contentLen, (double) (total * 100) / contentLen);
                    }
                }

                // put in cache
                if (CoreParams.mMemoryCache != null) {
                    Log.d("debug", "Cache not initialized, avoiding");
                    CoreParams.mMemoryCache.put(mUri.toString(), output.toByteArray());
                }

                if (mDownloaderCallback != null) {
                    mDownloaderCallback.onFinished(new ExpensiveType(output.toByteArray()));
                }

            } catch (Exception e) {

                if (mDownloaderCallback != null) {
                    mDownloaderCallback.onError(e);
                }

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    public void cancelDownload() {
        mFutureService.cancel(true);
        if (mDownloaderCallback != null) {
            mDownloaderCallback.onCanceled();
        }
    }


}
