package com.example.sanjay.jinitaskapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sanjay.jinitaskapp.adapter.UsersAdapter;
import com.example.sanjay.jinitaskapp.constants.Constants;
import com.example.sanjay.jinitaskapp.interfaces.UserCallback;
import com.example.sanjay.jinitaskapp.listeners.PaginationScrollListeners;
import com.example.sanjay.jinitaskapp.model.User;
import com.example.sanjay.jinitaskapp.request.UserAccount;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerViewUsers)
    RecyclerView mRecyclerViewUsers;
    LinearLayoutManager layoutManager;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tvError)
    TextView tvError;

    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    UsersAdapter adapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    private int mSince = 0;

    public static final String KEY_TASK_ALL = "taskalluser";
    public static HashMap<String, String> taskMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRecyclerView();

        taskMap = new HashMap<>();
        setPaginationScrollListeners();

        //checking internet connection
            if (AppController.getInstance().isDataConnected()) {
            hideError();

            // mocking network delay for API call
            if (taskMap.get(KEY_TASK_ALL) != Constants.KEY_GET_ALLUSER_TAG) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        taskMap.put(KEY_TASK_ALL, Constants.KEY_GET_ALLUSER_TAG);
                        loadFirstPage();
                    }
                }, 1000);
            }
        }else {
            showError("Uh Oh! No Internet Connection.");
            Log.d("AppController", "No Internet Connection");
        }
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewUsers.setLayoutManager(layoutManager);
        mRecyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        adapter = new UsersAdapter(this);
        mRecyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
    }

    // set custom onScrollListener to RecyclerView in separate method
    private void setPaginationScrollListeners() {
        mRecyclerViewUsers.addOnScrollListener(new PaginationScrollListeners(layoutManager, this) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                if (AppController.getInstance().isDataConnected()) {

                    if (taskMap.get(KEY_TASK_ALL) != Constants.KEY_GET_ALLUSER_TAG) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                taskMap.put(KEY_TASK_ALL, Constants.KEY_GET_ALLUSER_TAG);
                                loadNextPage();
                            }
                        }, 1000);

                    } else {
                        Log.d("AppController", "No Internet Connection");
                    }
                }
            }
        });

        if(swipeRefreshLayout!=null) {

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (AppController.getInstance().isDataConnected()) {
                        setContentView(R.layout.activity_main);
                    } else {
                        setContentView(R.layout.internet_conn_err);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getContentResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isUpEnabled() {
        return true;
    }

    @Override
    public String getAppTitle() {
        return "Users";
    }

    // loads first page data.
    private void loadFirstPage() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "loadFirstPage: ");
        UserAccount.getAllUsers(this, mSince, new UserCallback() {
            @Override
            public void onSuccess(List<User> userList) {
                adapter = new UsersAdapter(userList, MainActivity.this);
                mRecyclerViewUsers.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                mSince = userList.size();

                if (taskMap.get(KEY_TASK_ALL) == Constants.KEY_GET_ALLUSER_TAG) {
                    taskMap.remove(KEY_TASK_ALL);
                }

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                hideError();
            }

            @Override
            public void onFailure(String msg) {
                progressBar.setVisibility(View.GONE);
                showError(msg);
            }
        });

    }

    // loads next pages --> after first page

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: ");

        UserAccount.getAllUsers(this, mSince, new UserCallback() {
            @Override
            public void onSuccess(List<User> userList) {

                adapter.removeLoadingFooter();
                isLoading = false;
                adapter.addAll(userList);
                mSince += userList.size();

                if (taskMap.get(KEY_TASK_ALL) != null && taskMap.get(KEY_TASK_ALL) == Constants.KEY_GET_ALLUSER_TAG) {
                    taskMap.remove(KEY_TASK_ALL);
                }
                hideError();

                Log.d(TAG, "adapterItemCount :" + adapter.getItemCount());

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(String msg) {
                showError(msg);
            }
        });
    }
    private void showError(String msg){
        tvError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mRecyclerViewUsers.setVisibility(View.GONE);
        tvError.setText(msg);
    }
    private void hideError(){
        tvError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mRecyclerViewUsers.setVisibility(View.VISIBLE);
    }


}
