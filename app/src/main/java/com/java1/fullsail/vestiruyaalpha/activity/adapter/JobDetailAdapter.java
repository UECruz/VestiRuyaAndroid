package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java1.fullsail.vestiruyaalpha.activity.activity.JobDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JobDetailAdapter extends RecyclerView.Adapter<JobDetailAdapter.JobViewHolder> {

    private ArrayList<HashMap<String, Object>> jobs;


    public JobDetailAdapter(ArrayList<HashMap<String, Object>> acceptedOrders) {
        this.jobs = acceptedOrders;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new JobViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder jobViewHolder, int position) {
        jobViewHolder.tvName.setText(jobs.get(position).get("name").toString());
        jobViewHolder.tvPrice.setText(jobs.get(position).get("price").toString());
        jobViewHolder.itemView.setTag(jobs.get(position));
        jobViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), JobDetailActivity.class);
                intent.putExtra("data", (Serializable) view.getTag());

                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobs != null ? jobs.size() : 0;
    }


    public class JobViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPrice;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
            tvPrice = itemView.findViewById(android.R.id.text2);
        }
    }
}
