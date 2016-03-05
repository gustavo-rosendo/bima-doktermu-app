package com.bima.dokterpribadimu.data.inappbilling;

/**
 * Created by apradanas on 3/2/16.
 */
public interface BillingListener {
    void onSuccess();
    void onCancel();
    void onFailed();
}
