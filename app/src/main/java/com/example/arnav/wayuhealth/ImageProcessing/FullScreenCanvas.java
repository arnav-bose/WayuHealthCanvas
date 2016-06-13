package com.example.arnav.wayuhealth.ImageProcessing;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.arnav.wayuhealth.R;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FullScreenCanvas extends AppCompatActivity {

    ImageView imageViewCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_canvas);

        imageViewCanvas = (ImageView)findViewById(R.id.imageViewCanvas);
        assert imageViewCanvas != null;

        if(TakePicture.optionSelected.equals("Camera")){
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), TakePicture.imageUriCamera);
                imageViewCanvas.setImageBitmap(TakePicture.displayImage(bitmap));
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), TakePicture.selectedImageUri);
                imageViewCanvas.setImageBitmap(TakePicture.displayImage(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
