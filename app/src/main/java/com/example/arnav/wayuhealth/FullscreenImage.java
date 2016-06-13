package com.example.arnav.wayuhealth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class FullscreenImage extends AppCompatActivity {

    ImageView imageViewFullscreen;
    Bitmap bitmap;
    private boolean zoomOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        imageViewFullscreen = (ImageView) findViewById(R.id.imageViewFullscreen);
        Intent intent = getIntent();
        bitmap = (Bitmap) intent.getParcelableExtra("bitmap");
        imageViewFullscreen.setImageBitmap(bitmap);

        imageViewFullscreen.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageViewFullscreen.setScaleType(ImageView.ScaleType.FIT_XY);
        zoomOut = true;

        imageViewFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zoomOut) {
                    imageViewFullscreen.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageViewFullscreen.setAdjustViewBounds(true);
                    zoomOut = false;
                } else {
                    imageViewFullscreen.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageViewFullscreen.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut = true;
                }
            }
        });

    }
}

