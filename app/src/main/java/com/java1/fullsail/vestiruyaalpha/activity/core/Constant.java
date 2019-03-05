package com.java1.fullsail.vestiruyaalpha.activity.core;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;

public class Constant {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static boolean IS_DEBUG = true;
    public static final String SF_IS_TOKEN_SENT = "sf_is_token_sent";
    public static final String SF_IS_LOGIN = "sf_is_login";
    public static final String SF_IS_SIM_INFO_UPLOAD = "sf_is_sim_info_upload";
    public static final String SF_CALLER = "caller";
    public static final String SF_PROFILE = "sf_profile";
    public static final String SF_Type = "Type";

    public static final String CUSTOMER = "Customers";
    public static final String TAYLOR = "Tailors";
    public static final String MATERIAL = "Materials";
    public static final String ORDERS = "Orders";

    public static final String Silhouette = "Silhouette";
    public static final String Fabrics = "Fabrics";
    public static final String Neckline = "Neckline";
    public static final String Sleeves = "Sleeves";
    public static final String Straps = "Straps";
    public static final String Back_Details = "Back Details";
    public static final String Embellishment = "Embellishment";

    public static final String customers_profile_images = "customers_profile_images";
    public static final String tailors_profile_images = "tailors_profile_images";

    public static final String tailors_sample_images = "tailors_sample_images";
    public static final String customers_sample_images = "customers_sample_images";

    public static final String orders_images = "orders_images";

    public static final String Measurement = "Measurement";



    public static boolean isAdded = false;
    public static final String PROFILE_IMAGE = "Profile_Image";

    public static SharedPreferences m_sharedPreference;
    public static SharedPreferences.Editor m_sharedPrefEditor;


    public static final String CHATS = "Chats";
}
