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
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemDeleteListener;
import com.example.travel.listener.OnItemModifyClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/19,21:29
 * @@version:1.0
 * @@annotation:
 **/
public class MyTravelRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private boolean isUser;

    private ArrayList<MyTravelRecordEntity> datas;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemDeleteListener onItemDeleteListener;
    private OnItemModifyClickListener onItemModifyClickListener;

    public MyTravelRecordAdapter(Context context, ArrayList<MyTravelRecordEntity> arrayList) {
        this.mContext = context;
        this.datas = arrayList;
    }

    public MyTravelRecordAdapter(Context context, boolean isUser) {
        this.mContext = context;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_travelrecord_released_layout,parent,false);
            ViewHolderReleased viewHolder = new ViewHolderReleased(view, isUser);
            return  viewHolder;
        }
        else if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_travelrecord_aduiting_layout,parent,false);
            ViewHolderAuditing viewHolder = new ViewHolderAuditing(view, isUser);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_treavelrecord_rejected_layout,parent,false);
            ViewHolderRejected viewHolder = new ViewHolderRejected(view, isUser);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //????????????
        int type = getItemViewType(position);
        MyTravelRecordEntity ne = datas.get(position);
        if (type == 0) { //????????????
            ViewHolderReleased vh = (ViewHolderReleased) holder;
            //Log.e("holder",ne.getTitle());
            vh.recordName.setText(ne.getRecordName());
            vh.releasedTime.setText(ne.getRecordReleasedTime().split(" ")[0]);
            vh.likeNum.setText(String.valueOf(ne.getLikeNum()));
            vh.commitNum.setText(String.valueOf(ne.getCommitNum()));
            vh.browseNum.setText(String.valueOf(ne.getBrowseNum()));
            vh.recordRegion.setText(ne.getRecordRegion());
            vh.recordLimit.setText(ne.getRecordLimit() == 0 ? "????????????":
                                    ne.getRecordLimit() == 1 ? "???????????????": "???????????????");
            //vh.releasedTime.setText(ne.getRecordReleasedTime());
            Picasso.with(mContext)
                    .load(ne.getRecordCoverImage())
                    .into(vh.coverImage);
            vh.position = position;
        } else if (type == 1) { //?????????
            ViewHolderAuditing vh = (ViewHolderAuditing) holder;
            vh.recordName.setText(ne.getRecordName());
            vh.releasedTime.setText(ne.getRecordReleasedTime().split(" ")[0]);
            vh.recordRegion.setText(ne.getRecordRegion());
            vh.recordLimit.setText(ne.getRecordLimit() == 0 ? "????????????":
                    ne.getRecordLimit() == 1 ? "???????????????": "???????????????");
            //vh.releasedTime.setText(ne.getRecordReleasedTime());
            Picasso.with(mContext)
                    .load(ne.getRecordCoverImage())
                    .into(vh.coverImage);

            vh.position = position;
        } else if (type == 2) { //??????
            ViewHolderRejected vh = (ViewHolderRejected) holder;
            vh.recordName.setText(ne.getRecordName());
            vh.releasedTime.setText(ne.getRecordReleasedTime().split(" ")[0]);
            vh.recordRegion.setText(ne.getRecordRegion());
            vh.recordLimit.setText(ne.getRecordLimit() == 0 ? "????????????":
                    ne.getRecordLimit() == 1 ? "???????????????": "???????????????");
            //vh.releasedTime.setText(ne.getRecordReleasedTime());
            Picasso.with(mContext)
                    .load(ne.getRecordCoverImage())
                    .into(vh.coverImage);

            vh.position = position;
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
        return datas.get(position).getRecordState();
    }


    public class ViewHolderReleased extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView coverImage;
        private TextView recordName;
        private TextView releasedTime;
        private TextView likeNum;
        private TextView commitNum;
        private TextView browseNum;
        private TextView recordRegion;
        private TextView recordLimit;
        private ImageView deleteBtn;
        private int position;

        public ViewHolderReleased(@NonNull View view, boolean isUser) {
            super(view);
            coverImage = view.findViewById(R.id.my_travelNote_cover_image_released);
            recordName = view.findViewById(R.id.my_travelRecordName_released);
            releasedTime = view.findViewById(R.id.my_travelRecord_releasedTime_released);
            likeNum = view.findViewById(R.id.my_travelRecord_likeNum_released);
            commitNum = view.findViewById(R.id.my_travelRecord_commitNum_released);
            browseNum = view.findViewById(R.id.my_travelRecord_BrowseNum_released);
            recordRegion = view.findViewById(R.id.my_travelRecordRegion_released);
            recordLimit = view.findViewById(R.id.my_travelRecord_limit_released);
            deleteBtn = view.findViewById(R.id.my_travel_record_released_delete);
            if (isUser) {
                if (onItemDeleteListener != null) {
                    deleteBtn.setOnClickListener(this);
                }
            } else {
                deleteBtn.setVisibility(View.INVISIBLE);
            }
            if (onItemChildClickListener != null) {
                coverImage.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.my_travel_record_released_delete: {
                    if (onItemDeleteListener != null) {
                        onItemDeleteListener.onItemDeleteListener(position);
                    }
                    break;
                }
                case R.id.my_travelNote_cover_image_released: {
                    if (onItemChildClickListener != null) {
                        onItemChildClickListener.onItemChildClick(position);
                    }
                    break;
                }
                default:{

                }
            }
        }
    }

    public class ViewHolderAuditing extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView coverImage;
        private TextView recordName;
        private TextView releasedTime;
        private TextView recordRegion;
        private TextView recordLimit;
        private ImageView deleteBtn;
        private ImageView modifyBtn;
        public int position;

        public ViewHolderAuditing(@NonNull View view, boolean isUser) {
            super(view);
            coverImage = view.findViewById(R.id.my_travelNote_cover_image_auditing);
            recordName = view.findViewById(R.id.my_recordName_auditing);
            releasedTime = view.findViewById(R.id.my_record_releasedTime_auditing);
            recordRegion = view.findViewById(R.id.my_travelRecordRegion_auditing);
            recordLimit = view.findViewById(R.id.my_travelRecord_limit_auditing);
            deleteBtn = view.findViewById(R.id.my_travel_record_auditing_delete);
            modifyBtn = view.findViewById(R.id.my_travel_record_auditing_modify);
            if (isUser) {
                if (onItemDeleteListener != null) {
                    deleteBtn.setOnClickListener(this);
                }
            } else {
                deleteBtn.setVisibility(View.INVISIBLE);
            }
            if (onItemChildClickListener != null) {
                coverImage.setOnClickListener(this);
            }
            if (onItemModifyClickListener != null) {
                modifyBtn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.my_travel_record_auditing_delete: {
                    if (onItemDeleteListener != null) {
                        onItemDeleteListener.onItemDeleteListener(position);
                    }
                    break;
                }
                case R.id.my_travelNote_cover_image_auditing: {
                    if (onItemChildClickListener != null) {
                        onItemChildClickListener.onItemChildClick(position);
                    }
                    break;
                }
                case R.id.my_travel_record_auditing_modify: {
                    if (onItemModifyClickListener != null) {
                        onItemModifyClickListener.onItemModifyListener(position);
                    }
                    break;
                }
                default:{

                }
            }
        }
    }

    public class ViewHolderRejected extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView coverImage;
        private TextView recordName;
        private TextView releasedTime;
        private TextView recordRegion;
        private TextView recordLimit;
        private ImageView deleteBtn;
        private ImageView modifyBtn;
        public int position;

        public ViewHolderRejected(@NonNull View view, boolean isUser) {
            super(view);
            coverImage = view.findViewById(R.id.my_travelNote_cover_image_rejected);
            recordName = view.findViewById(R.id.my_recordName_rejected);
            releasedTime = view.findViewById(R.id.my_record_releasedTime_rejected);
            recordRegion = view.findViewById(R.id.my_travelRecordRegion_rejected);
            recordLimit = view.findViewById(R.id.my_travelRecord_limit_rejected);
            deleteBtn = view.findViewById(R.id.my_travel_record_rejected_delete);
            modifyBtn = view.findViewById(R.id.my_travel_record_rejected_modify);
            if (isUser) {
                if (onItemDeleteListener != null) {
                    deleteBtn.setOnClickListener(this);
                }
            } else {
                deleteBtn.setVisibility(View.INVISIBLE);
            }
            if (onItemChildClickListener != null) {
                coverImage.setOnClickListener(this);
            }
            if (onItemModifyClickListener != null) {
                modifyBtn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.my_travel_record_rejected_delete: {
                    if (onItemDeleteListener != null) {
                        onItemDeleteListener.onItemDeleteListener(position);
                    }
                    break;
                }
                case R.id.my_travelNote_cover_image_rejected: {
                    if (onItemChildClickListener != null) {
                        onItemChildClickListener.onItemChildClick(position);
                    }
                    break;
                }
                case R.id.my_travel_record_rejected_modify: {
                    if (onItemModifyClickListener != null) {
                        onItemModifyClickListener.onItemModifyListener(position);
                    }
                    break;
                }
                default:{

                }
            }
        }
    }


    public void setDatas(ArrayList<MyTravelRecordEntity> videoEntities) {
        this.datas = videoEntities;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnItemModifyClickListener(OnItemModifyClickListener onItemModifyClickListener) {
        this.onItemModifyClickListener = onItemModifyClickListener;
    }
}
