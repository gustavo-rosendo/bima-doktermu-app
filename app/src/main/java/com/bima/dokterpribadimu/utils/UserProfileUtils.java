package com.bima.dokterpribadimu.utils;

import android.content.Context;

import com.bima.dokterpribadimu.model.UserProfile;

/**
 * Created by apradanas on 2/21/16.
 */
public class UserProfileUtils {

    public static UserProfile getUserProfile(Context context) {
        return GsonUtils.fromJson(
                StorageUtils.getString(context, Constants.KEY_USER_PROFILE, ""),
                UserProfile.class
        );
    }
}
