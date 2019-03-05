package com.java1.fullsail.vestiruyaalpha.activity.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.java1.fullsail.vestiruyaalpha.R;
import com.java1.fullsail.vestiruyaalpha.activity.core.PayPalConfig;
import com.java1.fullsail.vestiruyaalpha.activity.model.ReviewModel;
import com.java1.fullsail.vestiruyaalpha.activity.model.TailorJob;
import com.java1.fullsail.vestiruyaalpha.databinding.ActivityPaymentBinding;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentActivity extends BaseActivity {
    private ActivityPaymentBinding binding;
    private Activity mActivity;
    private ReviewModel reviewmodel;
    private TailorJob tailorJob;
    private static PayPalConfiguration config;
    public static final int PAYPAL_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_payment);
        tailorJob=(TailorJob)getIntent().getSerializableExtra("items");
        reviewmodel=new ReviewModel();
        binding.tvPrice.setText(tailorJob.getPrice());
        setUpClicks();

        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        startPayPalService();

    }

    private void startPayPalService() {

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
    }

    private void setUpClicks() {

        binding.rlDone.setOnClickListener(this);
        binding.rlPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.rlDone:

                //getPayment();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String currentDateandTime = sdf.format(new Date());

                if(binding.edtReview.getText().toString()!=null && !binding.edtReview.getText().toString().isEmpty())
                {

                    mDialog.showCustomDalog();
                    reviewmodel.setCustomerId(firebaseUser.getUid());
                    reviewmodel.setTailorId(tailorJob.getUserId());
                    reviewmodel.setMessage(binding.edtReview.getText().toString());
                    reviewmodel.setRating((long) binding.rating.getRating());
                    reviewmodel.setPhotoImage(tailorJob.getPics());
                    reviewmodel.setTimestamp(currentDateandTime);

                    mDatabase.child("Customers").child("Reviews").push().setValue(reviewmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                mDialog.closeDialog();
                                Intent i=new Intent(mActivity,OrderPreviewActivity.class);
                                i.putExtra("items",tailorJob);
                                i.putExtra("from","payment");
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                            }
                            else
                            {
                                mDialog.closeDialog();
                            }
                        }
                    });

                }
                else
                {

                    Toast.makeText(mActivity,"Please write review",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.rlPayment:

                getPayment();

                break;
        }
    }

    private void getPayment() {

        String paymentAmount = binding.tvPrice.getText().toString().replace("$","");

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Vestiruya",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this,com.paypal.android.sdk.payments.PaymentActivity.class );
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(8);

                        Intent i=new Intent(this,OrderPreviewActivity.class);
                        i.putExtra("items",tailorJob);
                        i.putExtra("from","payment");
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                } else {

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mActivity,"User cancel",Toast.LENGTH_LONG).show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {

                Toast.makeText(mActivity,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
