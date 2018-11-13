package com.artem.mindvalleysample;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.artem.datadownloader.DataDownloader;
import com.artem.datadownloader.callbacks.DownloaderCallback;
import com.artem.datadownloader.factory.ExpensiveType;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.artem.mindvalleysample", appContext.getPackageName());
    }

    @Test
    public void testDownloadSingleImageSuccess(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        String tmpUrl = "https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=1080\\u0026fit=max\\u0026s=1881cd689e10e5dca28839e68678f432";

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        DataDownloader downloader = new DataDownloader(appContext, tmpUrl, new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {
                System.out.println(progress);
            }

            @Override
            public void onFinished(ExpensiveType type) {
                assertNotNull("Result should not be null", type);
                assertTrue("Result should be instance of JustAResultReal", type.asRawData().length>0);
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception ex) {
                fail("Exception " + ex.toString());
            }

            @Override
            public void onCanceled() {
                fail("onCanceled ");
            }
        });
        downloader.startDownload();

        try {
            if(!countDownLatch.await(100000, TimeUnit.MILLISECONDS)){
                fail("Timed out, see log for errors");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownloadAnyDataSuccess(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        String tmpUrl = "http://pastebin.com/raw/wgkJgazE";

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        DataDownloader downloader = new DataDownloader(appContext, tmpUrl, new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {
                System.out.println(progress);
            }

            @Override
            public void onFinished(ExpensiveType type) {
                assertNotNull("Result should not be null", type);
                assertTrue("Result should be instance of JustAResultReal", type.asRawData().length>0);
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception ex) {
                fail("Exception " + ex.toString());
            }

            @Override
            public void onCanceled() {
                fail("onCanceled ");
            }
        });
        downloader.startDownload();

        try {
            if(!countDownLatch.await(100000, TimeUnit.MILLISECONDS)){
                fail("Timed out, see log for errors");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private DataDownloader downloader;

    @Test
    public void testManuallyCancelDownloadSuccess(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        String tmpUrl = "https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=1080\\u0026fit=max\\u0026s=1881cd689e10e5dca28839e68678f432";

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        downloader = new DataDownloader(appContext, tmpUrl, new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {
                if(progress > 50D || progress < 60D){
                    downloader.cancelDownload();
                }
            }

            @Override
            public void onFinished(ExpensiveType type) {
            }

            @Override
            public void onError(Exception ex) {
            }

            @Override
            public void onCanceled() {
                countDownLatch.countDown();
            }
        });
        downloader.startDownload();

        try {
            if(!countDownLatch.await(100000, TimeUnit.MILLISECONDS)){
                fail("Timed out, see log for errors");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleDownloadsOneCanceled(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        String tmpUrl1 = "https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=1080\\u0026fit=max\\u0026s=1881cd689e10e5dca28839e68678f432";
        String tmpUrl2 = "https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026s=4b142941bfd18159e2e4d166abcd0705";

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        downloader = new DataDownloader(appContext, tmpUrl1, new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {
                if(progress > 50D || progress < 60D){
                    downloader.cancelDownload();
                }
            }

            @Override
            public void onFinished(ExpensiveType type) {
            }

            @Override
            public void onError(Exception ex) {
            }

            @Override
            public void onCanceled() {
                System.out.println("onCanceled");
            }
        });
        downloader.startDownload();

        DataDownloader downloader2 = new DataDownloader(appContext, tmpUrl2, new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {

            }

            @Override
            public void onFinished(ExpensiveType type) {
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception ex) {

            }

            @Override
            public void onCanceled() {

            }
        });
        downloader2.startDownload();

        try {
            if(!countDownLatch.await(100000, TimeUnit.MILLISECONDS)){
                fail("Timed out, see log for errors");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
