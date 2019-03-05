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
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityCustomerInfoTailorBinding;

@SuppressWarnings("ALL")
public class CustomerInfoTailorActivity extends BaseActivity {
    ActivityCustomerInfoTailorBinding binding;
    private String userType;
    private  User user;
    String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_customer_info_tailor);
        //User user=getProfileData(mActivity);
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
                //  saveProfileData(mActivity, user);

                //user.setKey(dataSnapshot.getKey());

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

                //binding.tvAddress.setText(user.getAddress());

                if(user.getProfilePic()!=null && !user.getProfilePic().isEmpty())
                {

                   /* Glide.with(this).placeholder(R.drawable.placeholder)
                            .load(user.getProfilePic())// image url
                            .into(binding.icProfile);*/

                    //Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_placeholder).into(binding.icProfile);

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder);

                    Glide.with(mActivity).load(user.getProfilePic()).apply(options).into(binding.icProfile);
                }


                //CommonUtils.setStringSharedPref(mActivity,Constant.SF_Type,userType);
                //CommonUtils.setBooleanPref(mActivity, Constant.SF_IS_LOGIN, true);
                //user = getProfileData(mActivity);

                //CommonUtils.redirectToActivity(mActivity, mActivity, MainActivity.class, true, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.closeDialog();
                //Toast.makeText(mActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUpClicks() {

        binding.btnMeasurement.setOnClickListener(this);
        binding.icEdit.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {

            case R.id.btnMeasurement:
                //  CommonUtils.clearSharedPrefs(mActivity);
                Intent i=new Intent(mActivity,CustomerInfoActivity.class);
                i.putExtra("customerId",customerId);
                startActivity(i);
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

                   /*i.putExtra("username",binding.edtName.getText().toString());
                   i.putExtra("email",binding.edtEmail.getText().toString());
                   i.putExtra("address",binding.edtAddress.getText().toString());
                   i.putExtra("password",binding.edtPassword.getText().toString());*/

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

                    //binding.
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
