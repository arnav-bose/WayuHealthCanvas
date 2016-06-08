package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.arnav.wayuhealth.R;
import com.example.arnav.wayuhealth.RecyclerViewImageUpload.DataSetImageUpload;
import com.example.arnav.wayuhealth.RecyclerViewImageUpload.RecyclerViewImageUploadAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arnav on 08/06/2016.
 */
public class AsyncTaskGetUploadImage extends AsyncTask<List<String>, Void, Void> {

    Context contextGetUploadImage;
    Activity activityGetUploadImage;
    RecyclerView recyclerViewGetUploadImage;
    RecyclerView.Adapter adapterGetUploadImage;
    RecyclerView.LayoutManager layoutManagerGetUploadImage;
    ArrayList<DataSetImageUpload> arrayListGetImageUpload = new ArrayList<>();

    AsyncTaskGetUploadImage(Context context){
        this.contextGetUploadImage = context;
        this.activityGetUploadImage = (Activity)context;

        recyclerViewGetUploadImage = (RecyclerView)activityGetUploadImage.findViewById(R.id.recyclerViewImageUpload);
        layoutManagerGetUploadImage = new LinearLayoutManager(contextGetUploadImage);
        recyclerViewGetUploadImage.setLayoutManager(layoutManagerGetUploadImage);
        recyclerViewGetUploadImage.setHasFixedSize(true);
        adapterGetUploadImage = new RecyclerViewImageUploadAdapter(arrayListGetImageUpload);
        recyclerViewGetUploadImage.setAdapter(adapterGetUploadImage);

    }

    @Override
    protected Void doInBackground(List<String>... path) {

        List<String> imageURL = path[0];
        Bitmap bitmap = null;

        try {
            Iterator iteratorImageURL = imageURL.iterator();
            while(iteratorImageURL.hasNext()){
                // Download Image from URL
                InputStream input = new java.net.URL(iteratorImageURL.next().toString()).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                DataSetImageUpload dataSetImageUpload = new DataSetImageUpload(bitmap);
                arrayListGetImageUpload.add(dataSetImageUpload);
                publishProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        adapterGetUploadImage.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
