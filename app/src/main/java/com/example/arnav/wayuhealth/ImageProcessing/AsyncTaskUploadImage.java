package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import com.example.arnav.wayuhealth.AppData;
import com.example.arnav.wayuhealth.SQLiteDatabase.DatabaseHandler;
import com.example.arnav.wayuhealth.SQLiteDatabase.ImageUploadStructure;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Arnav on 07/06/2016.
 */
public class AsyncTaskUploadImage extends AsyncTask<Void, Void, String> {

    Context contextUploadImage;
    Activity activityUploadImage;
    Bitmap bitmap;
    Uri imageUri;
    String uploadURL;
    ProgressDialog progressDialogUploadImage;

    AsyncTaskUploadImage(Context context, Bitmap bitmap, Uri imageUri, String uploadURL){
        contextUploadImage = context;
        activityUploadImage = (Activity)context;
        this.bitmap = bitmap;
        this.imageUri = imageUri;
        this.uploadURL = uploadURL;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogUploadImage = new ProgressDialog(activityUploadImage);
        progressDialogUploadImage.setTitle("WayuHealth");
        progressDialogUploadImage.setMessage("Uploading Image...");
        progressDialogUploadImage.setIndeterminate(false);
        progressDialogUploadImage.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uploadURL);
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("file", new ByteArrayBody(data, "image/png", getFileName(contextUploadImage, imageUri)));
        httpPost.setEntity(multipartEntity);
        String result = null;
        try {
            result = EntityUtils.toString(httpClient.execute(httpPost).getEntity(), "UTF-8");
            Log.d("FALCON UPLOAD: ", result);
        } catch (IOException e) {
            Log.d("FALCON UPLOAD: ", result);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //Storing result in SQLite Database
        try {
            progressDialogUploadImage.setMessage("Image Uploaded. Storing Details to SQLite Database.");
            JSONObject jsonObject = new JSONObject(result);
            String blobKey = jsonObject.getString("blobKey");
            String servingURL = jsonObject.getString("servingUrl");

            AppData.sharedPreferences = contextUploadImage.getSharedPreferences(AppData.mySharedPreferences, Context.MODE_PRIVATE);
            String memberID = AppData.sharedPreferences.getString("mem_id", "");

            DatabaseHandler databaseHandler = new DatabaseHandler(contextUploadImage);
            databaseHandler.addContact(new ImageUploadStructure(memberID, blobKey, servingURL));

            progressDialogUploadImage.dismiss();

            //Executing saveFileListInfo AsyncTask
            //Getting the parameters
            String email = AppData.sharedPreferences.getString("email", "");
            String sessionKey = AppData.sharedPreferences.getString("session_key", "");
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("session_key", sessionKey);
            bundle.putString("member_id", memberID);
            bundle.putString("blob_key", blobKey);
            bundle.putString("serving_url", servingURL);

            AsyncTaskSaveFileListInfo asyncTaskSaveFileListInfo = new AsyncTaskSaveFileListInfo(activityUploadImage, bundle);
            asyncTaskSaveFileListInfo.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getFileName(Context context, Uri imageUri) {
        String fileName = "";
        if (null == imageUri)
            return null;

        final String scheme = imageUri.getScheme();
        if (scheme == null) {
            fileName = imageUri.getLastPathSegment();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            fileName = imageUri.getLastPathSegment();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            String[] proj = { MediaStore.Images.Media.TITLE };
            Cursor cursor = context.getContentResolver().query(imageUri, proj,
                    null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.v("fileName", fileName);
        return fileName;

    }
}
