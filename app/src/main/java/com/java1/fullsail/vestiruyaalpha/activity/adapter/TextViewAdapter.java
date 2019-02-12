package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.LayoutTextviewBinding;

import java.util.ArrayList;

public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.MyViewHolder>{
    private ArrayList<TailorJob> list;
    private OnItemClickListener listener;
    int type;
    public static int AcceptedType = 0;
    public static int HistoryType = 1;

    public TextViewAdapter(ArrayList<TailorJob> list, OnItemClickListener listener, int type) {
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_textview, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.binding.tvName.setText(list.get(i).getName());
        holder.binding.tvPrice.setText(list.get(i).getPrice());
        if (type == HistoryType) {
            holder.binding.tvPrice.setVisibility(View.GONE);
        }
        else
        {
            holder.binding.tvPrice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutTextviewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.OnItemClick(getAdapterPosition(), view);
                }
            });
        }
    }
}
