package com.example.fbu_instaclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_instaclone.App;
import com.example.fbu_instaclone.Fragments.CommentsFragment;
import com.example.fbu_instaclone.Fragments.DetailFragment;
import com.example.fbu_instaclone.Fragments.ProfileFragment;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Post;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;
    CommentsFragment.NewCommentListener listener;

    public PostAdapter(Context context, List<Post> posts, CommentsFragment.NewCommentListener listener){
        this.context = context;
        this.posts = posts;
        this.listener = listener;
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
        TextView tvTimestamp;
        TextView tvUserhandle;
        TextView tvDescription;
        TextView tvLikes;
        ImageButton ibLike;
        ImageButton ibComment;
        ImageButton ibSend;
        TextView tvComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUserhandle = itemView.findViewById(R.id.tvProfilename);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ibLike = itemView.findViewById(R.id.ibLike);
            ibComment = itemView.findViewById(R.id.ibComment);
            ibSend = itemView.findViewById(R.id.ibSend);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvComments = itemView.findViewById(R.id.commentsLabel);
        }

        public void bind(Post post){
            tvDescription.setText(post.getDescription());
            tvUserhandle.setText(post.getUser().getUsername());
            tvLikes.setText(String.format("%d Likes", post.getLikes()));

            Date date = post.getCreatedAt();
            String dateStr = Post.getRelativeTimeAgo(date.toString());
            tvTimestamp.setText(dateStr);

            if(post.getLikeStatus() == true){
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart_active);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.RED);
                ibLike.setImageDrawable(wrapped);
            }
            else {
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.BLACK);
                ibLike.setImageDrawable(wrapped);
            }

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(post.getLikeStatus() == true){
                        post.setLikeStatus(false);
                        post.setLikes(post.getLikes() - 1);
                        changeLikeButton(false, post);
                        App.changeLikeStatus(post, false);
                    }
                    else{
                        post.setLikeStatus(true);
                        post.setLikes(post.getLikes() + 1);
                        changeLikeButton(true, post);
                        App.changeLikeStatus(post, true);
                    }
                }
            });

            Glide.with(context)
                    .load(post.getImage().getUrl())
                    .override(410, 250)
                    .centerCrop()
                    .into(ivPicture);

            ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("post", (Parcelable) post);
                    context.startActivity(i);*/
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Fragment myFragment = DetailFragment.newInstance(post, context);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, myFragment).addToBackStack(null).commit();
                }
            });

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

            ibComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showComments(post);
                }
            });

            List<String> comments = post.getComments();
            if(comments != null){
                tvComments.setVisibility(View.VISIBLE);
                tvComments.setText(String.format("View all %d comments", comments.size()));
                tvComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showComments(post);
                    }
                });
            }else{
                tvComments.setVisibility(View.GONE);
            }
        }

        public void showComments(Post post){
            FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
            CommentsFragment fragment = CommentsFragment.newInstance(post, context);
            fragment.position = getAdapterPosition();
            fragment.newCommentListener = listener;
            fragment.show(fm, "Comments Fragment");
        }

        public void changeLikeButton (boolean likeStatus, Post post){
            if(likeStatus == true){
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart_active);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.RED);
                ibLike.setImageDrawable(wrapped);
                tvLikes.setText(String.format("%d Likes", post.getLikes()));
            }
            else {
                Drawable unwrapped = AppCompatResources.getDrawable(context, R.drawable.ufi_heart);
                Drawable wrapped = DrawableCompat.wrap(unwrapped);
                DrawableCompat.setTint(wrapped, Color.BLACK);
                ibLike.setImageDrawable(wrapped);
                tvLikes.setText(String.format("%d Likes", post.getLikes()));
            }
        }
    }
}
