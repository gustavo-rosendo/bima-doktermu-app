package com.bima.dokterpribadimu.data.inappbilling;

/**
 * Created by apradanas on 3/6/16.
 */
public interface QueryInventoryListener {
    void onSuccess(boolean isSubscribed);
    void onFailed();
}
