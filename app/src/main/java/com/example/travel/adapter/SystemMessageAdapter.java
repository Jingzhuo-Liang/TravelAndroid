package com.example.travel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.activity.OtherUserInfoActivity;
import com.example.travel.entity.MyTravelRecordEntity;
import com.example.travel.entity.SystemMessageEntity;
import com.example.travel.listener.OnAdJudgeClickListener;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemDeleteListener;
import com.example.travel.listener.OnItemLongClickListener;
import com.example.travel.listener.OnItemModifyClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/5/14,17:16
 * @@version:1.0
 * @@annotation:
 **/
public class SystemMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private ArrayList<SystemMessageEntity> datas;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemLongClickListener onLongClickListener;
    private Bitmap unReadBitmap;

    public SystemMessageAdapter(Context context, ArrayList<SystemMessageEntity> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    public SystemMessageAdapter(Context context, Bitmap unReadBitmap) {
        this.mContext = context;
        this.unReadBitmap = unReadBitmap;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_layout,parent,false);
        ViewHolderMessage viewHolderMessage = new ViewHolderMessage(view);
        return viewHolderMessage;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SystemMessageEntity smEntity = datas.get(position);
        ViewHolderMessage vh = (ViewHolderMessage) holder;
        vh.messagerName.setText(smEntity.getMessagerName());
        vh.messageTime.setText(smEntity.getMessageTime());
        vh.messageMain.setText(smEntity.getMessageMain());
        vh.position = position;
        if (smEntity.getMessageState() == 0) { //未读
            vh.messageState.setImageBitmap(unReadBitmap);
        } else {
            vh.messageState.setVisibility(View.INVISIBLE);
            //vh.messageState.setImageBitmap(haveReadBitmap);
        }
        Picasso.with(mContext).
                load(smEntity.getMessagerPortrait()).
                into(vh.messagerPortrait);
    }

    public class ViewHolderMessage extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView messagerPortrait;
        private TextView messagerName;
        private ImageView messageState; //0 未读 1已读
        private TextView messageMain;
        private TextView messageTime;
        private int position;

        public ViewHolderMessage(@NonNull View itemView) {
            super(itemView);
            messagerPortrait = itemView.findViewById(R.id.messenger_portrait);
            messagerName = itemView.findViewById(R.id.messenger_center_messenger_userName);
            messageState = itemView.findViewById(R.id.messenger_center_message_state);
            messageMain = itemView.findViewById(R.id.message_center_message);
            messageTime = itemView.findViewById(R.id.message_center_message_time);
            if (onItemChildClickListener != null) {
                messageMain.setOnClickListener(this);
            }
            if (onLongClickListener != null) {
                messageMain.setOnLongClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.message_center_message : {
                    if (onItemChildClickListener != null) {
                        onItemChildClickListener.onItemChildClick(position);
                    }
                }
                default: {

                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.message_center_message: {
                    if (onLongClickListener != null) {
                        onLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    public void  setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public ArrayList<SystemMessageEntity> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<SystemMessageEntity> datas) {
        this.datas = datas;
    }

    public void setOnLongClickListener(OnItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
