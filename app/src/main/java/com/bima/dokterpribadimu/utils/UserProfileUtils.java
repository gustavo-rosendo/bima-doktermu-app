package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.widget.Toast;

import com.bima.dokterpribadimu.R;
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

    /**
     * Validate phone number. A phone number is valid if it has at least 11 digits, like in: 02742112462
     * @return boolean true if phone number is valid, boolean false if otherwise
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        boolean isValid = false;

        if(phoneNumber.length() >= Constants.PHONE_NUMBER_MINIMAL_DIGITS) {
            isValid = true;
        }

        return isValid;
    }
}
