package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.MyViewHolder> {

    private List<JobModel> list=new ArrayList<>();
    private Context ctx;
    private OnItemClickListener onItemClickListener;
    boolean isPriceVisible=true;

    public JobAdapter(List<JobModel> list,Context ctx,boolean isPriceVisible) {
        this.list = list;
        this.ctx=ctx;
        this.isPriceVisible=isPriceVisible;
    }

    public void setOnItemClickListner(OnItemClickListener onItemClickListner)
    {
        this.onItemClickListener=onItemClickListner;
    }

    @NonNull
    @Override
    public JobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_job_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull JobAdapter.MyViewHolder myViewHolder, int i) {

        JobModel model=list.get(i);

        if(isPriceVisible)
        {
            myViewHolder.tvPrice.setVisibility(View.VISIBLE);

            double price=Double.valueOf(list.get(i).getPrice());
            myViewHolder.tvPrice.setText("$"+ String.format("%.2f", price));
        }
        else
        {
            myViewHolder.tvPrice.setVisibility(View.GONE);
        }

        myViewHolder.tvName.setText(list.get(i).getUsername());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder);

        Glide.with(ctx).load(model.getPhotoImage()).apply(options).into(myViewHolder.icImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icImage;
        @SuppressWarnings("CanBeFinal")
        private TextView tvName,tvPrice;
        private RelativeLayout rlMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icImage=itemView.findViewById(R.id.icImage);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            rlMain=itemView.findViewById(R.id.rlMain);
            rlMain.setOnClickListener(this);


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
