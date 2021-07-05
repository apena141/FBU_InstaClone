package com.example.fbu_instaclone.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.fbu_instaclone.Adapter.CommentsAdapter;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Comment;
import com.example.fbu_instaclone.model.Post;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends DialogFragment {

    public interface NewCommentListener {
        void onNewComment(int position, Post post);
    }

    public NewCommentListener newCommentListener;
    public int position;
    public static final String TAG = "CommentsFragment";
    RecyclerView rvComments;
    CommentsAdapter adapter;
    Context context;
    Post post;
    List<Comment> comments;
    ImageView ivProfilePic;
    Button btPost;
    EditText etComment;

    public CommentsFragment() {
        // Required empty public constructor
    }

    public static CommentsFragment newInstance(Post post, Context context){
        CommentsFragment fragment = new CommentsFragment();
        fragment.context = context;
        fragment.post = post;
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvComments = view.findViewById(R.id.rvComments);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        btPost = view.findViewById(R.id.btPost);
        etComment = view.findViewById(R.id.etComment);

        rvComments.setLayoutManager(new LinearLayoutManager(context));
        rvComments.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        comments = post.comments;
        if(comments == null){
            comments = new ArrayList<>();
        }

        adapter = new CommentsAdapter(context, comments);
        rvComments.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Glide.with(context)
                .load(ParseUser.getCurrentUser().getParseFile(Post.KEY_PROFILE_PIC).getUrl())
                .transform(new RoundedCorners(10))
                .circleCrop()
                .override(50, 50)
                .into(ivProfilePic);

        // Click Listeners
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etComment.getText())){
                   etComment.setError("Cannot be empty");
                   return;
                }else{
                    String newComment = etComment.getText().toString();
                    Comment entity = new Comment();
                    entity.setBody(newComment);
                    entity.setPost(post);
                    entity.setUser(ParseUser.getCurrentUser());
                    addComment(entity);
                    comments.add(0, entity);
                    post.comments = comments;
                    etComment.setText("");
                    adapter.notifyItemInserted(0);
                }
            }
        });

        // END of Click Listeners
    }

    public void addComment(ParseObject comment){
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d(TAG, "Saved comment");
                }else{
                    Log.e(TAG, "Error saving comment");
                }
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        newCommentListener.onNewComment(position, post);
        super.onDestroyView();
    }
}