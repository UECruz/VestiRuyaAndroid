package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.core.FileUtils;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityItemDescriptionBinding;

import java.io.File;
import java.util.HashMap;

public class JobDetailActivity extends BaseActivity {
    private ActivityItemDescriptionBinding binding;
    private String jobkey;
    TailorJob model;
    String type;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int PERMISSION_ALL = 100;
    private Uri fileUri;
    int photoFlag;
    StorageReference childRef;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_item_description);

        binding.appBar.setVisibility(View.VISIBLE);
        setSupportActionBar(binding.toolbar);
        setTitle("");

        jobkey = getIntent().getStringExtra("key");

        type = getIntent().getStringExtra("type");

        binding.rlCancel.setVisibility(View.GONE);
        binding.btnOk.setText("Done");

        if (type.equals("job")){
            binding.rlCancel.setVisibility(View.GONE);
            binding.btnOk.setVisibility(View.VISIBLE);
            binding.btnOkHistory.setVisibility(View.GONE);
            binding.tvtailorName.setVisibility(View.GONE);
            binding.tvDate.setVisibility(View.GONE);
            binding.label1.setVisibility(View.GONE);
            binding.label2.setVisibility(View.GONE);
            binding.iv1.setVisibility(View.GONE);
            binding.iv2.setVisibility(View.GONE);
            binding.iv3.setVisibility(View.GONE);
            binding.tvSelect1.setVisibility(View.GONE);
            binding.tvSelect2.setVisibility(View.GONE);
            binding.tvSelect3.setVisibility(View.GONE);
        }
        if (type.equals("history")) {
            binding.rlCancel.setVisibility(View.GONE);
            binding.btnOk.setVisibility(View.GONE);
            binding.btnOkHistory.setVisibility(View.GONE);
            binding.tvDate.setVisibility(View.VISIBLE);
            binding.tvtailorName.setVisibility(View.VISIBLE);
            binding.label1.setVisibility(View.VISIBLE);
            binding.label2.setVisibility(View.VISIBLE);
            binding.iv1.setVisibility(View.GONE);
            binding.iv2.setVisibility(View.GONE);
            binding.iv3.setVisibility(View.GONE);
            binding.tvSelect1.setVisibility(View.GONE);
            binding.tvSelect2.setVisibility(View.GONE);
            binding.tvSelect3.setVisibility(View.GONE);
        }

        getJobdata();
        setUpClicks();
    }

    private void getJobdata() {
        mDialog.showCustomDalog();
        mDatabase.child(Constant.TAYLOR).child("Job").child(jobkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDialog.closeDialog();
                model = dataSnapshot.getValue(TailorJob.class);


                displayData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mDialog.closeDialog();
                Log.d("error", databaseError.getMessage());
            }
        });
    }

    private void displayData() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);



        Glide.with(this).load(model.getPicUrl()).apply(options).into(binding.icImage);
        binding.tvDressType.setText(model.getItems().getDressType());
        binding.tvFabric.setText(model.getItems().getFabric());
        binding.tvBackDetails.setText(model.getItems().getBackDetail());
        binding.tvEmbelishment.setText(model.getItems().getEmbellish());
        binding.tvNeckline.setText(model.getItems().getNeckline());
        binding.tvSleeve.setText(model.getItems().getSlevee());
        binding.tvStrap.setText(model.getItems().getStrap());
        binding.tvUsername.setText(model.getName());
        binding.tvPrice.setText(model.getPrice());
        binding.tvtailorName.setText(user.getUsername());
        if(model.getDate()!=null)
            binding.tvDate.setText(model.getDate());
    }

    private void setUpClicks() {
        binding.btnOk.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);
        binding.icImage.setOnClickListener(this);
        binding.tvSelect1.setOnClickListener(this);
        binding.tvSelect2.setOnClickListener(this);
        binding.tvSelect3.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icBack:
                onBackPressed();
                break;
            case R.id.btnOkHistory:
                onBackPressed();
                break;
            case R.id.btnOk:
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                    return;
                } else {
                    selectImage();
                }

                break;

            case R.id.upload1:
                photoFlag = 1;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;
            case R.id.upload2:
                photoFlag = 2;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;
            case R.id.upload3:
                photoFlag = 3;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;

            case R.id.icImage:
                Intent i;
                if (!type.equals("history")) {
                    i = new Intent(mActivity, CustomerInfoActivity.class);
                }else {
                    i = new Intent(mActivity, CustomerInfoTailorActivity.class);
                }
                i.putExtra("customerId",model.getCustomerId());
                startActivity(i);
                break;
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(mActivity, "Please grant permission to access files to add the picture", Toast.LENGTH_SHORT).show();
                }
                //noinspection UnnecessaryReturnStatement
                return;
        }
    }

    /*open dialog for choose option*/
    public void selectImage() {

        final CharSequence[] options = {"Choose from Gallery", "Take Photo", "Cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Add Mount!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*open gallery*/
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
    }

    /*open camera*/
    private void openCamera() {
        File saveFilePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mActivity.getPackageName());
        if (!saveFilePath.exists()) {
            saveFilePath.mkdirs();
        }
        String fileName = "RepairAppImg_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        fileUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CommonUtils.printLog("TAG", "outputFileUri intent" + fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 1002);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1001) {
                try {//gallery
                    fileUri = data.getData();
                    String path = FileUtils.getPath(mActivity, fileUri);
                    Bitmap bitmap = CommonUtils.handleSamplingAndRotationBitmap(path);
                    if (bitmap != null) {
                        binding.icImage.setImageBitmap(bitmap);
                        String newPath = CommonUtils.saveImage(mActivity, bitmap, Constant.PROFILE_IMAGE);
                        fileUri = FileUtils.getUri(new File(newPath));
                        uploadFile(fileUri);
                    } else {
                        Toast.makeText(mActivity, "can't access this image please select another image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1002) {
                // camera
                try {
                    String path = FileUtils.getPath(mActivity, fileUri);
                    Bitmap bitmap = CommonUtils.handleSamplingAndRotationBitmap(path);
                    if (bitmap != null) {
                        binding.icImage.setImageBitmap(bitmap);
                        String newPath = CommonUtils.saveImage(mActivity, bitmap, Constant.PROFILE_IMAGE);
                        fileUri = FileUtils.getUri(new File(newPath));
                        uploadFile(fileUri);
                    } else {
                        Toast.makeText(mActivity, "can't access this image please select another image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }

    private void uploadFile(Uri fileUri) {

        mDialog.showCustomDalog();


        final StorageReference childRef = storageRef.child("jobimages").child(firebaseUser.getUid() + "_" + System.currentTimeMillis());
        UploadTask uploadTask = childRef.putFile(fileUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    mDialog.closeDialog();
                    throw task.getException();
                }
                return childRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                mDialog.closeDialog();
                if (task.isSuccessful()) {
                    String downloadUri = String.valueOf(task.getResult());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("isConfimed", true);
                    hashMap.put("picUrl", downloadUri);

                    mDatabase.child(Constant.TAYLOR).child("Job").child(jobkey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.closeDialog();
                            if (task.isSuccessful()) {
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

}
