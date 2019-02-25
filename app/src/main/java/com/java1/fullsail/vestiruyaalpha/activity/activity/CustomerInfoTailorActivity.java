package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityCustomerInfoTailorBinding;

public class CustomerInfoTailorActivity extends BaseActivity {
    ActivityCustomerInfoTailorBinding binding;
    private String userType;
    private User user;
    String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_customer_info_tailor);
        setUpClicks();

        userType= "Customers";

        DatabaseReference ref = null;
        customerId = getIntent().getStringExtra("customerId");
        ref = mDatabase.child(userType).child(customerId);

        mDialog.showCustomDalog();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.closeDialog();
                user = dataSnapshot.getValue(User.class);

                if(dataSnapshot.hasChild("City,State")) {
                    user.setCitystate(dataSnapshot.child("City,State").getValue().toString());
                    binding.tvCity.setText(dataSnapshot.child("City,State").getValue().toString());
                }
                else if(dataSnapshot.hasChild("city"))
                {
                    user.setCity(dataSnapshot.child("city").getValue().toString());
                    binding.tvCity.setText(dataSnapshot.child("city").getValue().toString());
                }
                user.setKey(dataSnapshot.getKey());
                saveProfileData(mActivity, user);


                binding.tvName.setText(user.getUsername());
                binding.tvEmail.setText(user.getEmail());

                if(user.getAddress()!=null && !user.getAddress().isEmpty())
                {
                    user.setAddress(user.getAddress());
                }
                else
                {
                    binding.tvAddress.setVisibility(View.GONE);
                }

                if(user.getProfilePic()!=null && !user.getProfilePic().isEmpty())
                {


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder);

                    Glide.with(mActivity).load(user.getProfilePic()).apply(options).into(binding.icProfile);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.closeDialog();
            }
        });
    }
    private void setUpClicks() {

        binding.btnLogout.setOnClickListener(this);
        binding.icEdit.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {

            case R.id.btnLogout:
                CommonUtils.clearSharedPrefs(mActivity);
                Intent i=new Intent(mActivity,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;

            case R.id.icEdit:

                i=new Intent(mActivity,EditProfileActivity.class);
                startActivityForResult(i,101);
                break;

            case R.id.icBack:

                onBackPressed();
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
                if(data.getExtras()!=null)
                {


                    String name=data.getStringExtra("username");
                    String email=data.getStringExtra("email");
                    String address=data.getStringExtra("address");
                    String password=data.getStringExtra("password");
                    String download=data.getStringExtra("pic");

                    binding.tvName.setText(name);
                    binding.tvEmail.setText(email);
                    binding.tvAddress.setText(address);

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder);

                    Glide.with(mActivity).load(download).apply(options).into(binding.icProfile);

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }
}
