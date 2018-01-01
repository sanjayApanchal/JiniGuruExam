package com.example.sanjay.jinitaskapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanjay.jinitaskapp.constants.Constants;
import com.example.sanjay.jinitaskapp.interfaces.UserDetailCallback;
import com.example.sanjay.jinitaskapp.model.UserDetail;
import com.example.sanjay.jinitaskapp.request.UserAccount;

import butterknife.BindView;

public class UserDetailsActivity extends BaseActivity {
    private static final String TAG = UserDetailsActivity.class.getSimpleName() ;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCompany)
    TextView tvCompany;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvBio)
    TextView tvBio;
    @BindView(R.id.tvFollowers)
    TextView tvFollowers;
    @BindView(R.id.tvFollowing)
    TextView tvFollowing;
    @BindView(R.id.tvHireable)
    TextView tvHireable;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.btnBlog)
    Button btnBlog;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Constants.KEY_USERNAME);
        getUser(username);

    }

    @Override
    public int getContentResId() {
        return R.layout.activity_user_details;
    }

    @Override
    public boolean isUpEnabled() {
        return true;
    }

    @Override
    public String getAppTitle() {
        return "Details";
    }

    // getting single user details
    private void getUser(final String username){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Details");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        // calling REST API for single user
        UserAccount.getSingleUser(this, username, new UserDetailCallback() {
            @Override
            public void onSuccess(UserDetail userDetail) {


                Glide.with(UserDetailsActivity.this).load(userDetail.getAvatarUrl()).into(ivAvatar);

                if(userDetail.getName()!=null) {
                    tvName.setText(userDetail.getName());
                }
                if(userDetail.getEmail()!=null) {
                    tvEmail.setText(userDetail.getEmail().toString());
                }
                if (userDetail.getCompany()!=null) {
                    tvCompany.setText(userDetail.getCompany().toString());
                }
                if (userDetail.getBio()!=null) {
                    tvBio.setText(userDetail.getBio().toString());
                }
                if (userDetail.getFollowers()!=null) {
                    tvFollowers.setText(userDetail.getFollowers().toString());
                }
                if (userDetail.getFollowing()!=null) {
                    tvFollowing.setText(userDetail.getFollowing().toString());
                }
                if (userDetail.getHireable()!=null) {
                    tvHireable.setText(userDetail.getHireable().toString());
                }
                if (userDetail.getLocation()!=null) {
                    tvLocation.setText(userDetail.getLocation());
                }

                final Uri uri = Uri.parse(userDetail.getBlog());


                // Opening Blog URL in Chrome TAB --> Requires Chrome installed on device.
                CustomTabsIntent.Builder cBuilder = new CustomTabsIntent.Builder();
                cBuilder.setToolbarColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.colorPrimary));
                cBuilder.setSecondaryToolbarColor(ContextCompat.getColor(UserDetailsActivity.this, R.color.colorPrimaryDark));

                final CustomTabsIntent customTabsIntent = cBuilder.build();

                if (uri.isAbsolute()) {
                    btnBlog.setVisibility(View.VISIBLE);

                    btnBlog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                customTabsIntent.launchUrl(UserDetailsActivity.this, uri);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    btnBlog.setVisibility(View.GONE);
                }
                progressDialog.hide();
            }
            @Override
            public void onFailure(String msg) {
                Log.e(TAG, msg);
            }
        });
    }
}
