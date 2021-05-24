package com.example.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donkingliang.imageselector.utils.ImageUtil;
import com.donkingliang.imageselector.utils.UriUtils;
import com.donkingliang.imageselector.utils.VersionUtils;
import com.example.travel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<String> mImages = new ArrayList<>();
    private LayoutInflater mInflater;
    private boolean isAndroidQ = VersionUtils.isAndroidQ();

    public ImageAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String image = mImages.get(position);
        // 是否是剪切返回的图片
        boolean isCutImage = ImageUtil.isCutImage(mContext, image);
        if (isAndroidQ && !isCutImage) {
            if (image.contains("http:")) {
                Picasso.with(mContext)
                        .load(image)
                        .into(holder.ivImage);
            } else {
                Glide.with(mContext)
                        .load(UriUtils.getImageContentUri(mContext, image))
                        .skipMemoryCache(true)                      //禁止Glide内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)  //不缓存资源
                        .into(holder.ivImage);
            }
        } else {
            if (image.contains("http:")) {
                Picasso.with(mContext)
                        .load(image)
                        .into(holder.ivImage);
            } else {
                Glide.with(mContext)
                        .load(image)
                        .skipMemoryCache(true)                      //禁止Glide内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)  //不缓存资源
                        .into(holder.ivImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public void refresh(ArrayList<String> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }

}
