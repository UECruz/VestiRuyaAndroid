package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.TextViewAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityTailorHomeScreenBinding;

import java.util.ArrayList;

public class TailorHomeScreen extends BaseActivity {
    ActivityTailorHomeScreenBinding binding;
    private Activity mActivity;
    ArrayList<TailorJob> list = new ArrayList<>();
    TextViewAdapter acceptedAdapter;
    ValueEventListener listener;

    private TextViewAdapter mAdapter;
    ArrayList<TailorJob> listHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = TailorHomeScreen.this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_tailor_home_screen);
        setUpClicks();
        setAdapter();
        getAcceptedJobs();
    }

    private void setData() {
        user = getProfileData(this);
        binding.tvUserName.setText(user.getUsername());
    }

    private void getAcceptedJobs() {
        mDialog.showCustomDalog();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                listHistory.clear();
                list.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String jobid = postSnapshot.getKey();
                    TailorJob model = postSnapshot.getValue(TailorJob.class);
                    model.setKey(jobid);
                    if (model.getUserId() != null && model.getUserId().equals(user.getKey()) && model.getisAccepted() && !model.getisConfimed()) {
                        list.add(model);
                    }
                    if (model.getisConfimed() && firebaseUser.getUid().equals(model.getUserId())) {
                        listHistory.add(model);
                    }
                }
                if (list.size() > 0) {
                    binding.tvNoDataAccepted.setVisibility(View.GONE);
                } else {
                    binding.tvNoDataAccepted.setVisibility(View.VISIBLE);
                }
                if (listHistory.size() > 0) {
                    binding.tvNoDataHistory.setVisibility(View.GONE);
                } else {
                    binding.tvNoDataHistory.setVisibility(View.VISIBLE);
                }
                acceptedAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();

                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mDialog.closeDialog();
                Log.d("error", databaseError.getMessage());
            }
        };
        mDatabase.child(Constant.TAYLOR).child("Job").addValueEventListener(listener);
    }


    private void setAdapter() {
        binding.rvAccepted.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        acceptedAdapter = new TextViewAdapter(list, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View v) {
                Intent i = new Intent(mActivity, JobDetailActivity.class);
                i.putExtra("key", list.get(position).getKey());
                startActivity(i);
            }
        }, TextViewAdapter.AcceptedType);
        binding.rvAccepted.setAdapter(acceptedAdapter);


        mAdapter = new TextViewAdapter(listHistory, null, TextViewAdapter.HistoryType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        binding.rvHistory.setLayoutManager(mLayoutManager);
        binding.rvHistory.setItemAnimator(new DefaultItemAnimator());
        binding.rvHistory.setAdapter(mAdapter);
    }


    private void setUpClicks() {
        binding.icUser.setOnClickListener(this);
        binding.tvFindJob.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.icUser:
                i = new Intent(mActivity, ProfileActivity.class);
                startActivityForResult(i,102);
                break;
            case R.id.tvFindJob:

                i = new Intent(mActivity, ListofJobActivity.class);
                startActivityForResult(i,103);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.child(Constant.TAYLOR).child("Job").removeEventListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==102)
            {
                User user=getProfileData(mActivity);
                binding.tvUserName.setText(user.getUsername());

            }
            else if(requestCode==103)
            {

                getAcceptedJobs();
            }
        }
    }
}
