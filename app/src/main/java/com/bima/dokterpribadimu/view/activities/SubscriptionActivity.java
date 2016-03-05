package com.bima.dokterpribadimu.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.databinding.ActivitySubscriptionBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.iabutil.IabHelper;
import com.bima.dokterpribadimu.utils.iabutil.IabResult;
import com.bima.dokterpribadimu.utils.iabutil.Purchase;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

import javax.inject.Inject;

public class SubscriptionActivity extends BaseActivity {

    @Inject
    BillingClient billingClient;

    private ActivitySubscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        initViews();
    }

    private void initBillingClient() {
        billingClient.setBillingInitializationListener(new BillingInitializationListener() {
            @Override
            public void onSuccess() {
                billingClient.queryInventoryAsync();
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.setQueryInventoryListener(new QueryInventoryListener() {
            @Override
            public void onSuccess(boolean isSubscribed) {
                StorageUtils.putBoolean(
                        SubscriptionActivity.this,
                        Constants.KEY_USER_SUBSCIPTION,
                        isSubscribed);
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.init(this);
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.subscriptionSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!billingClient.isSubscribedToDokterPribadiKu()) {
                    billingClient.launchSubscriptionPurchaseFlow(new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            if (result.isSuccess()) {
                                showSuccessDialog(
                                        R.drawable.ic_dialog_success,
                                        getString(R.string.dialog_success),
                                        getString(R.string.dialog_subscription_success_message),
                                        getString(R.string.dialog_book_first_call),
                                        new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                            @Override
                                            public void onClick(DokterPribadimuDialog dialog) {
                                                startBookCallActivity();
                                                finish();
                                            }
                                        }
                                );
                            } else {
                                showErrorDialog(
                                        R.drawable.ic_bug,
                                        getString(R.string.dialog_failed),
                                        getString(R.string.dialog_sign_in_failed_message),
                                        getString(R.string.dialog_try_once_more),
                                        null);
                            }
                        }
                    });
                }
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.province_arrays)) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); // Hint to be displayed
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount() - 1; // don't display last item. It is used as hint.
                    }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.subscriptionProvinceSpinner.setAdapter(spinnerAdapter);
        binding.subscriptionProvinceSpinner.setSelection(spinnerAdapter.getCount());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initBillingClient();
    }

    @Override
    protected void onDestroy() {
        billingClient.release();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingClient.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
