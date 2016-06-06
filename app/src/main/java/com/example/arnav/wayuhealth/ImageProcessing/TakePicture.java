package com.example.arnav.wayuhealth.ImageProcessing;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakePicture extends Fragment {

    private static final int TAKE_PICTURE = 1;
    private static final int REQUEST_CODE = 1;
    private Uri imageUri;

    Button buttonTakePicture;
    Button buttonUploadImage;
    ImageView imageViewPicture;


    public TakePicture() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_take_picture, container, false);

        buttonTakePicture = (Button)view.findViewById(R.id.buttonTakePicture);
        buttonUploadImage = (Button)view.findViewById(R.id.buttonUploadImage);
        imageViewPicture = (ImageView)view.findViewById(R.id.imageViewPicture);

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
                            }
                                break;
                            case 1: //galleryIntent(view);
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


        // Inflate the layout for this fragment
        return view;
    }

    public void cameraIntent(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContext().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContext().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        imageViewPicture.setImageBitmap(displayImage(bitmap));
                        Toast.makeText(getActivity(), selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    public Bitmap displayImage(Bitmap bitmap){
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
