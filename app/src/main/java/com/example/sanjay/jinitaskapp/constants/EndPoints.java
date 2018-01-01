package com.example.sanjay.jinitaskapp.constants;



public class EndPoints {
    public static final String URL_ROOT = "https://api.github.com/users";
    public static final String URL_ALL_USERS = URL_ROOT + "?since=%d";
    public static final String URL_SINGLE_USER = URL_ROOT + "/%s";
}
