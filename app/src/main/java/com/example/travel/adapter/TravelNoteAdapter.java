package com.example.travel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.travel.R;
import com.example.travel.entity.TravelNoteEntity;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class TravelNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TravelNoteEntity> datas;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemClickListener  onItemClickListener;

    public TravelNoteAdapter(Context context, ArrayList<TravelNoteEntity> arrayList) {
        this.mContext = context;
        this.datas = arrayList;
    }

    public TravelNoteAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_travelrecord_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        TravelNoteEntity travelNoteEntity = datas.get(position);

        //vh.coverImage.setImageBitmap(travelNoteEntity.getCoverImage());
        vh.region.setText(travelNoteEntity.getRegion());
        vh.noteName.setText(travelNoteEntity.getNoteName());
        //vh.portrait.setImageBitmap(travelNoteEntity.getPortrait());
        vh.username.setText(travelNoteEntity.getUsername());
        vh.likeNum.setText(String.valueOf(travelNoteEntity.getLikeNum()));
        vh.position = position;
        /*
        // Picasso: 异步加载
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform())
                .into(vh.imageHeader);
        Picasso.with(mContext)
                .load(videoEntity.getCoverurl())
                .into(vh.thumb);
        vh.position  =position;
         */
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
       return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView coverImage;
        public TextView username;
        public ImageView portrait;
        public TextView region;
        public TextView noteName;
        public TextView likeNum;
        public int position;

        public ViewHolder(@NonNull View view) {
            super(view);
            coverImage = view.findViewById(R.id.travelNote_cover_image);
            username = view.findViewById(R.id.travelNote_userName);
            portrait = view.findViewById(R.id.travelNote_portrait);
            region = view.findViewById(R.id.travelNote_region);
            noteName = view.findViewById(R.id.travelNote_name);
            likeNum = view.findViewById(R.id.travelNote_likeNum);

            if (onItemChildClickListener != null) {
                coverImage.setOnClickListener(this);
            }
            if (onItemClickListener != null) {
                view.setOnClickListener(this);
            }

            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.travelNote_cover_image) {
                if (onItemChildClickListener != null) {
                    onItemChildClickListener.onItemChildClick(position);
                }
            }
            /*
            else {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
             */

        }
    }

    public void setDatas(ArrayList<TravelNoteEntity> videoEntities) {
        this.datas = videoEntities;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}