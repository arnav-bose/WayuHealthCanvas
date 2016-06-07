package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.appspot.wayuconnectdev.loginWC.LoginWC.PatUploadURL;
import com.example.arnav.wayuhealth.AppConstants;
import org.apache.http.HttpResponse;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Arnav on 06/06/2016.
 */
public class AsyncTaskGetUploadURL extends AsyncTask<Void, Void, String> {

    Context contextGetUploadURL;
    Activity activityGetUploadURL;
    String sessionKey, email, memberID;
    String uploadURL;
    ProgressDialog progressDialogGetUploadURL;

    AsyncTaskGetUploadURL(Context context, Bundle bundle) {
        contextGetUploadURL = context;
        activityGetUploadURL = (Activity) context;

        this.sessionKey = bundle.getString("session_key", "");
        this.email = bundle.getString("email", "");
        this.memberID = bundle.getString("mem_id", "");
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogGetUploadURL = new ProgressDialog(activityGetUploadURL);
        progressDialogGetUploadURL.setTitle("WayuHealth");
        progressDialogGetUploadURL.setMessage("Obtaining Image URL...");
        progressDialogGetUploadURL.setIndeterminate(false);
        progressDialogGetUploadURL.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();

        try {
            PatUploadURL uploadURL = apiServiceHandle.patUploadURL();
            uploadURL
                    .setSource("consult")
                    .setEmail(email)
                    .setSessionKey(sessionKey)
                    .setMemId(memberID);

            com.google.api.client.http.HttpResponse httpResponse = uploadURL.executeUnparsed();
            String response = httpResponse.parseAsString();
            Log.d("FALCON UPLOAD JSON : ", response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");

            if (success == 1) {
                //Start Multipart
                uploadURL = jsonObject.getString("uploadURL");
                Bitmap bitmap = TakePicture.bitmap;
                Uri imageUri = TakePicture.imageUri;
                try {
                    progressDialogGetUploadURL.dismiss();
                    AsyncTaskUploadImage asyncTaskUploadImage = new AsyncTaskUploadImage(contextGetUploadURL, bitmap, imageUri, uploadURL);
                    asyncTaskUploadImage.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(activityGetUploadURL, "Upload URL not Obtained", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
