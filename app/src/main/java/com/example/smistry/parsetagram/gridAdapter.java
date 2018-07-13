package com.example.smistry.parsetagram;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.smistry.parsetagram.model.Post;

import java.util.List;

// Provide the underlying view for an individual list item.
public class gridAdapter extends RecyclerView.Adapter<gridAdapter.VH> {
    private Activity mContext;
    private List<Post> mPostGrid;

    public gridAdapter(Activity context, List<Post> postGrid) {
        mContext = context;
        if (postGrid == null) {
            throw new IllegalArgumentException("contacts must not be null");
        }
       mPostGrid = postGrid;
    }

    // Inflate the view based on the viewType provided.
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new VH(itemView, mContext);
    }

    // Display data at the specified position
    @Override
    public void onBindViewHolder(VH holder, int position) {

        final Post post = mPostGrid.get(position);
        Glide.with(mContext).load(post.getImage().getUrl()).into(holder.postGrid);
    }

    @Override
    public int getItemCount() {
        return mPostGrid.size();
    }

    // Provide a reference to the views for each contact item
    public class VH extends RecyclerView.ViewHolder {
        final ImageView postGrid;

        public VH(View itemView, final Context context) {
            super(itemView);
            postGrid = itemView.findViewById(R.id.postGrid);

        }


    }



}
