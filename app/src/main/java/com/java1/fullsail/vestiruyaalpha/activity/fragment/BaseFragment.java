package com.java1.fullsail.vestiruyaalpha.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.java1.fullsail.vestiruyaalpha.activity.activity.BaseActivity;
import com.java1.fullsail.vestiruyaalpha.activity.core.CustomProgressDialog;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;

public class BaseFragment extends Fragment implements View.OnClickListener {


    protected FirebaseUser firebaseUser;
    public FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;
    protected Activity mActivity;
    protected CustomProgressDialog mDialog;
    public User user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        user = BaseActivity.getProfileData(mActivity);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mDialog = new CustomProgressDialog(getActivity());
    }


    @Override
    public void onClick(View view) {

    }
}
