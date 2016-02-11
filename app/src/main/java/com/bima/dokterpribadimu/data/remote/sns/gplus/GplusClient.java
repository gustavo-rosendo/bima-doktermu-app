package com.bima.dokterpribadimu.data.remote.sns.gplus;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.app.Activity;
import android.content.Intent;

import com.bima.dokterpribadimu.data.remote.sns.LoginClient;
import com.bima.dokterpribadimu.data.remote.sns.LoginListener;

/**
 * Created by apradanas on 2/12/16.
 */
public class GplusClient implements LoginClient, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;

    private Activity activity;
    private LoginListener loginListener;

    private GoogleApiClient googleApiClient;

    private static GplusClient instance;

    public static GplusClient getInstance() {
        if (instance == null) {
            return new GplusClient();
        }
        return instance;
    }

    public static void release() {
        if (instance != null) {
            instance.activity = null;
            instance = null;
        }
    }

    @Override
    public void init(Activity activity, LoginListener loginListener) {
        this.activity = activity;
        this.loginListener = loginListener;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            loginListener.onSuccess();
        } else {
            // Signed out, show unauthenticated UI.
            loginListener.onFail();
        }
    }

    @Override
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (loginListener != null) {
                            loginListener.onSignOut();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
