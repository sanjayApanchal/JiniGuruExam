package com.example.sanjay.jinitaskapp.interfaces;

import com.example.sanjay.jinitaskapp.model.UserDetail;


public interface UserDetailCallback {

    void onSuccess(UserDetail userDetail);
    void onFailure(String msg);
}
