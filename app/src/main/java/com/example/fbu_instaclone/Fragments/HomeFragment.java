package com.example.fbu_instaclone.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbu_instaclone.Adapter.PostAdapter;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    Context context;
    List<Post> posts;
    RecyclerView rvPosts;
    PostAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        adapter = new PostAdapter(context, posts);

        rvPosts.setLayoutManager(new LinearLayoutManager(context));
        rvPosts.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvPosts.setAdapter(adapter);
        queryPost();
    }

    public void queryPost(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                    return;
                }
                else{
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}