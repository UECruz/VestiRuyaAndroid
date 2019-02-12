package com.java1.fullsail.vestiruyaalpha.activity.core;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class CommonUtils {

    public static void redirectToActivity(Context c, Activity from, Class to, boolean isFinish, Bundle b, int flag) {

        if (from != null) {
            Intent i = new Intent(from, to);
            if (b != null) {
                i.putExtras(b);
            }
            if (flag != 0) {
                i.addFlags(flag);
            }
            if (isFinish) {
                from.startActivity(i);
                from.finish();

            } else {
                from.startActivity(i);
            }

        } else {
            Intent i = new Intent(c, to);
            if (b != null) {
                i.putExtras(b);
            }
            if (flag != 0) {
                i.addFlags(flag);
            }

            if (isFinish) {
                c.startActivity(i);
                ((Activity) c).finish();


            } else {
                c.startActivity(i);
            }
        }
    }


    public static String SHARED_PREF_NAME = "SHARED_PREFS_Pinkoo";

    public static void setSharedPreference(Context p_context) {
        if (Constant.m_sharedPreference == null)
            Constant.m_sharedPreference = p_context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }


    public static void setEditor(Context p_context) {

        setSharedPreference(p_context);
        if (Constant.m_sharedPrefEditor == null)
            Constant.m_sharedPrefEditor = Constant.m_sharedPreference.edit();
    }



    public static void setStringSharedPref(Context p_context, String p_spKey, String p_value) {

        setEditor(p_context);
        Constant.m_sharedPrefEditor.putString(p_spKey, p_value);
        Constant.m_sharedPrefEditor.commit();
    }

    public static void setBooleanPref(Context p_context, String p_spKey, boolean p_value) {
        setEditor(p_context);
        Constant.m_sharedPrefEditor.putBoolean(p_spKey, p_value);
        Constant.m_sharedPrefEditor.commit();
    }



    public static String getStringSharedPref(Context p_context, String p_spKey, String p_value) {

        setSharedPreference(p_context);
        return Constant.m_sharedPreference.getString(p_spKey, p_value);
    }

    public static boolean getBooleanSharedPref(Context p_context, String p_spKey, boolean p_value) {

        setSharedPreference(p_context);
        return Constant.m_sharedPreference.getBoolean(p_spKey, p_value);
    }

    public static Bitmap handleSamplingAndRotationBitmap(String selectedImage) {
        int MAX_HEIGHT = 800;
        int MAX_WIDTH = 800;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImage, options);

        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        options.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeFile(selectedImage, options);
        if (selectedImage != null)
            img = rotateImageIfRequired(img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) {

        try {
            ExifInterface ei = new ExifInterface(selectedImage);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static String saveImage(Context context, Bitmap bitmap, String dirName) {
        String filePah = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getPackageName() + File.separator + dirName;
        if (!new File(filePah).exists()) {
            new File(filePah).mkdirs();
        }

        Calendar calendar = Calendar.getInstance();
        String filename = File.separator + "RepairAppIMG__" + calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND) + ".jpg";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePah + filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return filePah + filename;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePah + File.separator + filename;
    }



    public static void requestFocus(View p_view, Context p_Context) {
        if (p_view.requestFocus()) {
            ((Activity) p_Context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean validateForEmpty(EditText p_editText, Context p_context, String p_string) {
        if (p_editText.getText().toString().trim().isEmpty()) {
            p_editText.setError(p_string);
            requestFocus(p_editText, p_context);
            return false;
        } else {
            p_editText.setError(null);
        }

        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static void printLog(String tag, String message) {
        if (Constant.IS_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void clearSharedPrefs(Context context) {


        setEditor(context);
        Constant.m_sharedPrefEditor
                .remove(Constant.SF_Type)
                .remove(Constant.SF_PROFILE)
                .remove(Constant.SF_IS_LOGIN)
                .remove(Constant.SF_CALLER)
                .remove(Constant.SF_IS_TOKEN_SENT)
                .remove(Constant.SF_IS_SIM_INFO_UPLOAD)
                .commit();
    }
}
