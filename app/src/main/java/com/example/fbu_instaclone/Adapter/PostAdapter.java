package com.example.fbu_instaclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfile;
        ImageView ivPicture;
        TextView tvUserhandle;
        TextView tvDescription;
        ImageButton ibLike;
        ImageButton ibComment;
        ImageButton ibSend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUserhandle = itemView.findViewById(R.id.tvProfilename);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ibLike = itemView.findViewById(R.id.ibLike);
            ibComment = itemView.findViewById(R.id.ibComment);
            ibSend = itemView.findViewById(R.id.ibSend);
        }

        public void bind(Post post){
            tvDescription.setText(post.getDescription());
            tvUserhandle.setText(post.getUser().getUsername());
            Glide.with(context)
                    .load(post.getImage().getUrl())
                    .into(ivPicture);
        }
    }
}
