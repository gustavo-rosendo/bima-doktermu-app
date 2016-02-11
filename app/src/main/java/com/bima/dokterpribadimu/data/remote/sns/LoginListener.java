package com.bima.dokterpribadimu.data.remote.sns;

/**
 * Created by apradanas on 2/11/16.
 */
public interface LoginListener {
    void onSuccess();
    void onSignOut();
    void onFail();
    void onCancel();
}
