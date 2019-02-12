package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.model.ChatModel;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    private ArrayList<ChatModel> chatModelArrayList;
    private String userId;

    public ChatAdapter(ArrayList<ChatModel> chatModelArrayList, String userId) {
        this.chatModelArrayList = chatModelArrayList;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (chatModelArrayList.get(position).getSender().equals(userId)) {
            viewType = 1;
        } else {
            viewType = 0;
        }
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_chat, parent, false);
            return new RightViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_chat, parent, false);
            return new LeftViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).tvRightMessage.setText(chatModelArrayList.get(position).getMessage());
        } else {
            ((LeftViewHolder) holder).tvLeftMessage.setText(chatModelArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    private class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeftMessage;

        private LeftViewHolder(View itemView) {
            super(itemView);
            tvLeftMessage = itemView.findViewById(R.id.chat_tvUserLeft);
        }
    }

    private class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvRightMessage;

        private RightViewHolder(View itemView) {
            super(itemView);
            tvRightMessage = itemView.findViewById(R.id.chat_tvUserRight);
        }
    }
}
