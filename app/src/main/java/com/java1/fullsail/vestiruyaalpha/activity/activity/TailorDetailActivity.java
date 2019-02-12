package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.core.FileUtils;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityTailorDetailBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TailorDetailActivity extends BaseActivity {
    ActivityTailorDetailBinding binding;
    private final int PERMISSION_ALL = 100;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private Uri fileUri;
    HashMap<Integer, String> hashMap = new HashMap<>();
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_tailor_detail);
        setListeners();
        setUpdata();
    }

    @SuppressLint("SetTextI18n")
    private void setUpdata() {
        binding.tvUserName.setText("Username: " + user.getUsername());
        binding.tvEmail.setText("Email: " + user.getEmail());
        binding.tcPass.setText("Password: " + user.getPassword());
        binding.tvCity.setText("City,State: " + user.getCity());
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.banner).error(R.drawable.banner).into(binding.ivProfile);
    }

    private void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.tvSelect1.setOnClickListener(this);
        binding.tvSelect2.setOnClickListener(this);
        binding.tvSelect3.setOnClickListener(this);
        binding.tvBack.setOnClickListener(this); //tailor sample imgs
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                boolean isValid = validateFields();
                if (isValid) {
                    addToStorage();
                }
                break;
            case R.id.tvSelect1:
                index = 1;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    openGallery();
                }
                break;
            case R.id.tvSelect2:
                index = 2;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    openGallery();
                }
                break;
            case R.id.tvSelect3:
                index = 3;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    openGallery();
                }
                break;
        }
    }

    private void addToStorage() {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            uploadFile(pair);
        }
    }

    private void uploadFile(final Map.Entry pair) {
        mDialog.show();

        final StorageReference childRef = storageRef.child(Constant.tailors_sample_images).child(firebaseUser.getUid() + "_File_" + pair.getKey());
        UploadTask uploadTask = childRef.putFile(Uri.fromFile(new File(pair.getValue().toString())));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    mDialog.dismiss();
                    throw task.getException();
                }
                return childRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    if (pair.getKey().equals(3)) {
                        CommonUtils.setBooleanPref(mActivity, Constant.SF_IS_LOGIN, true);
                        Intent i = new Intent(mActivity,TailorHomeScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                }
            }
        });
    }

    private boolean validateFields() {
        if (hashMap.size() > 2 && hashMap.containsKey(1) && hashMap.containsKey(2) && hashMap.containsKey(3)) {
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(mActivity, "Please grant permission to access files to add images", Toast.LENGTH_SHORT).show();
                }
                return;
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

    /*open gallery*/
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
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
                        if (index == 1) {
                            binding.iv1.setImageBitmap(bitmap);
                        } else if (index == 2) {
                            binding.iv2.setImageBitmap(bitmap);
                        } else if (index == 3) {
                            binding.iv3.setImageBitmap(bitmap);
                        }
                        hashMap.put(index, path);

                    } else {
                        Toast.makeText(mActivity, "can't access this image please select another image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
