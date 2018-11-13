package com.artem.mindvalleysample.downloader.view;

import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.widget.Toast;

import com.artem.datadownloader.DataDownloader;
import com.artem.datadownloader.callbacks.DownloaderCallback;
import com.artem.datadownloader.factory.ExpensiveType;
import com.artem.mindvalleysample.R;
import com.artem.mindvalleysample.downloader.adapters.SampleRecyclerViewAdapter;
import com.artem.mindvalleysample.downloader.beans.PictureBean;
import com.artem.mindvalleysample.downloader.components.ItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloaderCallback {

    private SwipeRefreshLayout swipeRefresh;
    private SampleRecyclerViewAdapter adapter;
    private RecyclerView mRecycler;
    private int columnCount = 2;
    private StaggeredGridLayoutManager layoutManager;
    private static final String MAIN_URL = "http://pastebin.com/raw/wgkJgazE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipeRefresh);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);

        swipeRefresh.setOnRefreshListener(() -> refreshData());

        adapter = new SampleRecyclerViewAdapter(MainActivity.this, new ArrayList<>());
        mRecycler.setAdapter(adapter);

        refreshData();
    }

    private void refreshData() {
        swipeRefresh.setRefreshing(true);
        DataDownloader downloader = new DataDownloader(this, MAIN_URL, this);
        downloader.startDownload();
    }

    @Override
    public void onProgress(int bytesDownloaded, int bytesSum, double progress) {
        // swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onFinished(ExpensiveType type) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            swipeRefresh.setRefreshing(false);
            List<PictureBean> data = new ArrayList<>();

            try {
                JSONArray jsonArray = type.asJSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String full = jsonObject.getJSONObject("urls").getString("regular");

                        PictureBean pictureBean = new PictureBean(id, full);
                        data.add(pictureBean);
                    } catch (Exception ex) { // json exception
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

            adapter.setItemList(data);
            adapter.notifyDataSetChanged();

        });
    }

    @Override
    public void onError(Exception ex) {
        swipeRefresh.setRefreshing(false);
        Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceled() {
        swipeRefresh.setRefreshing(false);
    }
}
