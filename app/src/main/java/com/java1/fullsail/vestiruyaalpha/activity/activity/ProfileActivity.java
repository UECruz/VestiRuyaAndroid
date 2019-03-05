package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.OnItemClickListener;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.RatingAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.InterestsShownModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.ReviewModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity {
    ActivityProfileBinding binding;
    private String userType;
    private  User user;
    private InterestsShownModel model;
    private RatingAdapter mAdapter;
    private List<ReviewModel> list=new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_profile);
        //User user=getProfileData(mActivity);
        setUpClicks();

        userType=CommonUtils.getStringSharedPref(mActivity,Constant.SF_Type,"");
        Log.d("userType",userType);
        DatabaseReference ref = null;
        ref = mDatabase.child(userType).child(firebaseUser.getUid());

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
                    //noinspection ConstantConditions
                    user.setCity(dataSnapshot.child("city").getValue().toString());
                    binding.tvCity.setText(dataSnapshot.child("city").getValue().toString());
                }
                user.setKey(dataSnapshot.getKey());
                saveProfileData(mActivity, user);

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

       /* binding.tvName.setText(user.getUsername());
        binding.tvEmail.setText(user.getEmail());
        binding.tvAddress.setText(user.getAddress());
        binding.tvCity.setText(user.getCity());

        if(user.getProfilePic()!=null && !user.getProfilePic().isEmpty())
        {
            //Picasso.get().load(user.getProfilePic()).into(binding.icProfile);
*//*
            Glide.with(this).placeholder(R.drawable.placeholder)
                    .load(user.getProfilePic())// image url
                    .into(binding.icProfile);*//*

           //Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_placeholder).into(binding.icProfile);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);

            Glide.with(this).load(user.getProfilePic()).apply(options).into(binding.icProfile);
        }
        setUpClicks();*/
        if (userType.equals("Customers")) {
            binding.relativeReview.setVisibility(View.GONE);
            binding.txtReview.setVisibility(View.GONE);
        } else {
            setRatingAdapter();
            getRatings();
        }

        getData(1);
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
                    if(firebaseUser.getUid().equals(reviewModel.getTailorId()))
                    {
                        list.add(reviewModel);
                    }
                    //Log.e("Get Data", post.<YourMethod>());
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
    private void getData(final int i) {
        String childName,childName1;
        if (userType.equals("Customers")) {
            childName = Constant.customers_profile_images;
            childName1 = Constant.customers_sample_images;
        } else {
            childName = Constant.tailors_profile_images;
            childName1 = Constant.tailors_sample_images;
        }
        storageRef.child(childName1+"/" + firebaseUser.getUid() + "_File_" + i).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
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
                finish();
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
