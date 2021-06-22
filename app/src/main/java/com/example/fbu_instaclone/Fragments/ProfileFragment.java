package com.example.fbu_instaclone.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.fbu_instaclone.Adapter.ProfileAdapter;
import com.example.fbu_instaclone.EndlessRecyclerViewScrollListener;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends HomeFragment {

    ImageView ivProfile;
    TextView tvHandle;
    TextView tvPosts;
    TextView tvFollowing;
    TextView tvFollowers;
    ProfileAdapter adapter;

    public ProfileFragment(Context context){
        super(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        tvPosts = view.findViewById(R.id.tvPosts);
        tvFollowers = view.findViewById(R.id.tvFollowerLabel);
        tvFollowing = view.findViewById(R.id.tvFollowingLabel);

        tvPosts.setVisibility(View.INVISIBLE);
        tvFollowers.setVisibility(View.INVISIBLE);
        tvFollowing.setVisibility(View.INVISIBLE);

        ivProfile = view.findViewById(R.id.ivProfile);
        Glide.with(context)
                .load(ParseUser.getCurrentUser().getParseFile(Post.KEY_PROFILE_PIC).getUrl())
                .circleCrop()
                .override(140, 140)
                .into(ivProfile);

        tvHandle = view.findViewById(R.id.tvHandle);
        tvHandle.setText("@" + ParseUser.getCurrentUser().getUsername());

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPost();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMorePosts(totalItemsCount);
            }
        };

        posts = new ArrayList<>();

        // Can I add a 3rd parameter here that will indicate if we want to inflate the Grid Layout view
        // or the linear layout view thats used for the timeline??
        // Example: adapter = new PostAdapter(context, posts, type)
        adapter = new ProfileAdapter(context, posts);

        rvPosts.setLayoutManager(gridLayoutManager);
        rvPosts.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        rvPosts.setAdapter(adapter);
        rvPosts.addOnScrollListener(scrollListener);
        lastPost = 0;
        queryPost();
    }

    @Override
    protected void queryPost() {
        showProgressBar();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.orderByDescending(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                    return;
                }
                else{
                    tvPosts.setVisibility(View.VISIBLE);
                    tvFollowers.setVisibility(View.VISIBLE);
                    tvFollowing.setVisibility(View.VISIBLE);
                    tvPosts.setText(String.format("%d Posts", objects.size()));
                    posts.clear();
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    hideProgressBar();
                }
            }
        });
    }
}