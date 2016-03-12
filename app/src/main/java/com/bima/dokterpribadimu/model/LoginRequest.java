package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas on 3/5/16.
 */
public class LoginRequest {

    private String email;
    private String password;
    private String login_type;
    private UserProfile userProfile;

    public LoginRequest(String email, String password, String login_type, UserProfile userProfile) {
        this.email = email;
        this.password = password;
        this.login_type = login_type;
        this.userProfile = userProfile;
    }
}
