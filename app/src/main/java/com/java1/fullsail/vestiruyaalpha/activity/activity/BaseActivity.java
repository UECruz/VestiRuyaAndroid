package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.core.CustomProgressDialog;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    Activity mActivity;
    protected FirebaseAuth mAuth;
    protected CustomProgressDialog mDialog;
    protected FirebaseUser firebaseUser;
    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage;
    protected StorageReference storageRef;
    public User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = getProfileData(getApplicationContext());

        mDialog = new CustomProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
    }


    public static void saveProfileData(Context context, User profilePojo) {
        Gson gson = new Gson();
        String json = gson.toJson(profilePojo);
        CommonUtils.setStringSharedPref(context, Constant.SF_PROFILE, json);
    }

    public static User getProfileData(Context context) {
        Gson gson = new Gson();
        String json = CommonUtils.getStringSharedPref(context, Constant.SF_PROFILE, "");
        return gson.fromJson(json, User.class);
    }

}
