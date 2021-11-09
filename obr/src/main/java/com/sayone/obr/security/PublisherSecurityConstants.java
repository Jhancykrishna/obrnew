package com.sayone.obr.security;

import com.sayone.obr.SpringApplicationContext;

public class PublisherSecurityConstants {

    public static final long EXPIRATION_TIME = 864000000;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/publisher";

    public static String getTokenSecret() {

        PublisherAppProperties publisherAppProperties = (PublisherAppProperties) SpringApplicationContext.getBean("AppProperties");
        return publisherAppProperties.getTokenSecret();
    }
}
