package com.java1.fullsail.vestiruyaalpha.activity.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.java1.fullsail.vestiruyaalpha.R;

import java.util.Objects;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        getWindow().setDimAmount(0f);
        setCancelable(false);
    }

    public void showCustomDalog() {
        try {
            if (!isShowing()) {
                show();
            }
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
    }

    public void closeDialog() {
        try {
            dismiss();
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
    }
}