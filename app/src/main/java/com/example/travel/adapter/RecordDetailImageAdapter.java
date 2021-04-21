package com.example.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageUtil;
import com.donkingliang.imageselector.utils.UriUtils;
import com.donkingliang.imageselector.utils.VersionUtils;
import com.example.travel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/20,21:09
 * @@version:1.0
 * @@annotation:
 **/
public class RecordDetailImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> mImages = new ArrayList<>();
    private LayoutInflater mInflater;
    private boolean isAndroidQ = VersionUtils.isAndroidQ();

    public RecordDetailImageAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, final int position) {
        final String image = mImages.get(position);
        Picasso.with(mContext)
                .load(image)
                .into(holder.ivImage);

        /*
        // 是否是剪切返回的图片
        boolean isCutImage = ImageUtil.isCutImage(mContext, image);
        if (isAndroidQ && !isCutImage) {
            Glide.with(mContext).load(UriUtils.getImageContentUri(mContext, image)).into(holder.ivImage);
        } else {
            Glide.with(mContext).load(image).into(holder.ivImage);
        }
         */

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
            ivImage = itemView.findViewById(R.id.record_detail_image);
        }
    }

}
