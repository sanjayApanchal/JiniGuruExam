package com.example.sanjay.jinitaskapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;



public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar toolbar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getAppTitle() != null) {

                    getSupportActionBar().setTitle(getAppTitle());
            }

            if (isUpEnabled()) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract int getContentResId();

    public abstract boolean isUpEnabled();

    public abstract String getAppTitle();
}
