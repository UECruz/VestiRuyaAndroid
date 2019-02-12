package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityTypeSelectorBinding;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;

import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.CUSTOMER;
import static com.java1.fullsail.vestiruyaalpha.activity.core.Constant.TAYLOR;



public class LoginTypeSelectorActivity extends BaseActivity {
    ActivityTypeSelectorBinding binding;
    //private String userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_type_selector);

        setListeners();
    }

    private void setListeners() {
        binding.btnCustmoer.setOnClickListener(this);
        binding.btnTailor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.btnCustmoer:
                CommonUtils.setStringSharedPref(mActivity, Constant.SF_Type, CUSTOMER);
                Intent i=new Intent(mActivity,SignUpActivity.class);
                i.putExtra("userType", "Customers");
                startActivity(i);

                break;
            case R.id.btnTailor:
                CommonUtils.setStringSharedPref(mActivity, Constant.SF_Type, TAYLOR);
                i=new Intent(mActivity,SignUpActivity.class);
                i.putExtra("userType", "Tailors");
                startActivity(i);
                break;
        }
    }
}
