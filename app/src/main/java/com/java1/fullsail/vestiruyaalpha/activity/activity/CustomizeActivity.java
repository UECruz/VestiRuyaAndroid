package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.fragment.CustomizeFragment;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityCustomizeBinding;

import java.util.HashMap;

public class CustomizeActivity extends BaseActivity {
    public ActivityCustomizeBinding binding;
    public HashMap<String,String> summeryMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_customize);

        gotoFragment(CustomizeFragment.newInstance(0,getIntent().getExtras()));
        setListeners();
    }

    private void setListeners() {

        binding.tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                onBackPressed();
                break;
        }
    }

    public void gotoFragment(Fragment fg) {
        if (fg.getArguments() != null && fg.getArguments().getInt("position", 0) > 6) {
            Toast.makeText(mActivity, "Summary", Toast.LENGTH_SHORT).show();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fg).commit();
        }
    }
}
