package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.model.ItemModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.ItemModel2;
import com.java1.fullsail.vestiruyaalpha.activity.model.JobModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityItemDescriptionBinding;

import java.util.HashMap;

public class ItemDescriptionActivity extends BaseActivity {

    private ActivityItemDescriptionBinding binding;
    private Activity mActivity;
    private ItemModel model;
    private String jobkey;
    private User user;
    private JobModel jobmodel;
    ImageView icImage;
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_item_description);
        model=(ItemModel) getIntent().getSerializableExtra("item");

        jobkey=getIntent().getStringExtra("key");
        jobmodel=(JobModel)getIntent().getSerializableExtra("jobmodel");

        binding.tvUsername.setText(jobmodel.getUsername());

        double price=Double.valueOf(jobmodel.getPrice());
        binding.tvPrice.setText("$"+ String.format("%.2f", price));
        binding.btnOkHistory.setVisibility(View.GONE);
        binding.label1.setVisibility(View.GONE);
        binding.label2.setVisibility(View.GONE);
        binding.iv1.setVisibility(View.GONE);
        binding.iv2.setVisibility(View.GONE);
        binding.iv3.setVisibility(View.GONE);
        binding.tvSelect1.setVisibility(View.GONE);
        binding.tvSelect2.setVisibility(View.GONE);
        binding.tvSelect3.setVisibility(View.GONE);
        binding.tvDressType.setText(model.getBodytype());
        binding.tvFabric.setText(model.getFabric());
        binding.tvBackDetails.setText(model.getBackdetail());
        binding.tvEmbelishment.setText(model.getEmbellishment());
        binding.tvNeckline.setText(model.getNeckline());
        binding.tvSleeve.setText(model.getSleeves());
        binding.tvStrap.setText(model.getStraps());
        binding.icImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mActivity,CustomerInfoTailorActivity.class);
                i.putExtra("customerId",jobmodel.getUserid());
                startActivity(i);
            }
        });
        binding.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mActivity,CustomerInfoTailorActivity.class);
                i.putExtra("customerId",jobmodel.getUserid());
                startActivity(i);
            }
        });
        user=getProfileData(mActivity);
        getFabric();
        setUpClicks();

    }

    private void getFabric() {


    }

    private void setUpClicks() {

        binding.btnOk.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.btnOk:

                mDialog.showCustomDalog();
                mDatabase.child("Customers").child("Orders").child(jobkey).child("interestsShown").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mDialog.closeDialog();
                        dataSnapshot.getValue();
                        HashMap<String,Object> result=new HashMap<>();
                        result.put("isJobAccepted",true);
                        result.put("tailorID",firebaseUser.getUid());
                        result.put("tailorLoc",user.getAddress()+" "+user.getCity());
                        result.put("tailorPic",user.getProfilePic());
                        result.put("tailorname",user.getUsername());

                        if (dataSnapshot.getValue()!= null) {
                            mDatabase.child("Customers").child("Orders").child(jobkey).child("interestsShown").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(result);
                        } else {
                            mDatabase.child("Customers").child("Orders").child(jobkey).child("interestsShown").child("0").setValue(result);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                TailorJob tailorjob=new TailorJob();
                tailorjob.setAccepted(true);
                tailorjob.setConfimed(false);
                tailorjob.setCustomerId(jobmodel.getUserid());
                tailorjob.setUserId(firebaseUser.getUid());
                tailorjob.setPrice(jobmodel.getPrice());
                tailorjob.setPics(jobmodel.getPhotoImage());
                tailorjob.setName(jobmodel.getUsername());
                tailorjob.setDate(jobmodel.getDate());

                ItemModel2 items=new ItemModel2();
                items.setBackDetail(model.getBackdetail());
                items.setDressType(model.getBodytype());
                items.setEmbellish(model.getEmbellishment());
                items.setFabric(model.getFabric());
                items.setNeckline(model.getNeckline());
                items.setSlevee(model.getSleeves());
                items.setStrap(model.getStraps());

                tailorjob.setItems(items);


                mDatabase.child("Tailors").child("Job").push().setValue(tailorjob).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {

                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });


                break;


            case R.id.btnCancel:


                finish();

                break;

        }
    }

}
