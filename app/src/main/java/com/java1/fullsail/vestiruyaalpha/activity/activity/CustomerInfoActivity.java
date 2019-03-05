package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.model.Measurement;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityCustomerInfoBinding;
import com.squareup.picasso.Picasso;

@SuppressWarnings("ALL")
public class CustomerInfoActivity extends BaseActivity {

    ActivityCustomerInfoBinding binding;
    private String customerId;
    private String userId;
    private Measurement.MeasureItem measureItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_customer_info);

        customerId = getIntent().getStringExtra("customerId");
        //userId=getIntent().getStringExtra("userId");
        setListeners();

        loadData();
        getData(1);
    }

    private void loadData() {

        //mDatabase.
        mDialog.showCustomDalog();
        mDatabase.child("Customers").child("Measurement").orderByChild("userId").equalTo(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(CustomerInfoActivity.this,"User measurement Not found.",Toast.LENGTH_SHORT).show();
                    return;
                }


                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {

                    String key=childSnap.getKey();
                    Measurement model=childSnap.getValue(Measurement.class);
                    Log.d("userIdName",model.getUsername());
                    Log.d("userId",model.getUserId());

                    if(model!=null && model.getMeasureItem()!=null && model.getUserId().equals(customerId))
                    {
                        measureItem=model.getMeasureItem();
                        binding.tvUsernamee.setText(model.getUsername());
                        binding.tvHeight.setText(measureItem.getHeight());
                        binding.tvNeckCIR.setText(measureItem.getNeck());
                        binding.tvChestCIR.setText(measureItem.getChest());
                        binding.tvWaistCIR.setText(measureItem.getWaist());
                        binding.tvBustSize.setText(measureItem.getBust());
                        binding.tvArmLength.setText(measureItem.getArm());
                        binding.tvLegLength.setText(measureItem.getLeg());

                    }
                    /*OrderModel orderModel = childSnap.getValue(OrderModel.class);
                    orderModel.setOrderId(childSnap.getKey());
                    orderModels.add(orderModel);*/
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
