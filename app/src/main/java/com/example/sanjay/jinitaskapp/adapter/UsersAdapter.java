package com.example.sanjay.jinitaskapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanjay.jinitaskapp.R;
import com.example.sanjay.jinitaskapp.UserDetailsActivity;
import com.example.sanjay.jinitaskapp.constants.Constants;
import com.example.sanjay.jinitaskapp.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<User> mUserList = new ArrayList<>();
    Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;

    public UsersAdapter(List<User> mUserList, Context mContext) {
        this.mUserList = mUserList;
        this.mContext = mContext;
    }
    public UsersAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case ITEM:
                viewHolder = getViewHolder(parent,inflater);
                break;
            case LOADING:
                View v2 =inflater.inflate(R.layout.item_process,parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater){
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.user_row_item,parent,false);
        viewHolder = new ContentVH(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final User user = mUserList.get(position);
        switch (getItemViewType(position)){
            case ITEM:
                ContentVH holder = (ContentVH) viewHolder;


                holder.tvName.setText(user.getLogin());
                holder.tvType.setText(user.getType());

                Log.d("adapter :","name :"+user.getLogin()+" ,"+"  type :"+user.getType());

                Glide.with(mContext).load(user.getAvatarUrl()).placeholder(R.drawable.profile_placeholder).into(holder.circleAvatar);


                holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra(Constants.KEY_USERNAME,user.getLogin());
                        mContext.startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra(Constants.KEY_USERNAME,user.getLogin());
                        mContext.startActivity(intent);
                    }
                });

                break;
            case LOADING:
                break;
        }



    }

    @Override
    public int getItemCount() {
        return mUserList!=null?mUserList.size():0;
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mUserList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    class ContentVH extends RecyclerView.ViewHolder{
            @BindView(R.id.circleAvatarView)
            public CircleImageView circleAvatar;
            @BindView(R.id.tvUserName)
            public TextView tvName;
            @BindView(R.id.tvType)
            public TextView tvType;
            @BindView(R.id.btnInfo)
            ImageButton btnInfo;

        public ContentVH(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class LoadingVH extends RecyclerView.ViewHolder{

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    // Helper Methods

    public void add(User user) {
        mUserList.add(user);
        notifyItemInserted(mUserList.size() - 1);
    }

    public void addAll(List<User> users) {
        for (User user : users) {
            add(user);
        }
    }

    public void remove(User user) {
        int position = mUserList.indexOf(user);
        if (position > -1) {
            mUserList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new User());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mUserList.size() - 1;
        User item = getItem(position);

        if (item != null) {
            mUserList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public User getItem(int position) {
        return mUserList.get(position);
    }



}
