package com.example.sanjay.jinitaskapp.interfaces;

import com.example.sanjay.jinitaskapp.model.User;

import java.util.List;

public interface UserCallback {
    void onSuccess(List<User> userList);
    void onFailure(String msg);
}
