package com.example.arnav.wayuhealth.ImageProcessing;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arnav.wayuhealth.AppData;
import com.example.arnav.wayuhealth.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakePicture extends Fragment {

    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_CODE = 1;
    public static Uri imageUriCamera;
    public static Bitmap bitmap;
    public static String optionSelected;
    public static Uri selectedImageUri;
    public static Uri imageUriGallery;

    Button buttonTakePicture;
    Button buttonUploadImage;
    //SimpleDraweeView simpleDraweeViewTakePhoto;
    ImageView imageViewTakePhoto;

    public TakePicture() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_take_picture, container, false);

        buttonTakePicture = (Button)view.findViewById(R.id.buttonTakePicture);
        buttonUploadImage = (Button)view.findViewById(R.id.buttonUploadImage);
        //simpleDraweeViewTakePhoto = (SimpleDraweeView)view.findViewById(R.id.simpleDraweeViewTakePhoto);
        imageViewTakePhoto = (ImageView)view.findViewById(R.id.imageViewTakePhoto);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask for Permission
                OnRequestPermissions();

                AlertDialog.Builder builerPictureMode = new AlertDialog.Builder(getActivity());
                String[] types = {"Camera", "Gallery"};
                builerPictureMode.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which){
                            case 0: {
                                cameraIntent(view);
                                optionSelected = "Camera";
                            }
                                break;
                            case 1: {
                                galleryIntent(view);
                                optionSelected = "Gallery";
                            }
                                break;
                        }
                    }
                });

                builerPictureMode.show();
            }
        });

        //Handling Upload Button
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.sharedPreferences = getContext().getSharedPreferences(AppData.mySharedPreferences, Context.MODE_PRIVATE);
                Bundle bundleGetUploadURL = new Bundle();
                bundleGetUploadURL.putString("mem_id", AppData.sharedPreferences.getString("mem_id", ""));
                bundleGetUploadURL.putString("email", AppData.sharedPreferences.getString("email", ""));
                bundleGetUploadURL.putString("session_key", AppData.sharedPreferences.getString("session_key", ""));
                AsyncTaskGetUploadURL asyncTaskGetUploadURL = new AsyncTaskGetUploadURL(getActivity(), bundleGetUploadURL);
                asyncTaskGetUploadURL.execute();
            }
        });

        imageViewTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FullScreenCanvas.class);
                startActivity(i);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void cameraIntent(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUriCamera = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    public void galleryIntent(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(optionSelected.equals("Camera")){
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = imageUriCamera;
                        //simpleDraweeViewTakePhoto.setImageURI(selectedImage);
                        getContext().getContentResolver().notifyChange(selectedImage, null);
                        ContentResolver cr = getContext().getContentResolver();
                        try {
                            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                            imageViewTakePhoto.setImageBitmap(displayImage(bitmap));
                            //simpleDraweeViewTakePhoto.setImageBitmap(displayImage(bitmap));
                            Toast.makeText(getActivity(), selectedImage.toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
                            Log.e("Camera", e.toString());
                        }
                    }
            }
        }
        else{
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == TAKE_PICTURE) {
                    selectedImageUri = data.getData();
                    String selectedImagePath = getPath(selectedImageUri);
                    imageUriGallery = Uri.parse(selectedImagePath);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                        imageViewTakePhoto.setImageBitmap(displayImage(bitmap));
                        //simpleDraweeViewTakePhoto.setImageBitmap(displayImage(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void OnRequestPermissions(){
        List<String> permissionsList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.CAMERA);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionsList.size() > 0){
            ActivityCompat.requestPermissions(getActivity(), permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Access Permision Denied", Toast.LENGTH_SHORT).show(); //Permission Denied
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static Bitmap displayImage(Bitmap bitmap){
        Bitmap bitmapScaled;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if(height >= 4096 || width >= 4096){
            int scaledDownHeight = (int)(height * (1024.0 / width));
            bitmapScaled = Bitmap.createScaledBitmap(bitmap, 1024, scaledDownHeight, true);
        }
        else
            return bitmap;
        return bitmapScaled;
    }

}
