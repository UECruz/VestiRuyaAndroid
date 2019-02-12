package com.java1.fullsail.vestiruyaalpha.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.model.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder>{
    private List<ReviewModel> list=new ArrayList<>();
    private Context ctx;
    private OnItemClickListener onItemClickListner;

    public RatingAdapter(List<ReviewModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    public void setOnItemClickListner(OnItemClickListener onItemClickListner)
    {
        this.onItemClickListner=onItemClickListner;
    }

    @NonNull
    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rating, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.MyViewHolder myViewHolder, int i) {


        ReviewModel model=list.get(i);
        myViewHolder.tvMessage.setText(model.getMessage());
        myViewHolder.ratingBar.setRating(Float.valueOf(model.getRating()));

        if(model.getPhotoImage()!=null && !model.getPhotoImage().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);

            Glide.with(ctx).load(model.getPhotoImage()).apply(options).into(myViewHolder.icImage);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RatingBar ratingBar;
        private TextView tvMessage;
        private ImageView icImage;
        private RelativeLayout rlMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar=itemView.findViewById(R.id.ratings);
            tvMessage=itemView.findViewById(R.id.tvMessage);
            rlMain=itemView.findViewById(R.id.rlMain);
            icImage=itemView.findViewById(R.id.icImage);

            rlMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(onItemClickListner!=null)
            {
                onItemClickListner.OnItemClick(getAdapterPosition(),view);
            }
        }
    }
}
