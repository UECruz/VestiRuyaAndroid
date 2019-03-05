package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.model.Customize;
import com.java1.fullsail.vestiruyaalpha.databinding.ItemCustomizeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {
    @SuppressWarnings("WeakerAccess")
    ArrayList<Customize> list;
    public int selectedIndex = -1;

    public SummaryAdapter(ArrayList<Customize> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customize, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.binding.tvName.setText(list.get(i).getName());
        myViewHolder.binding.llContainer.setBackgroundResource(android.R.color.white);
        Picasso.get().load(list.get(i).getImage()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).resize(400,400).onlyScaleDown().into(myViewHolder.binding.ivCat);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCustomizeBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
