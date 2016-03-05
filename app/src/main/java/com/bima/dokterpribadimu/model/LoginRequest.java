package com.bima.dokterpribadimu.model;

/**
 * Created by apradanas on 3/5/16.
 */
public class LoginRequest {

    private String email;
    private String password;
    private String login_type;

    public LoginRequest(String email, String password, String login_type) {
        this.email = email;
        this.password = password;
        this.login_type = login_type;
    }
}
