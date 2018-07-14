package com.example.smistry.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.smistry.parsetagram.model.Post;

import java.util.List;

public class parseAdapter extends RecyclerView.Adapter <parseAdapter.ViewHolder>{

    private List<Post> mPosts;
    public Context context;
    public parseAdapter(List<Post> posts){ mPosts = posts; }
    private ShareActionProvider mShareActionProvider;


    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View postView = inflater.inflate(R.layout.item_post, parent, false);
            ViewHolder viewHolder = new ViewHolder(postView);
            return viewHolder;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ImageView ivProfileImage;
            public TextView tvUsername;
            public TextView tvBody;
            public TextView timeStamp;
            public ImageView imageView;
            public ImageView imageView2;
            public ImageView imageView4;
            public ImageView Share;
            public ImageView profilePic;
            public ImageView ivBookmark;
            public ImageView ivMore;


        public ViewHolder(View itemView){
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            imageView = (ImageView) itemView.findViewById(R.id.ivLike);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2) ;
            imageView4 = (ImageView) itemView.findViewById(R.id.ivComment);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            Share = (ImageView) itemView.findViewById(R.id.Share);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            ivBookmark = (ImageView) itemView.findViewById(R.id.ivBookmark);
            ivMore = (ImageView) itemView.findViewById(R.id.ivMore);
            itemView.setOnClickListener(this);

        }


        public void onClick(View v) {
                // gets item position
                int position = getAdapterPosition();
                // make sure the position is valid, i.e. actually exists in the view
                Log.d("Location", "You clicked");
                if (position != RecyclerView.NO_POSITION) {
                    // get the movie at the position, this won't work if the class is static
                    Post post = mPosts.get(position);
                    Log.d("Location", "Got a tweet");
                    // create intent for the new activity
                    ((bottomNav)context).showDetails(post);
                }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //get data according to position
        final Post post = mPosts.get(position);
        holder.timeStamp.setText(post.getRelativeTimeAgo());
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvBody.setText(post.getDescription());
        GlideApp.with(context).load(post.getImage().getUrl()).into(holder.ivProfileImage);

        if(post.getUser().getParseFile("profilePic") != null) {
            GlideApp.with(context).load(post.getUser().getParseFile("profilePic").getUrl()).circleCrop().into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }


}
