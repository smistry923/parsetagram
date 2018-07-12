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
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class profile extends Fragment {
    Button logOutButton;
    Button takeProfilePic;
    File photoFile;
    public final String APP_TAG = "MyProfilePic";
    public final int REQUEST_IMAGE_CAPTURE = 1;
    ParseUser currentUser = ParseUser.getCurrentUser();
    public String photoFileName = "profile_pic.jpg";
    ImageView profilePic;
    Button upload;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        logOutButton = getView().findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        profilePic = view.findViewById(R.id.profilePic);
        upload = view.findViewById(R.id.upload);

        takeProfilePic = view.findViewById(R.id.takeProfilePic);
        takeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (intent.resolveActivity(profile.this.getActivity().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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
            profilePic.setImageBitmap(imageBitmap);

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ParseUser user = ParseUser.getCurrentUser();
                    final File file = getPhotoFileUri(photoFileName);
                    final ParseFile parseFile = new ParseFile(file);
                    setTakeProfilePic(parseFile);
                }
            });

        }
    }

    private void setTakeProfilePic(ParseFile imageFile) {
        currentUser = ParseUser.getCurrentUser();
        currentUser.put("profilePic", imageFile);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d("Home Activity", "Set Profile Pic");

                } else {
                    e.printStackTrace();
                }
            }
        });

        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d("imageFile", "imageFile saved!");

                } else {
                    e.printStackTrace();
                }
            }
        });

    }



}
