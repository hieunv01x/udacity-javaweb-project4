package com.example.demo.security;

public class SecurityConstants {
    public static final String SECRET = "secret_key";
    public static final long EXPIRED_TIME = 86_400_000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
}
