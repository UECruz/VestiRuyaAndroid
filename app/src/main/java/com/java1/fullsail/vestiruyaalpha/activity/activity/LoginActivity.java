package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityLoginBinding;

import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.CUSTOMER;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.TAYLOR;

public class LoginActivity extends BaseActivity {
    private boolean isTailor;
    ActivityLoginBinding binding;
    private String userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_login);

        userType=CommonUtils.getStringSharedPref(mActivity,Constant.SF_Type,"");
        boolean isLoggedIn = CommonUtils.getBooleanSharedPref(mActivity, Constant.SF_IS_LOGIN, false);

        if (isLoggedIn && userType.equals("Customers")) {
            CommonUtils.redirectToActivity(mActivity, mActivity, MainActivity.class, true, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if(isLoggedIn && userType.equals("Tailors"))
        {
            CommonUtils.redirectToActivity(mActivity, mActivity, TailorHomeScreen.class, true, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        setListeners();
    }

    private void setListeners() {

        binding.btnLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                boolean isEmail = CommonUtils.validateForEmpty(binding.etEmail, mActivity, getResources().getString(R.string.required_field));
                boolean isPass = CommonUtils.validateForEmpty(binding.etPass, mActivity, getResources().getString(R.string.required_field));
                if (isEmail && isPass) {
                    if (CommonUtils.isValidEmail(binding.etEmail.getText().toString())) {
                        if (CommonUtils.isInternetAvailable(mActivity)) {
                            signIn(binding.etEmail.getText().toString(), binding.etPass.getText().toString());
                        } else {
                        }
                    } else {
                        binding.etEmail.setError("Enter valid email");
                    }
                }

                break;
            case R.id.btnSignUp:
                Intent intent = new Intent(mActivity, LoginTypeSelectorActivity.class);
                startActivity(intent);
        }
    }

    private void signIn(String email, String password) {

        mDialog.showCustomDalog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.closeDialog();
                        if (task.isSuccessful()) {

                            firebaseUser = mAuth.getCurrentUser();

                            if (firebaseUser != null) {
                                mDatabase.child(TAYLOR).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                            if (childDataSnapshot.getValue() != null) {

                                                if (firebaseUser.getUid().equals(childDataSnapshot.getKey())) {
                                                    User user = childDataSnapshot.getValue(User.class);

                                                    if(dataSnapshot.hasChild("City,State")) {
                                                        user.setCitystate(dataSnapshot.child("City,State").getValue().toString());
                                                    }
                                                    else if(dataSnapshot.hasChild("city"))
                                                    {
                                                        user.setCity(dataSnapshot.child("city").getValue().toString());
                                                    }//user.setCitystate(childDataSnapshot.child("City,State").getValue().toString());

                                                    user.setKey(childDataSnapshot.getKey());
                                                    saveProfileData(mActivity, user);

                                                    CommonUtils.setStringSharedPref(mActivity, Constant.SF_Type, TAYLOR);
                                                    CommonUtils.setBooleanPref(mActivity, Constant.SF_IS_LOGIN, true);

                                                    CommonUtils.redirectToActivity(mActivity, mActivity, TailorHomeScreen.class, true, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    break;

                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            if(firebaseUser!=null)
                            {

                                mDatabase.child(CUSTOMER).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                            if (childDataSnapshot.getValue() != null) {

                                                if (firebaseUser.getUid().equals(childDataSnapshot.getKey())) {
                                                    User user = childDataSnapshot.getValue(User.class);

                                                    if(dataSnapshot.hasChild("City,State")) {
                                                        user.setCitystate(dataSnapshot.child("City,State").getValue().toString());
                                                    }
                                                    else if(dataSnapshot.hasChild("city"))
                                                    {
                                                        user.setCity(dataSnapshot.child("city").getValue().toString());
                                                    }

                                                    user.setKey(childDataSnapshot.getKey());
                                                    saveProfileData(mActivity, user);



                                                    CommonUtils.setStringSharedPref(mActivity, Constant.SF_Type, CUSTOMER);
                                                    CommonUtils.setBooleanPref(mActivity, Constant.SF_IS_LOGIN, true);
                                                    CommonUtils.redirectToActivity(mActivity, mActivity, MainActivity.class, true, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    break;

                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        mDialog.closeDialog();
                                    }
                                });
                            }
                        }
                        else
                        {
                            mDialog.closeDialog();
                            Toast.makeText(mActivity,"Invalid Credential",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
