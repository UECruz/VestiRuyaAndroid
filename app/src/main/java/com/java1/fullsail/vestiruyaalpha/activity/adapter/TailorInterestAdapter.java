package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.model.InterestsShownModel;
import com.java1.fullsail.vestiruyaalpha.databinding.LayoutTextviewBinding;

import java.util.List;

public class TailorInterestAdapter extends RecyclerView.Adapter<TailorInterestAdapter.MyViewHolder> {

    private List<InterestsShownModel> list;
    private Context ctx;
    private OnItemClickListener onItemClickListener;

    public TailorInterestAdapter(List<InterestsShownModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_textview, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        InterestsShownModel model = list.get(i);

        myViewHolder.binding.tvName.setText(list.get(i).getTailorname());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder);

        Glide.with(ctx).load(model.getTailorPic()).apply(options).into(myViewHolder.binding.ivImage);


        myViewHolder.binding.tvPrice.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutTextviewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.mainLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(onItemClickListener!=null)
            {
                onItemClickListener.OnItemClick(getAdapterPosition(),view);
            }
        }
    }
}
