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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.CommonUtils;
import com.java1.fullsail.vestiruyaalpha.activity.core.Constant;
import com.java1.fullsail.vestiruyaalpha.activity.core.FileUtils;
import com.java1.fullsail.vestiruyaalpha.activity.model.User;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityRegistrationBinding;

import java.io.File;
import java.util.HashMap;

public class SignUpActivity extends BaseActivity {
    private final int PERMISSION_ALL = 100;
    private Uri fileUri;
    String userType;
    User user;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_registration);

        userType = getIntent().getStringExtra("userType");

        if (!userType.equals("Customers")) {
            binding.etAddress.setVisibility(View.GONE);
        }

        binding.createAccountBtn.setOnClickListener(this);
        binding.btnPickImage.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
//        getSupportActionBar().setTitle("Sign Up");
        //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.create_account_btn:
                boolean isValid = validateFields();
                if (isValid) {
                    mDialog.showCustomDalog();
                    mAuth.createUserWithEmailAndPassword(binding.RegEmail.getText().toString(), binding.RegPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            mDialog.closeDialog();
                            if (task.isSuccessful()) {
                                firebaseUser = mAuth.getCurrentUser();
                                if (task.isSuccessful()) {
                                    uploadFile(fileUri);
                                } else {
                                    Toast.makeText(mActivity, "Fails to send verification Email", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                if (task.getException() != null) {
                                    Toast.makeText(mActivity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                    });
                }
                break;
            case R.id.btnPickImage:
                if (!hasPermissions(mActivity, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
                } else {
                    selectImage();
                }
                break;

        }
    }

    private void storeUserData(final String downloadUrl) {

        mDialog.showCustomDalog();
        HashMap<String, Object> data = new HashMap<>();//are you login and then save data right?
        data.put("email", firebaseUser.getEmail());
        data.put("profilePic", downloadUrl);
        data.put("username", binding.etName.getText().toString());
        data.put("city", binding.etCity.getText().toString());
        data.put("password", binding.RegPassword.getText().toString());
        data.put("address", binding.etAddress.getText().toString());

        mDatabase.child(userType).child(firebaseUser.getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDatabase.child(userType).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            user.setKey(dataSnapshot.getKey());
                            user.setFirebaseId(firebaseUser.getUid());
                            BaseActivity.saveProfileData(SignUpActivity.this, user);
//                            CommonUtils.setBooleanPref(mActivity, Constant.SF_IS_LOGIN, true);
                            mDialog.closeDialog();
                            if (userType.equals(Constant.TAYLOR)) {
                                CommonUtils.redirectToActivity(mActivity, mActivity, TailorDetailActivity.class, false, null, 0);
                            } else if (userType.equals(Constant.CUSTOMER)) {
                                CommonUtils.redirectToActivity(mActivity, mActivity, CustomerDetailActivity.class, false, null, 0);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mDialog.closeDialog();

                        }
                    });
                }
            }
        });
    }


    boolean validateFields() {

        if (fileUri == null) {
            Toast.makeText(mActivity, "Please, select profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean isValidName = CommonUtils.validateForEmpty(binding.etName, mActivity, "This field is required");
        boolean isValidEmail = CommonUtils.validateForEmpty(binding.RegEmail, mActivity, "This field is required");
        boolean isValidPass = CommonUtils.validateForEmpty(binding.RegPassword, mActivity, "This field is required");
        boolean isValidCity = CommonUtils.validateForEmpty(binding.etCity, mActivity, "This field is required");

        boolean isValidAdd = true;
        if (userType.equals("Customers")) {
            isValidAdd = CommonUtils.validateForEmpty(binding.etAddress, mActivity, "This field is required");
        }

        if (isValidName && isValidEmail && isValidPass && isValidCity && isValidAdd) {
            if (CommonUtils.isValidEmail(binding.RegEmail.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();


//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            sendToMain();
//        }
    }

    private void sendToMain() {

        Intent mainintent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(mainintent);
        finish();
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
                    Toast.makeText(mActivity, "Please grant permission to access files to add the profile picture", Toast.LENGTH_SHORT).show();
                }
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
                        binding.profileImage.setImageBitmap(bitmap);
                        String newPath = CommonUtils.saveImage(mActivity, bitmap, Constant.PROFILE_IMAGE);
                        fileUri = FileUtils.getUri(new File(newPath));

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
                        binding.profileImage.setImageBitmap(bitmap);
                        String newPath = CommonUtils.saveImage(mActivity, bitmap, Constant.PROFILE_IMAGE);
                        fileUri = FileUtils.getUri(new File(newPath));

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
        String childName;
        if (userType.equals("Customers")) {
            childName = Constant.customers_profile_images;
        } else {
            childName = Constant.tailors_profile_images;
        }

        final StorageReference childRef = storageRef.child(childName).child(firebaseUser.getUid() + "_" + System.currentTimeMillis());
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
                    storeUserData(downloadUri);
                }
            }
        });
    }

}
