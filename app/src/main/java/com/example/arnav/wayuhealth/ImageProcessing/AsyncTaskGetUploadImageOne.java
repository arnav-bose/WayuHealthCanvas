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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Arnav on 08/06/2016.
 */
public class AsyncTaskGetUploadImageOne extends AsyncTask<HashMap<String, String>, Void, Void> {

    Context contextGetUploadImage;
    Activity activityGetUploadImage;
    RecyclerView recyclerViewGetUploadImage;
    RecyclerView.Adapter adapterGetUploadImage;
    RecyclerView.LayoutManager layoutManagerGetUploadImage;
    ArrayList<DataSetImageUpload> arrayListGetImageUpload = new ArrayList<>();
    static ImageLoader imageLoader = null;

    AsyncTaskGetUploadImageOne(Context context){
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
    protected Void doInBackground(HashMap<String, String>... hashMaps) {

        try{
            Iterator i = AsyncTaskGetUrls.hashMap.entrySet().iterator();
            while(i.hasNext()){
                final Map.Entry entry = (Map.Entry)i.next();
                if(entry.getValue().equals("Not Started")){
                    imageLoader.loadImage(entry.getKey().toString(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            entry.setValue("Completed");
                            DataSetImageUpload dataSetImageUpload = new DataSetImageUpload(loadedImage);
                            arrayListGetImageUpload.add(dataSetImageUpload);
                            publishProgress();
                        }
                    });
                }
            }

        }catch (Exception e) {
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
