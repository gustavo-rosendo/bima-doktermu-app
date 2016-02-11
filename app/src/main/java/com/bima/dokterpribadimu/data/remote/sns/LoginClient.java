package com.bima.dokterpribadimu.data.remote.sns;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by apradanas on 2/11/16.
 */
public interface LoginClient {
    void init(Activity activity, LoginListener loginListener);
    void onStart();
    void onStop();
    void onActivityResult(int requestCode, int responseCode, Intent data);
    void signIn();
    void signOut();
}
