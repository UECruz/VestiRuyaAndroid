package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.ItemModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivitySummaryBinding;

import java.util.Calendar;
import java.util.HashMap;

public class SummaryActivity extends BaseActivity {
    ActivitySummaryBinding binding;
    public HashMap<String, String> summeryMap = new HashMap<>();
    private boolean isEdit;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_summary);
        summeryMap = (HashMap<String, String>) getIntent().getSerializableExtra("data");

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            orderId = getIntent().getStringExtra("orderId");
        }

        binding.tvPost.setOnClickListener(this);

        binding.tvBodyType.setText(summeryMap.get("bodytype"));
        binding.tvFabric.setText(summeryMap.get("fabrics"));
        binding.tvSleeves.setText(summeryMap.get("neckline"));
        binding.tvStraps.setText(summeryMap.get("sleeves"));
        binding.tvNeckline.setText(summeryMap.get("straps"));
        binding.tvBackDetail.setText(summeryMap.get("backDetails"));
        binding.tvEmbelishment.setText(summeryMap.get("embellishment"));

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tvPost:
                if (TextUtils.isEmpty(binding.etPrice.getText())) {
                    Toast.makeText(mActivity, "Please enter price", Toast.LENGTH_SHORT).show();
                    return;
                }
                summeryMap.put("dateTime", Constant.simpleDateFormat.format(Calendar.getInstance().getTime()));
                summeryMap.put("userId", user.getKey());
                summeryMap.put("userName", user.getUsername());
                summeryMap.put("price", binding.etPrice.getText().toString());
                uploadPost();
                break;
        }
    }


    private void uploadPost() {
        mDialog.showCustomDalog();

        JobModel model = new JobModel();
        model.setCanEdit(false);
        model.setIsJobConfirmed(false);
        model.setPhotoImage(user.getProfilePic());
        model.setPrice(binding.etPrice.getText().toString());
        model.setTailorID("");
        model.setUserid(user.getKey());
        model.setUsername(user.getUsername());

        ItemModel items = new ItemModel();
        items.setBodytype(summeryMap.get("bodytype"));
        items.setFabric(summeryMap.get("fabrics"));
        items.setNeckline(summeryMap.get("neckline"));
        items.setSleeves(summeryMap.get("sleeves"));
        items.setStraps(summeryMap.get("straps"));
        items.setBackdetail(summeryMap.get("backDetails"));
        items.setEmbellishment(summeryMap.get("embellishment"));

        model.setItemmodel(items);

        if (!isEdit)
            mDatabase.child(Constant.CUSTOMER).child(Constant.ORDERS).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mDialog.closeDialog();
                    if (task.isSuccessful()) {
                        Constant.isAdded = true;
                        Toast.makeText(mActivity, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mActivity, "Failure in post data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        else
            mDatabase.child(Constant.CUSTOMER).child(Constant.ORDERS).child(orderId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mDialog.closeDialog();
                    if (task.isSuccessful()) {
                        Constant.isAdded = true;
                        Toast.makeText(mActivity, "updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mActivity, "Failure in update data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
