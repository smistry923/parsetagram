package com.example.smistry.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.smistry.parsetagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;


public class dashboard extends Fragment {
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;
    Button cameraButton;
    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/20180709_175418.jpg";
    public final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    EditText etDescription;
    Button postButton;
    ImageView ivCam;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        mImageView = view.findViewById(R.id.mImageView);
        postButton = view.findViewById(R.id.postButton);
        etDescription = view.findViewById(R.id.etDescription);
        ivCam = view.findViewById(R.id.ivCam);

        mImageView.setVisibility(View.INVISIBLE);
        postButton.setVisibility(View.INVISIBLE);
        etDescription.setVisibility(View.INVISIBLE);


        cameraButton = view.findViewById(R.id.cameraButton);
         cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivCam.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Create a File reference to access to future access
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.example.smistry.parsetagram", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);


                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(dashboard.this.getActivity().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }

         });


    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.put("comments", new ArrayList<String>());

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


    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            mImageView.setImageBitmap(imageBitmap);

            mImageView.setVisibility(View.VISIBLE);
            cameraButton.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            postButton.setVisibility(View.VISIBLE);
            etDescription.setVisibility(View.VISIBLE);

            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String description = etDescription.getText().toString();
                    final ParseUser user = ParseUser.getCurrentUser();

                    final File file = getPhotoFileUri(photoFileName);
                    final ParseFile parseFile = new ParseFile(file);

                    createPost(description, parseFile, user);

                    mImageView.setVisibility(View.INVISIBLE);
                    postButton.setVisibility(View.INVISIBLE);
                    etDescription.setVisibility(View.INVISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);

                }
            });

        }
    }


}
