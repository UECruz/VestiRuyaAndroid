package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.JobAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityListofJobBinding;

import java.util.ArrayList;
import java.util.List;

public class ListofJobActivity extends BaseActivity {
    private ActivityListofJobBinding binding;
    private Activity mActivity;
    private JobAdapter mAdapter;
    private String userType;
    private List<JobModel> list = new ArrayList<>();
    boolean isPriceVisible=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = ListofJobActivity.this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_listof_job);
        userType = CommonUtils.getStringSharedPref(mActivity, Constant.SF_Type, "");
        setAdapter();
        setUpClicks();
        getData();
    }

    private void setUpClicks() {

        binding.icBack.setOnClickListener(this);
    }

    private void getData() {
        mDialog.showCustomDalog();
        DatabaseReference ref = null;
        ref = mDatabase.child("Customers").child("Orders");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String jobid = postSnapshot.getKey();
                    JobModel model = postSnapshot.getValue(JobModel.class);
                    model.setKey(jobid);
                    list.add(model);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mDialog.closeDialog();
                Log.d("error", databaseError.getMessage());
            }
        });

    }

    private void setAdapter() {

        mAdapter = new JobAdapter(list,mActivity,isPriceVisible);
        mAdapter.setOnItemClickListner(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View v) {

                switch (v.getId())
                {

                    case R.id.rlMain:

                        Intent i=new Intent(mActivity,ItemDescriptionActivity.class);
                        i.putExtra("jobmodel",list.get(position));
                        i.putExtra("key",list.get(position).getKey());
                        i.putExtra("item",list.get(position).getItemmodel());
                        startActivityForResult(i,101);


                        break;
                }

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.icBack:

                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {

            if(requestCode==101)
            {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
