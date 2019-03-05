package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityOrderPreviewBinding;
import com.squareup.picasso.Picasso;

public class OrderPreviewActivity extends BaseActivity {

    private ActivityOrderPreviewBinding binding;
    private Activity mActivity;
    private String from;
    private TailorJob tailorJob;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=OrderPreviewActivity.this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_order_preview);

        tailorJob=(TailorJob)getIntent().getSerializableExtra("items");
        from=getIntent().getStringExtra("from");

        if(from.equals("tailorinfo"))
        {
            getData(1);
            binding.tvDate.setVisibility(View.GONE);
            binding.tvtailorName.setVisibility(View.GONE);
            binding.label1.setVisibility(View.GONE);
            binding.label2.setVisibility(View.GONE);
        }
        else if(from.equals("payment"))
        {

            binding.tvHeading.setText("Thank you");
            binding.llScrollview.setVisibility(View.VISIBLE);
            binding.llyesno.setVisibility(View.GONE);
            binding.rlOk.setVisibility(View.VISIBLE);
            binding.toolbar.setVisibility(View.GONE);
            binding.tvDate.setVisibility(View.VISIBLE);
            binding.tvtailorName.setVisibility(View.VISIBLE);
            binding.label1.setVisibility(View.VISIBLE);
            binding.label2.setVisibility(View.VISIBLE);
            getData(1);
        }

        setUpClicks();
        setUpData();



    }

    private void setUpData() {

        binding.tvDressType.setText(tailorJob.getItems().getDressType());
        binding.tvFabric.setText(tailorJob.getItems().getFabric());
        binding.tvBackDetails.setText(tailorJob.getItems().getBackDetail());
        binding.tvEmbelishment.setText(tailorJob.getItems().getEmbellish());
        binding.tvNeckline.setText(tailorJob.getItems().getNeckline());
        binding.tvSleeve.setText(tailorJob.getItems().getSlevee());
        binding.tvStrap.setText(tailorJob.getItems().getStrap());
        binding.tvtailorName.setText(tailorJob.getName());
        binding.tvDate.setText(tailorJob.getDate());

    }

    private void setUpClicks() {

        binding.icBack.setOnClickListener(this);

        if(from.equals("tailorinfo")) {
            binding.btnYes.setOnClickListener(this);
            binding.btnNo.setOnClickListener(this);
        }
        else if(from.equals("payment"))
        {
            binding.btnOk.setOnClickListener(this);
        }
    }

    private void getData(final int i) {
        mDialog.showCustomDalog();
        storageRef.child("tailors_sample_images/" + tailorJob.getUserId() + "_File_" + i).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                mDialog.closeDialog();
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.icBack:

                finish();
                break;

            case R.id.btnYes:
                Intent i=new Intent(mActivity,PaymentActivity.class);
                i.putExtra("items",tailorJob);
                startActivity(i);

                break;

            case R.id.btnNo:

                finish();
                break;

            case R.id.btnOk:

                i=new Intent(mActivity,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }
}
