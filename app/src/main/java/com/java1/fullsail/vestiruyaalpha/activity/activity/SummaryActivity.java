package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.adapter.SummaryAdapter;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.model.Customize;
import com.java1.fullsail.vestiruyaalpha.activity.model.ItemModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivitySummaryBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class SummaryActivity extends BaseActivity {
    ActivitySummaryBinding binding;
    public HashMap<String, ArrayList<Customize>> summeryMap = new HashMap<>();
    private boolean isEdit;
    private String orderId;
    ArrayList<Customize> list = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private SummaryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_summary);

        //noinspection unchecked
        summeryMap = (HashMap<String, ArrayList<Customize>>) getIntent().getSerializableExtra("data");

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            orderId = getIntent().getStringExtra("orderId");
        }

        list.add(summeryMap.get("bodytype").get(0));
        list.add(summeryMap.get("fabrics").get(0));
        list.add(summeryMap.get("neckline").get(0));
        list.add(summeryMap.get("sleeves").get(0));
        list.add(summeryMap.get("straps").get(0));
        list.add(summeryMap.get("backDetails").get(0));
        list.add(summeryMap.get("embellishment").get(0));

        adapter = new SummaryAdapter(list);
        binding.rvCategories.setLayoutManager(new GridLayoutManager(mActivity, 3));
        binding.rvCategories.setAdapter(adapter);
        binding.tvPost.setOnClickListener(this);
        @SuppressWarnings("RedundantCast") final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker) findViewById(R.id.single_day_picker);
        singleDateAndTimePicker.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                //  display(displayed);
                dateTime =displayed;
            }
        });
    }
    String dateTime;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tvPost:
                if (TextUtils.isEmpty(binding.etPrice.getText())) {
                    Toast.makeText(mActivity, "Please enter price", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadPost();
                break;

        }
    }


    private void uploadPost() {
        mDialog.showCustomDalog();

        JobModel model = new JobModel();
        model.setDate(dateTime);
        model.setCanEdit(false);
        model.setIsJobConfirmed(false);
        model.setPhotoImage(user.getProfilePic());
        model.setPrice(binding.etPrice.getText().toString());
        model.setTailorID("");
        model.setUserid(user.getKey());
        model.setUsername(user.getUsername());

        ItemModel items = new ItemModel();
        items.setBodytype(summeryMap.get("bodytype").get(0).getName());
        items.setFabric(summeryMap.get("fabrics").get(0).getName());
        items.setNeckline(summeryMap.get("neckline").get(0).getName());
        items.setSleeves(summeryMap.get("sleeves").get(0).getName());
        items.setStraps(summeryMap.get("straps").get(0).getName());
        items.setBackdetail(summeryMap.get("backDetails").get(0).getName());
        items.setEmbellishment(summeryMap.get("embellishment").get(0).getName());

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
