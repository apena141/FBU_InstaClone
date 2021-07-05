package com.example.fbu_instaclone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_instaclone.R;
import com.example.fbu_instaclone.model.Comment;
import com.parse.ParseException;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    public static final String TAG = "CommentsAdapter";
    List<Comment> comments;
    Context context;

    public CommentsAdapter(Context context, List<Comment> comments){
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvComment;
        ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivProfile = itemView.findViewById(R.id.ivPic);
        }

        public void bind(Comment comment){
            tvComment.setText(comment.get(Comment.KEY_BODY).toString());
            String url = "";
            try {
                url = comment.getUser().fetchIfNeeded().getParseFile("profilePic").getUrl();
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
            Glide.with(context)
                    .load(url)
                    .circleCrop()
                    .override(55, 55)
                    .into(ivProfile);
        }
    }
}
