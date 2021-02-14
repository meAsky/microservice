package com.contains.user.response;

/**
 * @date: 2021-02-14 17:03
 **/
public class LoginResponse extends Response{

    private String token;

    public LoginResponse(String token) {
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
