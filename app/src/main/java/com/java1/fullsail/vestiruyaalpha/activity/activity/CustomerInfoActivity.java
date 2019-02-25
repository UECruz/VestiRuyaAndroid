package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.OrderAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.Measurement;
import com.java1.fullsail.vestiruyaalpha.activity.model.OrderModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityCustomerInfoBinding;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerInfoActivity extends BaseActivity {

    ActivityCustomerInfoBinding binding;
    private String customerId;
    private String customerName;
    private Measurement.MeasureItem measureItem;
    private  User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_customer_info);

        customerId = getIntent().getStringExtra("customerId");
//        customerName = getIntent().getStringExtra("name");
//        binding.tvUsername.setText(customerName);
        setListeners();

        loadData();
        getData(1);
    }

    private void loadData() {

        //mDatabase.
        mDialog.showCustomDalog();
        mDatabase.child("Customers").child("Measurement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {

                    String key=childSnap.getKey();
                    Measurement model=childSnap.getValue(Measurement.class);
                    if(model!=null && model.getMeasureItem()!=null && model.getUserId().equals(customerId))
                    {
                        measureItem=model.getMeasureItem();
                        binding.tvHeight.setText(measureItem.getHeight());
                        binding.tvNeckCIR.setText(measureItem.getNeck());
                        binding.tvChestCIR.setText(measureItem.getChest());
                        binding.tvWaistCIR.setText(measureItem.getWaist());
                        binding.tvBustSize.setText(measureItem.getBust());
                        binding.tvArmLength.setText(measureItem.getArm());
                        binding.tvLegLength.setText(measureItem.getLeg());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.closeDialog();
            }
        });
    }

    private void getData(final int i) {
        storageRef.child("customers_sample_images/" + customerId + "_File_" + i).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
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

    private void setListeners() {
        binding.icBack.setOnClickListener(this);
        binding.icChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icBack:
                onBackPressed();
                break;

            case R.id.icChat:

                Intent i=new Intent(mActivity,ChatActivity.class);
                i.putExtra("customerId",customerId);
                startActivity(i);
                break;
        }
    }
}
