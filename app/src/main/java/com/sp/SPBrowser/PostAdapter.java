package com.sp.SPBrowser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.sp.SPBrowser.textHandler.model.Post;

import java.util.Objects;

public class PostAdapter extends FirestoreRecyclerAdapter<Post,PostAdapter.PostViewHolder> {

    IPostAdapter listener;
    IComment listener2;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options,IPostAdapter listener,IComment listener2) {
        super(options);
        this.listener = listener;
        this.listener2 = listener2;

    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {

        holder.postText.setText(model.text);
        holder.UserText.setText(model.createdBy.UserName);
        Glide.with(holder.userImage.getContext()).load(model.createdBy.imageUri).centerCrop().into(holder.userImage);
        holder.likeCount.setText(String.valueOf(model.likedBy.size()));/*this code should be debugged*/
        holder.createdAt.setText( Utils.getTimeAgo(model.createdAt));
        holder.comments.setText(String.valueOf(model.commentedBy.size()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        boolean isLiked = model.likedBy.contains(currentUserId);
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.getContext(), R.drawable.ic_liked));
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.getContext(), R.drawable.ic_outline_favorite_border_24));
        }

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostViewHolder viewHolder = new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
        viewHolder.likeButton.setOnClickListener(v -> listener.onLikeClicked(getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getId()));
        viewHolder.commentImage.setOnClickListener(v -> listener2.onCommentButtonClicked(getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getId()));

        return viewHolder;

    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postText,UserText,createdAt,likeCount,comments;
        public ImageView userImage,likeButton,commentImage;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postText =itemView.findViewById(R.id.postTitle);
            UserText =itemView.findViewById(R.id.userName);
            createdAt =itemView.findViewById(R.id.createdAt);
            likeCount =itemView.findViewById(R.id.likeCount);
            userImage =itemView.findViewById(R.id.userImage);
            likeButton =itemView.findViewById(R.id.likeButton);
            comments = itemView.findViewById(R.id.comments);
            commentImage = itemView.findViewById(R.id.commentImage);

        }
    }
}
 interface  IPostAdapter{
   void onLikeClicked(String postId);
}
interface  IComment{
    void onCommentButtonClicked(String postId);
}
