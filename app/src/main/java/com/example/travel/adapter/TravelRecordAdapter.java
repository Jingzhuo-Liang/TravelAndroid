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
import com.example.travel.entity.MyTravelRecordEntity;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.listener.OnAdJudgeClickListener;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemModifyClickListener;
import com.example.travel.util.StringUtils;
import com.example.travel.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation: travelRecord and advertisement
 **/
public class TravelRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TravelRecordEntity> datas;
    private OnItemChildClickListener onItemChildClickListener;
    private OnAdJudgeClickListener onAdJudgeClickListener;

    public TravelRecordAdapter(Context context, ArrayList<TravelRecordEntity> arrayList) {
        this.mContext = context;
        this.datas = arrayList;
    }

    public TravelRecordAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) { //record
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_travelrecord_layout,parent,false);
            TravelRecord travelRecord = new TravelRecord(view);
            return travelRecord;
        } else  { //ad
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_advertisement_layout,parent,false);
            Advertisement advertisement = new Advertisement(view);
            return advertisement;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        TravelRecordEntity travelNoteEntity = datas.get(position);
        if (type == 0) { //record
            TravelRecord tr = (TravelRecord) holder;
            tr.region.setText(travelNoteEntity.getRecordRegion());
            tr.noteName.setText(travelNoteEntity.getRecordName());
            tr.username.setText(travelNoteEntity.getAuthorName());
            tr.likeNum.setText(String.valueOf(travelNoteEntity.getLikeNum()));
            tr.position = position;

            // Picasso: 异步加载
            if (!StringUtils.isEmpty(travelNoteEntity.getAuthorPortrait())) {
                Picasso.with(mContext)
                        .load(travelNoteEntity.getAuthorPortrait())
                        .transform(new CircleTransform())
                        .into(tr.portrait);
            }
            if (!StringUtils.isEmpty(travelNoteEntity.getRecordCoverImage())) {
                Picasso.with(mContext)
                        .load(travelNoteEntity.getRecordCoverImage())
                        .into(tr.coverImage);
            }
        } else { //ad
            Advertisement ad = (Advertisement) holder;
            ad.adName.setText(travelNoteEntity.getAdName());
            ad.position = position;

            // Picasso: 异步加载
            if (!StringUtils.isEmpty(travelNoteEntity.getAdCoverImage())) {
                Picasso.with(mContext)
                        .load(travelNoteEntity.getAdCoverImage())
                        .transform(new CircleTransform())
                        .into(ad.adPortrait);
            }
            if (!StringUtils.isEmpty(travelNoteEntity.getAdPortrait())) {
                Picasso.with(mContext)
                        .load(travelNoteEntity.getAdPortrait())
                        .into(ad.adImage);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size();
        }
       return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    public class TravelRecord extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView coverImage;
        public TextView username;
        public ImageView portrait;
        public TextView region;
        public TextView noteName;
        public TextView likeNum;
        public int position;

        public TravelRecord(@NonNull View view) {
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
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.travelNote_cover_image) {
                if (onItemChildClickListener != null) {
                    onItemChildClickListener.onItemChildClick(position);
                }
            }
        }
    }

    public class Advertisement extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView adImage;
        public TextView adName;
        public ImageView adPortrait;
        public TextView adLink;
        public ImageView adDownArrow;
        public int position;

        public Advertisement(@NonNull View view) {
            super(view);
            adImage = view.findViewById(R.id.ad_image);
            adName = view.findViewById(R.id.ad_name);
            adPortrait = view.findViewById(R.id.ad_portrait);
            adLink = view.findViewById(R.id.ad_link);
            adDownArrow = view.findViewById(R.id.ad_down_arrow);

            if (onItemChildClickListener != null) {
                adImage.setOnClickListener(this);
                adLink.setOnClickListener(this);
            }
            if (onAdJudgeClickListener != null) {
                adDownArrow.setOnClickListener(this);
            }
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ad_image || v.getId() == R.id.ad_link) {
                if (onItemChildClickListener != null) {
                    onItemChildClickListener.onItemChildClick(position);
                }
            } else if (v.getId() == R.id.ad_down_arrow) {
                if (onAdJudgeClickListener != null) {
                    onAdJudgeClickListener.onAdJudgeClick(adDownArrow, position);
                }
            }
        }
    }

    public void setDatas(ArrayList<TravelRecordEntity> videoEntities) {
        this.datas = videoEntities;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnAdJudgeClickListener(OnAdJudgeClickListener onAdJudgeClickListener) {
        this.onAdJudgeClickListener = onAdJudgeClickListener;
    }
}
