package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.Manifest;
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
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.core.FileUtils;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityEditProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class EditProfileActivity extends BaseActivity {
    ActivityEditProfileBinding binding;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int PERMISSION_ALL = 100;
    private Uri fileUri;
    private String name,address,email,password;
    private String userType;
    String downloadUri;
    private User usermodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_edit_profile);
        userType=CommonUtils.getStringSharedPref(mActivity,Constant.SF_Type,"");
        getData();
        setUpClicks();
        getData(1);
        if(user.getProfilePic()!=null && !user.getProfilePic().isEmpty())
        {

                   /* Glide.with(this).placeholder(R.drawable.placeholder)
                            .load(user.getProfilePic())// image url
                            .into(binding.icProfile);*/

            //Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_placeholder).into(binding.icProfile);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);

            Glide.with(mActivity).load(user.getProfilePic()).apply(options).into(binding.profileImage);
        }
    }

    private void getData() {

        usermodel=getProfileData(mActivity);
        binding.edtEmail.setText(usermodel.getEmail());
    }

    private void setUpClicks() {

        binding.icBack.setOnClickListener(this);
        binding.rlSave.setOnClickListener(this);
        binding.rlSelect.setOnClickListener(this);
        binding.upload1.setOnClickListener(this);
        binding.upload2.setOnClickListener(this);
        binding.upload3.setOnClickListener(this);
    }

    int photoFlag;

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.rlSave:

                boolean isValid=validateField();


                if(isValid)
                {
                    updatedata();
                }

                break;

            case R.id.rlSelect:
                photoFlag = 1;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {

                    selectImage();
                }
                break;
            case R.id.upload1:
                photoFlag = 2;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;
            case R.id.upload2:
                photoFlag = 3;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;
            case R.id.upload3:
                photoFlag = 4;
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;

            case R.id.icBack:


                finish();

                break;

        }

    }

    private boolean validateField() {


        if(fileUri==null)
        {
            Toast.makeText(mActivity,"Please upload photo",Toast.LENGTH_LONG).show();
            return false;
        }

        if(binding.edtName.getText().toString()==null || binding.edtName.getText().toString().isEmpty())
        {

            Toast.makeText(mActivity,"Please enter name",Toast.LENGTH_LONG).show();
            return false;
        }
        if(binding.edtAddress.getText().toString()==null || binding.edtAddress.getText().toString().isEmpty())
        {
            Toast.makeText(mActivity,"Please enter address",Toast.LENGTH_LONG).show();
            return false;
        }
        if(binding.edtPassword.getText().toString()==null || binding.edtPassword.getText().toString().isEmpty())
        {
            Toast.makeText(mActivity,"Please enter password",Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void updatedata() {


        mDialog.showCustomDalog();
        AuthCredential credential = EmailAuthProvider
                .getCredential(usermodel.getEmail(), usermodel.getPassword());

        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseUser.updatePassword(binding.edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.closeDialog();
                            if (task.isSuccessful()) {

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("username", binding.edtName.getText().toString());
                                result.put("email",binding.edtEmail.getText().toString());
                                result.put("address", binding.edtAddress.getText().toString()+binding.edtAddress2.getText().toString());
                                result.put("password",binding.edtPassword.getText().toString());
                                result.put("profilePic",downloadUri);

                                mDatabase.child(userType).child(firebaseUser.getUid()).updateChildren(result);

                                //reference.child(userType).child(firebaseUser.getUid()).updateChildren(result);
                                Intent i=new Intent();
                                i.putExtra("username",binding.edtName.getText().toString());
                                i.putExtra("email",binding.edtEmail.getText().toString());
                                i.putExtra("address",binding.edtAddress.getText().toString());
                                i.putExtra("password",binding.edtPassword.getText().toString());
                                i.putExtra("pic",downloadUri);

                                Toast.makeText(mActivity,"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK,i);
                                finish();

                            } else {

                                Toast.makeText(mActivity,"Error while update password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    mDialog.closeDialog();
                    Toast.makeText(mActivity,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });

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

    /*permission result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    Toast.makeText(mActivity, "Please grant permission to access files to add the profile picture", Toast.LENGTH_SHORT).show();
                }
                //noinspection UnnecessaryReturnStatement
                return;
        }
    }

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
        String fileName = "VestiRuyaAppImg_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        fileUri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        CommonUtils.printLog("TAG", "outputFileUri intent" + fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 1002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1001) {
                try {//gallery
                    fileUri = data.getData();
                    String path = FileUtils.getPath(mActivity, fileUri);
                    Bitmap bitmap = CommonUtils.handleSamplingAndRotationBitmap(path);
                    if (bitmap != null) {
                        if (photoFlag==1){
                            binding.profileImage.setImageBitmap(bitmap);
                        }
                        if (photoFlag==2){
                            binding.iv1.setImageBitmap(bitmap);
                        }
                        if (photoFlag==3){
                            binding.iv2.setImageBitmap(bitmap);
                        }
                        if (photoFlag==4){
                            binding.iv3.setImageBitmap(bitmap);
                        }
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
                        if (photoFlag==1){
                            binding.profileImage.setImageBitmap(bitmap);
                        }
                        if (photoFlag==2){
                            binding.iv1.setImageBitmap(bitmap);
                        }
                        if (photoFlag==3){
                            binding.iv2.setImageBitmap(bitmap);
                        }
                        if (photoFlag==4){
                            binding.iv3.setImageBitmap(bitmap);
                        }
                        String newPath = CommonUtils.saveImage(mActivity, bitmap, Constant.PROFILE_IMAGE);
                        fileUri = FileUtils.getUri(new File(newPath));
                        uploadFile(fileUri);
                        // uploadFile(fileUri);
                    } else {
                        Toast.makeText(mActivity, "can't access this image please select another image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }

    StorageReference childRef;

    private void uploadFile(Uri fileUri) {

        mDialog.showCustomDalog();
        String childName,childName1;
        if (userType.equals("Customers")) {
            childName = Constant.customers_profile_images;
            childName1 = Constant.customers_sample_images;
        } else {
            childName = Constant.tailors_profile_images;
            childName1 = Constant.tailors_sample_images;
        }
        if (photoFlag==1){
            childRef = storageRef.child(childName).child(firebaseUser.getUid() + "_" + System.currentTimeMillis());
        }
        if (photoFlag==2){
            childRef = storageRef.child(childName1).child(firebaseUser.getUid() + "_File_" + 1);
        }
        if (photoFlag==3){
            childRef = storageRef.child(childName1).child(firebaseUser.getUid() + "_File_" + 2);
        }
        if (photoFlag==4){
            childRef = storageRef.child(childName1).child(firebaseUser.getUid() + "_File_" + 3);
        }

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
                    downloadUri = String.valueOf(task.getResult());
                    user.setProfilePic(downloadUri);
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("profilePic",downloadUri);
                    mDatabase.child(userType).child(firebaseUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful())
                            {

                            } else {

                            }
                        }
                    });
                }
            }
        });
    }
    private void getData(final int i) {
        String childName,childName1;
        if (userType.equals("Customers")) {
            childName = Constant.customers_profile_images;
            childName1 = Constant.customers_sample_images;
        } else {
            childName = Constant.tailors_profile_images;
            childName1 = Constant.tailors_sample_images;
        }
        storageRef.child(childName1+"/" + firebaseUser.getUid() + "_File_" + i).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
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
    private void nextScreen()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child(userType).child(firebaseUser.getUid()).child("Job");

        //final Query query = reference.child(userType).child(firebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                String path = "/" + dataSnapshot.getKey() + "/" + key;
                HashMap<String, Object> result = new HashMap<>();
                result.put("Status", "COMPLETED");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
