package com.example.smistry.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smistry.parsetagram.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.List;


public class Details extends Fragment {
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public ImageView imageView;
    public ImageView imageView2;
    public ImageView imageView4;
    public TextView timeStamp;
    private List<Post> mComments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String id = args.getString("Post");

        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) view.findViewById(R.id.tvUserName);
        tvBody = (TextView) view.findViewById(R.id.tvBody);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2) ;
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        timeStamp = (TextView) view.findViewById(R.id.timeStamp);

        Post.Query postQuery = new Post.Query().withUser();
        postQuery.getQuery(Post.class).getInBackground(id, new GetCallback<Post>(){

            @Override
            public void done(Post object, ParseException e) {
                try {
                    tvUsername.setText(object.getUser().fetchIfNeeded().getUsername());
                    tvBody.setText(object.getDescription());
                    timeStamp.setText(object.getRelativeTimeAgo());
                    GlideApp.with(getContext()).load(object.getImage().getUrl()).into(ivProfileImage);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });

    }


}
