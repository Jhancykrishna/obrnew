package com.sayone.obr.security;

import com.sayone.obr.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 10800000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/signup";
    public static final String TOKEN_SECRET = "jdh45ft657";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}

