package com.example.smistry.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class commentAdapter extends RecyclerView.Adapter <commentAdapter.ViewHolder>{

    private List<Object> mComments;
    public Context context;
    public commentAdapter(List<Object> objects){mComments = objects;}
    public ParseUser user;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvCommentBody;



        public ViewHolder(View itemView){
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvCommentBody = (TextView) itemView.findViewById(R.id.tvBody);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String comment = (String) mComments.get(position);
        final String [] commentArray = comment.split(" ", 2);
        ParseQuery<ParseUser>commentParse = ParseUser.getQuery().whereEqualTo("objectId", commentArray[0]);
        commentParse.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for(int i = 0; i < objects.size(); i++){
                    user = objects.get(i);
                    GlideApp.with(context).load(user.getParseFile("profilePic").getUrl()).circleCrop().into(holder.ivProfileImage);
                    holder.tvCommentBody.setText("@"+user.getUsername() + ": " + commentArray[1]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }




}
