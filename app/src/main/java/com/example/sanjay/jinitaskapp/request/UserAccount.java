package com.example.sanjay.jinitaskapp.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sanjay.jinitaskapp.AppController;
import com.example.sanjay.jinitaskapp.constants.EndPoints;
import com.example.sanjay.jinitaskapp.interfaces.UserCallback;
import com.example.sanjay.jinitaskapp.interfaces.UserDetailCallback;
import com.example.sanjay.jinitaskapp.model.User;
import com.example.sanjay.jinitaskapp.model.UserDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;



public class UserAccount {
    private static final String TAG = UserAccount.class.getSimpleName();



    public static void getAllUsers(final Context context,int since ,final UserCallback mListener){
        final String url = String.format(EndPoints.URL_ALL_USERS, since);

        Log.e(TAG, "Users : " + url);


                BaseRequest baseRequest = new BaseRequest(url, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        List<User> userList;
                        Log.e(TAG, response.toString());
                        Gson gson = new Gson();

                        userList =  gson.fromJson(response.toString(),new TypeToken<ArrayList<User>>(){}.getType());

                        Log.e(TAG, "User List Size: "+userList.size());

                        mListener.onSuccess(userList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"Error: "+ error.getMessage());
                        mListener.onFailure(error.getMessage());
                    }
                });

                AppController.getInstance().addToRequestQueue(baseRequest);
            }





    public static void getSingleUser(final Context context, String username, final UserDetailCallback mListener){
        final String url = String.format(EndPoints.URL_SINGLE_USER, username);
        Log.e(TAG, "User Detail : " + url);

        BaseRequest baseRequest = new BaseRequest(url, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
               UserDetail userDetail;
                Log.e(TAG,response.toString());
                Gson gson = new Gson();
                userDetail = gson.fromJson(response.toString(),UserDetail.class);
                Log.e(TAG, "User Detail"+ userDetail);
                mListener.onSuccess(userDetail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error: "+ error.getMessage());
                mListener.onFailure(error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(baseRequest);

    }



}
