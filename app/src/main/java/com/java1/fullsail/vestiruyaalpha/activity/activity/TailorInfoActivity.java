package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.RatingAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.model.InterestsShownModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.ReviewModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityTailorInfoBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TailorInfoActivity extends BaseActivity {
    private ActivityTailorInfoBinding binding;
    private Activity mActivity;
    private InterestsShownModel model;
    private RatingAdapter mAdapter;
    private List<ReviewModel> list=new ArrayList<>();
    private boolean isWorkDone=false;
    private TailorJob tailorjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=TailorInfoActivity.this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_tailor_info);
        model=(InterestsShownModel)getIntent().getSerializableExtra("tailorinfo");
        setupClicks();
        setUpData();
        getData(1);
        setRatingAdapter();
        getRatings();
        getIsJonDone();

    }

    private void getIsJonDone() {

        mDatabase.child("Tailors").child("Job").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    tailorjob=postSnapshot.getValue(TailorJob.class);
                    if(firebaseUser.getUid().equals(tailorjob.getCustomerId()) && model.getTailorID().equals(tailorjob.getUserId()) && tailorjob.getisConfimed())
                    {

                        isWorkDone=true;
                        binding.icJob.setImageResource(R.drawable.ic_job_done);
                        break;
                    }
                    else
                    {
                        binding.icJob.setImageResource(R.drawable.ic_job);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRatings() {

        mDialog.showCustomDalog();
        mDatabase.child("Customers").child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                mDialog.closeDialog();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ReviewModel reviewModel = postSnapshot.getValue(ReviewModel.class);
                    if(model.getTailorID().equals(reviewModel.getTailorId()))
                    {
                        list.add(reviewModel);
                    }
                }
                mAdapter.notifyDataSetChanged();

                if(list.size()==0)
                {
                    binding.tvNoData.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.tvNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mDialog.closeDialog();
            }
        });
    }

    private void setRatingAdapter() {

        mAdapter = new RatingAdapter(list,mActivity);
        mAdapter.setOnItemClickListner(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View v) {

                switch (v.getId())
                {

                    case R.id.rlMain:



                        break;
                }

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        binding.rvRatings.setLayoutManager(mLayoutManager);
        binding.rvRatings.setItemAnimator(new DefaultItemAnimator());
        binding.rvRatings.setAdapter(mAdapter);

    }


    private void setUpData() {
        binding.tvUsername.setText(model.getTailorname());
        binding.tvName.setText(model.getTailorname());
        binding.tvAddress.setText(model.getTailorLoc());

        if(model.getTailorPic()!=null && !model.getTailorPic().isEmpty())
        {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);

            Glide.with(mActivity).load(model.getTailorPic()).apply(options).into(binding.icImage);
        }
    }

    private void setupClicks() {

        binding.icBack.setOnClickListener(this);
        binding.icJob.setOnClickListener(this);
        binding.icChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.icBack:

                finish();
                break;

            case R.id.icJob:

                if(tailorjob!=null && isWorkDone) {
                    Intent i = new Intent(mActivity, OrderPreviewActivity.class);
                    i.putExtra("items", tailorjob);
                    i.putExtra("from","tailorinfo");
                    startActivity(i);
                }
                break;

            case R.id.icChat:

                Intent i=new Intent(mActivity,ChatActivity.class);
                i.putExtra("customerId",tailorjob.getUserId());
                startActivity(i);
                break;
        }
    }

    private void getData(final int i) {
        storageRef.child("tailors_sample_images/" + model.getTailorID() + "_File_" + i).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    int temp = i + 1;
                    if (temp < 4) {
                        getData(temp);
                    }
                    if (i == 1) {
                        Picasso.get().load(task.getResult()).error(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(binding.iv1);
                    } else if (i == 2) {
                        Picasso.get().load(task.getResult()).error(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(binding.iv2);
                    } else if (i == 3) {
                        Picasso.get().load(task.getResult()).error(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(binding.iv3);
                    }
                }
            }


        });

    }
}
