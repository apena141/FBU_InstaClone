package com.example.fbu_instaclone.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailFragment extends Fragment {

    Context context;
    ImageView ivProfile;
    ImageView ivPic;
    TextView tvHandle;
    TextView tvLikes;
    TextView tvDescription;
    TextView tvTimestamp;
    Post post;

    public DetailFragment(Post post, Context context) {
        // Required empty public constructor
        this.post = post;
        this.context = context;
    }

    public static DetailFragment newInstance(Post post, Context context) {
        DetailFragment fragment = new DetailFragment(post, context);
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
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivPic = view.findViewById(R.id.ivPicture);
        tvHandle = view.findViewById(R.id.tvProfilename);
        tvLikes = view.findViewById(R.id.tvLikes);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTimestamp = view.findViewById(R.id.tvTimestamp);

        tvDescription.setText(post.getDescription());
        tvHandle.setText(post.getUser().getUsername());
        tvLikes.setText(String.format("%d Likes", post.getLikes()));

        Date date = post.getCreatedAt();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        String dateStr = format.format(date);
        tvTimestamp.setText(dateStr);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .centerCrop()
                .into(ivPic);

        Glide.with(context)
                .load(post.getUserProfilePic().getUrl())
                .circleCrop()
                .into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment myFragment = new ProfileFragment(context, post.getUser());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, myFragment).addToBackStack(null).commit();
            }
        });
    }
}