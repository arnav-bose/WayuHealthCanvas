package com.example.arnav.wayuhealth.RecyclerViewImageUpload;

import android.graphics.Bitmap;

/**
 * Created by Arnav on 08/06/2016.
 */
public class DataSetImageUpload {

    Bitmap bitmapImageUpload;

    public DataSetImageUpload(Bitmap bitmapImageUpload){
        this.bitmapImageUpload = bitmapImageUpload;
    }

    public Bitmap getImageViewNotes() {
        return bitmapImageUpload;
    }

    public void setImageViewNotes(Bitmap bitmapImageUpload) {
        this.bitmapImageUpload = bitmapImageUpload;
    }

}
