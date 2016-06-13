package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.appspot.wayuconnectdev.loginWC.LoginWC.FetchFileList;
import com.example.arnav.wayuhealth.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arnav on 08/06/2016.
 */
public class AsyncTaskGetUrls extends AsyncTask<Void, Void, String> {

    Context contextGetUrls;
    Activity activityGetUrls;
    String email, sessionKey, memberID;
    List<String> jsonPath;
    public static HashMap hashMap;

    AsyncTaskGetUrls(Context context, Bundle bundle){
        this.contextGetUrls = context;
        this.activityGetUrls = (Activity)context;
        this.email = bundle.getString("email", "");
        this.sessionKey = bundle.getString("session_key", "");
        this.memberID = bundle.getString("member_id");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        jsonPath = new ArrayList<String>();
        hashMap = new HashMap();

    }

    @Override
    protected String doInBackground(Void... params) {

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();

        try {
            FetchFileList fetchFileList = apiServiceHandle.fetchFileList();
            fetchFileList
                    .setEmail(email)
                    .setSessionKey(sessionKey)
                    .setMemId(memberID);

            com.google.api.client.http.HttpResponse httpResponse = fetchFileList.executeUnparsed();
            String response = httpResponse.parseAsString();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObjectParent = new JSONObject(result);
            JSONArray jsonArrayParent = jsonObjectParent.getJSONArray("consultFiles");
            for(int i = 0; i < jsonArrayParent.length(); i++){
                JSONObject jsonObjectFinal = jsonArrayParent.getJSONObject(i);
                jsonPath.add(jsonObjectFinal.getString("serving_url"));
                hashMap.put(jsonObjectFinal.getString("serving_url"), "Not Started");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        multipleAsyncTasks();

    }

    public void multipleAsyncTasks(){
        AsyncTaskGetUploadImageOne asyncTaskImageUploadOne = new AsyncTaskGetUploadImageOne(contextGetUrls);
        asyncTaskImageUploadOne.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        AsyncTaskGetUploadImageTwo asyncTaskImageUploadTwo = new AsyncTaskGetUploadImageTwo(contextGetUrls);
//        asyncTaskImageUploadTwo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
