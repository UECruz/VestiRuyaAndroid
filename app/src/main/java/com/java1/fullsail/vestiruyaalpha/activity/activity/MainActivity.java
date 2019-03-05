package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.OrderAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.TailorInterestAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.InterestsShownModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{
    ActivityMainBinding binding;

    private OrderAdapter orderAdapter;
    private ArrayList<JobModel> orderModels = new ArrayList<>();
    private TailorInterestAdapter mAdapter;
    private List<InterestsShownModel> listIntrest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        user = getProfileData(this);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_main);
        binding.tvUserName.setText(user.getUsername());
        setOrderListAdapter();
        setTailorInterestAdapter();
        setListeners();
        loadMyOrders();
    }

    private void setTailorInterestAdapter() {

        binding.rvTailorInterest.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mAdapter = new TailorInterestAdapter(listIntrest, mActivity);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View v) {

                switch (v.getId())
                {

                    case R.id.mainLayout:

                        Intent i=new Intent(mActivity,TailorInfoActivity.class);
                        i.putExtra("tailorinfo",listIntrest.get(position));
                        startActivity(i);

                        break;
                }

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        binding.rvTailorInterest.setLayoutManager(mLayoutManager);
        binding.rvTailorInterest.setItemAnimator(new DefaultItemAnimator());
        binding.rvTailorInterest.setAdapter(mAdapter);
    }

    private void setOrderListAdapter() {

        binding.rvOrderList.setLayoutManager(new LinearLayoutManager(mActivity));
        orderAdapter = new OrderAdapter(orderModels, new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View v) {
                switch (v.getId()) {
                    case R.id.tvDelete:

                        mDatabase.child(Constant.CUSTOMER).child(Constant.ORDERS).child(orderModels.get(position).getKey()).setValue(null);
                        orderModels.remove(position);
                        orderAdapter.notifyItemRemoved(position);

                        break;
                    case R.id.tvEdit:
                        Bundle b = new Bundle();
                        b.putBoolean("isEdit",true);
                        b.putString("orderId",orderModels.get(position).getKey());
                        CommonUtils.redirectToActivity(mActivity, mActivity, CustomizeActivity.class, false, b, 0);
                        break;
                }
            }
        });
        binding.rvOrderList.setAdapter(orderAdapter);
    }

    private void setListeners() {
        binding.linAddOrder.setOnClickListener(this);
        binding.icUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linAddOrder:

                CommonUtils.redirectToActivity(mActivity, mActivity, CustomizeActivity.class, false, new Bundle(), 0);
                break;

            case R.id.icUser:
                Intent i = new Intent(mActivity, ProfileActivity.class);
                startActivityForResult(i, 102);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadMyOrders() {
        mDialog.showCustomDalog();
        mDatabase.child(Constant.CUSTOMER).child(Constant.ORDERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDialog.closeDialog();
                orderModels.clear();
                listIntrest.clear();
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    JobModel model = childSnap.getValue(JobModel.class);
                    model.setKey(childSnap.getKey());
                    if (model.getUserid() != null && user.getKey().equals(model.getUserid()))
                        orderModels.add(model);
                    if (childSnap.hasChild("interestsShown") && firebaseUser.getUid().equals(model.getUserid())) {
                        for (DataSnapshot snapshot : childSnap.child("interestsShown").getChildren()) {
                            InterestsShownModel interestsShownModel = snapshot.getValue(InterestsShownModel.class);
                            listIntrest.add(interestsShownModel);

                        }
                    }
                }
                orderAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();

                if (orderModels.size() > 0) {
                    binding.linAddOrder.setVisibility(View.GONE);
                } else {
                    binding.linAddOrder.setVisibility(View.VISIBLE);
                }
                if (listIntrest.size() > 0) {
                    binding.tvNoData1.setVisibility(View.GONE);
                } else {
                    binding.tvNoData1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.closeDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 102) {

                User user = getProfileData(mActivity);
                binding.tvUserName.setText(user.getUsername());

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        if (orderAdapter != null) {
            orderAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (orderAdapter != null) {
            orderAdapter.restoreStates(savedInstanceState);
        }
    }
}
