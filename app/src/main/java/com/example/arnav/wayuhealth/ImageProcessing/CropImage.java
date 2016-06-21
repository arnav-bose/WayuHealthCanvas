package com.example.arnav.wayuhealth.ImageProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.arnav.wayuhealth.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class CropImage extends AppCompatActivity {

    CropImageView cropImageViewCanvas;
    Uri uriCropImage;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        cropImageViewCanvas = (CropImageView) findViewById(R.id.cropImageView);

        bundle = getIntent().getExtras();
        if(bundle == null){
            displayCropImage();
        }
        else{
            String uri = bundle.getString("uriAltered");
            uriCropImage = Uri.parse(uri);
            cropImageViewCanvas.setImageUriAsync(uriCropImage);
        }

        //Floating Action Button for Rotation
        ImageView imageViewFloatingActionButtonRotate = new ImageView(this);
        imageViewFloatingActionButtonRotate.setImageResource(R.drawable.ic_rotate_right_white_24dp);

        FloatingActionButton floatingActionButtonRotate = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFloatingActionButtonRotate)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .build();

        //Handling Floating Button OnClick
        floatingActionButtonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageViewCanvas.rotateImage(90);
            }
        });

        //Floating Action Button for Saving Image
        ImageView imageViewFloatingActionButtonSave = new ImageView(this);
        imageViewFloatingActionButtonSave.setImageResource(R.drawable.ic_save_white_24dp);

        FloatingActionButton floatingActionButtonSave = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFloatingActionButtonSave)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(6)
                .build();

        //Handling Floating Button OnClick
        floatingActionButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmapCropped = cropImageViewCanvas.getCroppedImage();
                Uri uriCroppedImage = getImageUri(CropImage.this, bitmapCropped);
                Bundle bundle = new Bundle();
                bundle.putString("uriCroppedImage", uriCroppedImage.toString());
                Intent i = new Intent(CropImage.this, FullScreenCanvas.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
    }

    public void displayCropImage() {
        if (TakePicture.bitmap!=null){
            cropImageViewCanvas.setImageBitmap(TakePicture.bitmap);
        } else
            finish();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Cropped Image", null);
        return Uri.parse(path);
    }
}
