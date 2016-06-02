package com.bima.dokterpribadimu.data.sns.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.data.sns.LoginClient;
import com.bima.dokterpribadimu.data.sns.LoginListener;
import com.bima.dokterpribadimu.model.FacebookProfile;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.TokenUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by apradanas on 2/11/16.
 */
public class FacebookClient implements LoginClient {

    private static final List<String> PERMISSIONS = Arrays.asList("public_profile", "email", "user_friends");

    private Activity activity;
    private LoginListener loginListener;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken mAccessToken;
    private ProfileTracker profileTracker;

    private static FacebookClient instance;

    public static FacebookClient getInstance() {
        if (instance == null) {
            instance = new FacebookClient();
        }
        return instance;
    }

    public static void release() {
        if (instance != null) {
            instance.activity = null;
        }
    }

    @Override
    public void init(Activity activity, final LoginListener loginListener) {
        this.activity = activity;
        this.loginListener = loginListener;

        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        if (loginListener != null) {
                            loginListener.onCancel();
                        }
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (loginListener != null) {
                            loginListener.onFail();
                        }
                    }
                });
    }

    private void getUserProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        FacebookProfile fbProfile = GsonUtils.fromJson(object.toString(), FacebookProfile.class);

                        if (fbProfile.getId() == null) {
                            loginListener.onFail();
                            return;
                        }

                        String picture = "https://graph.facebook.com/" + fbProfile.getId() + "/picture?type=large";
                        String email = fbProfile.getEmail() != null ? fbProfile.getEmail() : "";

                        UserProfile userProfile = UserProfileUtils.getUserProfile(
                                DokterPribadimuApplication.getInstance().getApplicationContext());
                        if(userProfile == null) {
                            userProfile = new UserProfile(
                                    fbProfile.getId(),
                                    fbProfile.getName(),
                                    fbProfile.getFirstName(),
                                    fbProfile.getLastName(),
                                    email,
                                    picture,
                                    "",
                                    "",
                                    Constants.LOGIN_TYPE_FACEBOOK,
                                    0.0,
                                    0.0,
                                    TokenUtils.generateToken(
                                            fbProfile.getEmail() + System.currentTimeMillis())
                            );
                        }
                        else {
                            userProfile.setId(fbProfile.getId());
                            userProfile.setName(fbProfile.getName());
                            userProfile.setFirstName(fbProfile.getFirstName());
                            userProfile.setLastName(fbProfile.getLastName());
                            userProfile.setEmail(email);
                            userProfile.setPicture(picture);
                            userProfile.setLoginType(Constants.LOGIN_TYPE_FACEBOOK);
                            userProfile.setAccessToken(TokenUtils.generateToken(
                                    fbProfile.getEmail() + System.currentTimeMillis()));
                        }

                        loginListener.onSuccess(userProfile);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name, last_name, email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn() {
        LoginManager.getInstance().logInWithReadPermissions(activity, PERMISSIONS);
    }

    @Override
    public void signOut() {
        LoginManager.getInstance().logOut();

        if (loginListener != null) {
            loginListener.onSignOut();
        }
    }
}
