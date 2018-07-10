package com.example.smistry.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smistry.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;


public class home extends Fragment {
    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/20180709_175418.jpg";
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        descriptionInput = view.findViewById(R.id.description);
        createButton = view.findViewById(R.id.create_button);
        refreshButton = view.findViewById(R.id.refresh_button);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, parseFile, user);

            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });


    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d("Home Activity", "Create Post Success!");

                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void loadTopPosts(){
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null){
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post" + i + "] = " + objects.get(i).getDescription()
                                + "\n username = " + objects.get(i).getUser().getUsername());
                    }

                } else {
                    e.printStackTrace();

                }

            }
        });
    }

}


