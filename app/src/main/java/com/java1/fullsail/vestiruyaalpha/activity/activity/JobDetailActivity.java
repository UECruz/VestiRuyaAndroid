package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityItemDescriptionBinding;

import java.util.HashMap;

public class JobDetailActivity extends BaseActivity {
    private ActivityItemDescriptionBinding binding;
    private String jobkey;
    TailorJob model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_item_description);

        binding.appBar.setVisibility(View.VISIBLE);
        setSupportActionBar(binding.toolbar);
        setTitle("");

        jobkey = getIntent().getStringExtra("key");
        binding.rlCancel.setVisibility(View.GONE);
        binding.btnOk.setText("Done");
        getJobdata();
        setUpClicks();
    }

    private void getJobdata() {
        mDialog.showCustomDalog();
        mDatabase.child(Constant.TAYLOR).child("Job").child(jobkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                model = dataSnapshot.getValue(TailorJob.class);

                displayData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mDialog.closeDialog();
                Log.d("error", databaseError.getMessage());
            }
        });
    }

    private void displayData() {
        binding.tvDressType.setText(model.getItems().getDressType());
        binding.tvFabric.setText(model.getItems().getFabric());
        binding.tvBackDetails.setText(model.getItems().getBackDetail());
        binding.tvEmbelishment.setText(model.getItems().getEmbellish());
        binding.tvNeckline.setText(model.getItems().getNeckline());
        binding.tvSleeve.setText(model.getItems().getSlevee());
        binding.tvStrap.setText(model.getItems().getStrap());
        binding.tvUsername.setText(model.getName());
        binding.tvPrice.setText(model.getPrice());
    }

    private void setUpClicks() {
        binding.btnOk.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);
        binding.icImage.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icBack:
                onBackPressed();
                break;
            case R.id.btnOk:
                mDialog.showCustomDalog();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isConfimed", true);

                mDatabase.child(Constant.TAYLOR).child("Job").child(jobkey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDialog.closeDialog();
                        if (task.isSuccessful()) {
                            finish();
                        }
                    }
                });
                break;

            case R.id.icImage:

                Intent i=new Intent(mActivity,CustomerInfoActivity.class);
                i.putExtra("customerId",model.getCustomerId());
                i.putExtra("name",model.getName());
                startActivity(i);
                break;
        }
    }
}
