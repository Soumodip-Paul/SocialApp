package com.sp.SPBrowser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sp.SPBrowser.textHandler.model.PostComments;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
ArrayList<PostComments> postComments;
    public  CommentAdapter(ArrayList<PostComments> postComments){
        this.postComments=postComments;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        PostComments comments = postComments.get(position);
        holder.Username.setText(comments.user.UserName);
        holder.CreatedAt.setText(Utils.getTimeAgo(comments.createdAt));
        holder.text.setText(comments.text);
        Glide.with(holder.userImage.getContext()).load(comments.user.imageUri).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return postComments.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter,parent,false));
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView Username,text,CreatedAt;
        public ImageView userImage;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
           Username = itemView.findViewById(R.id.commentUserName);
           text =itemView.findViewById(R.id.commentText);
           CreatedAt = itemView.findViewById(R.id.commentTime);
           userImage = itemView.findViewById(R.id.commentUserImage);

        }
    }

}
