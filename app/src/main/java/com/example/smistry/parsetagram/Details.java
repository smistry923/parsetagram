package com.example.smistry.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smistry.parsetagram.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class Details extends Fragment {
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public ImageView ivLikes;
    public ImageView ivComment;
    public ImageView ivShare;
    public TextView timeStamp;
    private List<Post> mComments;
    private ImageView close;
    private ImageView Share;
    private EditText etComment;
    private Button btSend;
    public RecyclerView comments;
    public ArrayList commentList;
    public commentAdapter commentAdapter;
    public ImageView ivProfilePic;
    public Post post;


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

        ivProfileImage = (ImageView) view.findViewById(R.id.ivPostImage);
        tvUsername = (TextView) view.findViewById(R.id.tvUserName);
        tvBody = (TextView) view.findViewById(R.id.tvBody);
        ivLikes = (ImageView) view.findViewById(R.id.ivLike);
        ivComment = (ImageView) view.findViewById(R.id.ivComment) ;
        ivShare = (ImageView) view.findViewById(R.id.ivShare);
        timeStamp = (TextView) view.findViewById(R.id.timeStamp);
        Share = (ImageView) view.findViewById(R.id.Share);
        close = (ImageView) view.findViewById(R.id.close);
        etComment = (EditText) view.findViewById(R.id.etComment);
        btSend = (Button) view.findViewById(R.id.btSend);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);

        commentList = new ArrayList();
        commentAdapter = new commentAdapter(commentList);

        comments = (RecyclerView) view.findViewById(R.id.rvComments);
        comments.setLayoutManager(new LinearLayoutManager(getContext()));
        comments.setAdapter(commentAdapter);
        etComment.setVisibility(View.INVISIBLE);
        btSend.setVisibility(View.INVISIBLE);


        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), bottomNav.class);
                startActivity(intent);
            }

        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etComment.setVisibility(View.VISIBLE);
                btSend.setVisibility(View.VISIBLE);
                comments.setVisibility(View.INVISIBLE);


            }
        });





        final Post.Query postQuery = new Post.Query().withUser();
        postQuery.getQuery(Post.class).getInBackground(id, new GetCallback<Post>(){

            @Override
            public void done(final Post object, ParseException e) {
                try {
                    tvUsername.setText(object.getUser().fetchIfNeeded().getUsername());
                    tvBody.setText(object.getDescription());
                    timeStamp.setText(object.getRelativeTimeAgo());
                    GlideApp.with(getContext()).load(object.getImage().getUrl()).into(ivProfileImage);
                    //GlideApp.with(getContext()).load(object.getUser().getParseFile("profilePic").getUrl()).circleCrop().into(ivProfilePic);
                    if(object.getUser().getParseFile("profilePic") != null) {
                        GlideApp.with(getContext()).load(object.getUser().getParseFile("profilePic").getUrl()).circleCrop().into(ivProfilePic);
                    }



                    btSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String comment = etComment.getText().toString();
                            comment = ParseUser.getCurrentUser().getObjectId() + " " + comment;
                            object.add("comments", comment);
                            object.saveInBackground();
                            etComment.getText().clear();

                            etComment.setVisibility(View.INVISIBLE);
                            btSend.setVisibility(View.INVISIBLE);
                            comments.setVisibility(View.VISIBLE);

                            ((bottomNav)getContext()).showDetails(object);

                        }
                    });

                    commentList.clear();
                    commentList.addAll(object.getList("comments"));
                    commentAdapter.notifyDataSetChanged();




                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });

    }


}
