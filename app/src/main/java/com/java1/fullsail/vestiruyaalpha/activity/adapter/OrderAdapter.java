package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.core.swipe_reveal.ViewBinderHelper;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.OrderModel;
import com.java1.fullsail.vestiruyaalpha.databinding.OrderItemLayoutBinding;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter{
    ArrayList<JobModel> list;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    OnItemClickListener listener;

    public OrderAdapter(ArrayList<JobModel> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;
        if (list != null && 0 <= position && position < list.size()) {
            final JobModel data = list.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.binding.swipeLayout, data.getKey());

            // Bind your data here
            holder.bind(data);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(final JobModel data) {
            binding.frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binderHelper.closeLayout(list.get(getAdapterPosition()).getKey());
                }
            });
            binding.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binderHelper.closeLayout(list.get(getAdapterPosition()).getKey());
                    listener.OnItemClick(getAdapterPosition(), v);
                }
            });
            binding.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binderHelper.closeLayout(list.get(getAdapterPosition()).getKey());
                    listener.OnItemClick(getAdapterPosition(), v);
                }
            });
            binding.tvName.setText(data.getUsername());
            if (data.getPrice().contains("$")) {
                String price = data.getPrice().replace("$", "");

                binding.tvOrderPrice.setText("$"+ String.format("%.2f", Double.valueOf(price)));
                //binding.tvOrderPrice.setText(data.getPrice());
            }
            else {
                binding.tvOrderPrice.setText("$" + String.format("%.2f", Double.valueOf(data.getPrice())));
            }

            binding.tvFabric.setText(data.getItemmodel().getFabric());
            binding.tvEmbelishment.setText(data.getItemmodel().getEmbellishment());
            binding.tvNeckline.setText(data.getItemmodel().getNeckline());
            binding.tvBodyType.setText(data.getItemmodel().getBodytype());
            binding.tvBack.setText(data.getItemmodel().getBackdetail());
            binding.tvSleeves.setText(data.getItemmodel().getSleeves());
            binding.tvStraps.setText(data.getItemmodel().getStraps());

        }
    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
}
