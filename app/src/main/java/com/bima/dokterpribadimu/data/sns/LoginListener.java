package com.bima.dokterpribadimu.data.sns;

import com.bima.dokterpribadimu.model.UserProfile;

/**
 * Created by apradanas on 2/11/16.
 */
public interface LoginListener {
    void onSuccess(UserProfile userProfile);
    void onSignOut();
    void onFail();
    void onCancel();
}
