package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.arnav.wayuhealth.R;
import com.example.arnav.wayuhealth.RecyclerViewImageUpload.DataSetImageUpload;
import com.example.arnav.wayuhealth.RecyclerViewImageUpload.RecyclerViewImageUploadAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.nostra13.universalimageloader.core.ImageLoader.*;

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
    static ImageLoader imageLoader = null;

    AsyncTaskGetUploadImage(Context context){
        this.contextGetUploadImage = context;
        this.activityGetUploadImage = (Activity)context;

        recyclerViewGetUploadImage = (RecyclerView)activityGetUploadImage.findViewById(R.id.recyclerViewImageUpload);
        layoutManagerGetUploadImage = new LinearLayoutManager(contextGetUploadImage);
        recyclerViewGetUploadImage.setLayoutManager(layoutManagerGetUploadImage);
        recyclerViewGetUploadImage.setHasFixedSize(true);
        adapterGetUploadImage = new RecyclerViewImageUploadAdapter(arrayListGetImageUpload);
        recyclerViewGetUploadImage.setAdapter(adapterGetUploadImage);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activityGetUploadImage));
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    protected Void doInBackground(List<String>... path) {

        List<String> imageURL = path[0];

        try {
            final Iterator iteratorImageURL = imageURL.iterator();
            while(iteratorImageURL.hasNext()) {

                        imageLoader.loadImage(iteratorImageURL.next().toString(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                DataSetImageUpload dataSetImageUpload = new DataSetImageUpload(loadedImage);
                                arrayListGetImageUpload.add(dataSetImageUpload);
                                publishProgress();
                    }
                });
            }

//                Bitmap bitmap = lruCacheClass.getBitmapFromMemCache(iteratorImageURL.next().toString());
//                if(bitmap!=null){
//                    DataSetImageUpload dataSetImageUpload = new DataSetImageUpload(bitmap);
//                    arrayListGetImageUpload.add(dataSetImageUpload);
//                    publishProgress();
//                }else{
//                    // Download Image from URL
//                    InputStream input = new java.net.URL(iteratorImageURL.next().toString()).openStream();
//                    // Decode Bitmap
//                    bitmap = BitmapFactory.decodeStream(input);
//                    lruCacheClass.addBitmapToMemoryCache(iteratorImageURL.toString(), bitmap);
//                    DataSetImageUpload dataSetImageUpload = new DataSetImageUpload(bitmap);
//                    arrayListGetImageUpload.add(dataSetImageUpload);
//                    publishProgress()

        } catch (Exception e) {
            Log.d("FALCON CACHE: ", e.toString());
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
