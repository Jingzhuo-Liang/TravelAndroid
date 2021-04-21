package com.example.travel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.entity.MyTravelRecordEntity;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/19,21:29
 * @@version:1.0
 * @@annotation:
 **/
public class MyTravelRecordAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<MyTravelRecordEntity> datas;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemClickListener  onItemClickListener;


    public MyTravelRecordAdapter(Context context, ArrayList<MyTravelRecordEntity> arrayList) {
        this.mContext = context;
        this.datas = arrayList;
    }

    public MyTravelRecordAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_travelrecord_released_layout,parent,false);
            ViewHolderReleased viewHolder = new ViewHolderReleased(view);
            return  viewHolder;
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_travelrecord_aduiting_layout,parent,false);
            ViewHolderAuditing viewHolder = new ViewHolderAuditing(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //渲染数据
        int type = getItemViewType(position);
        MyTravelRecordEntity ne = datas.get(position);
        if (type == 0) {
            ViewHolderReleased vh = (ViewHolderReleased) holder;
            //Log.e("holder",ne.getTitle());
            vh.recordName.setText(ne.getRecordName());
            vh.releasedTime.setText(ne.getRecordReleasedTime());
            vh.likeNum.setText(String.valueOf(ne.getLikeNum()));
            vh.commitNum.setText(String.valueOf(ne.getCommitNum()));
            vh.browseNum.setText(String.valueOf(ne.getBrowseNum()));
            vh.recordRegion.setText(ne.getRecordRegion());
            vh.releasedTime.setText(ne.getRecordReleasedTime());
            Picasso.with(mContext)
                    .load(ne.getRecordCoverImage())
                    .into(vh.coverImage);
        }
        else if (type == 1) {
            ViewHolderAuditing vh = (ViewHolderAuditing) holder;
            vh.recordName.setText(ne.getRecordName());
            vh.releasedTime.setText(ne.getRecordReleasedTime());
            vh.recordRegion.setText(ne.getRecordRegion());
            vh.releasedTime.setText(ne.getRecordReleasedTime());
            Picasso.with(mContext)
                    .load(ne.getRecordCoverImage())
                    .into(vh.coverImage);


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
        int type = datas.get(position).getRecordState();
        return type;
    }

    public class ViewHolderReleased extends RecyclerView.ViewHolder {
        private ImageView coverImage;
        private TextView recordName;
        private TextView releasedTime;
        private TextView likeNum;
        private TextView commitNum;
        private TextView browseNum;
        private TextView recordRegion;

        public ViewHolderReleased(@NonNull View view) {
            super(view);
            coverImage = view.findViewById(R.id.my_travelNote_cover_image_released);
            recordName = view.findViewById(R.id.my_travelRecordName_released);
            releasedTime = view.findViewById(R.id.my_travelRecord_releasedTime_released);
            likeNum = view.findViewById(R.id.my_travelRecord_likeNum_released);
            commitNum = view.findViewById(R.id.my_travelRecord_commitNum_released);
            browseNum = view.findViewById(R.id.my_travelRecord_BrowseNum_released);
            recordRegion = view.findViewById(R.id.my_travelRecordRegion_released);
        }
    }

    public class ViewHolderAuditing extends RecyclerView.ViewHolder {
        private ImageView coverImage;
        private TextView recordName;
        private TextView releasedTime;
        private TextView recordRegion;

        public ViewHolderAuditing(@NonNull View view) {
            super(view);
            coverImage = view.findViewById(R.id.my_travelNote_cover_image_auditing);
            recordName = view.findViewById(R.id.my_recordName_auditing);
            releasedTime = view.findViewById(R.id.my_record_releasedTime_auditing);
            recordRegion = view.findViewById(R.id.my_travelRecordRegion_auditing);
        }
    }


    public void setDatas(ArrayList<MyTravelRecordEntity> videoEntities) {
        this.datas = videoEntities;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
