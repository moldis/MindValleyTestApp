package com.artem.mindvalleysample.downloader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artem.datadownloader.DataDownloader;
import com.artem.datadownloader.callbacks.DownloaderCallback;
import com.artem.datadownloader.factory.ExpensiveType;
import com.artem.mindvalleysample.R;
import com.artem.mindvalleysample.downloader.beans.PictureBean;

import java.util.List;

public class SampleRecyclerViewAdapter extends RecyclerView.Adapter<SampleRecyclerViewAdapter.SampleViewHolders> {

    private List<PictureBean> itemList;
    private Context context;

    public SampleRecyclerViewAdapter(Context context, List<PictureBean> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public void setItemList(List<PictureBean> itemList) {
        this.itemList = itemList;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stagged_item_view, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position) {

        DataDownloader downloadManager = new DataDownloader(context, itemList.get(position).getFullPicture(), new DownloaderCallback() {
            @Override
            public void onProgress(int bytesDownloaded, int bytesSum, double progress) {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    holder.progress.setVisibility(View.VISIBLE);
                    holder.txtProgress.setVisibility(View.VISIBLE);
                    holder.txtProgress.setText("%" + progress);
                });
            }

            @Override
            public void onFinished(ExpensiveType type) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    holder.txtError.setVisibility(View.GONE);
                    holder.progress.setVisibility(View.GONE);
                    holder.txtProgress.setVisibility(View.GONE);
                    Bitmap original = type.asBitmap();
                    Bitmap result = Bitmap.createScaledBitmap(original, original.getWidth() / 2, original.getHeight() / 2, false);
                    holder.image.setImageBitmap(result);
                });
            }

            @Override
            public void onError(Exception ex) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    holder.progress.setVisibility(View.GONE);
                    holder.txtProgress.setVisibility(View.GONE);
                    holder.txtError.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onCanceled() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    holder.progress.setVisibility(View.GONE);
                    holder.txtProgress.setVisibility(View.GONE);
                });
            }
        });
        downloadManager.startDownload();
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class SampleViewHolders extends RecyclerView.ViewHolder {
        ImageView image;
        ProgressBar progress;
        TextView txtProgress, txtError;

        public SampleViewHolders(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            progress = itemView.findViewById(R.id.progress);
            txtProgress = itemView.findViewById(R.id.txtProgress);
            txtError = itemView.findViewById(R.id.txtError);
        }
    }
}
