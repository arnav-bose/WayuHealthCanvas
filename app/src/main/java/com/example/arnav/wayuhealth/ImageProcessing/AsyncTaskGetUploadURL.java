package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.appspot.wayuconnectdev.loginWC.LoginWC.PatUploadURL;
import com.example.arnav.wayuhealth.AppConstants;
import com.google.api.client.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Arnav on 06/06/2016.
 */
public class AsyncTaskGetUploadURL extends AsyncTask<Void, Void, String> {

    Context contextGetUploadURL;
    Activity activityGetUploadURL;
    String sessionKey, email, memberID;

    AsyncTaskGetUploadURL(Context context, Bundle bundle){
        contextGetUploadURL = context;
        activityGetUploadURL = (Activity)context;

        this.sessionKey = bundle.getString("session_key", "");
        this.email = bundle.getString("email", "");
        this.memberID = bundle.getString("mem_id", "");
    }


    @Override
    protected String doInBackground(Void... params) {

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();

        try{
            PatUploadURL uploadURL = apiServiceHandle.patUploadURL();
            uploadURL
                    .setSource("consult")
                    .setEmail(email)
                    .setSessionKey(sessionKey)
                    .setMemId(memberID);

            HttpResponse httpResponse = uploadURL.executeUnparsed();
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
        Toast.makeText(activityGetUploadURL, s, Toast.LENGTH_LONG).show();
    }
}
