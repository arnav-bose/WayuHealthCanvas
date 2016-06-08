package com.example.arnav.wayuhealth.ImageProcessing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.appspot.wayuconnectdev.loginWC.LoginWC.SaveFileListInfo;
import com.example.arnav.wayuhealth.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arnav on 08/06/2016.
 */
public class AsyncTaskSaveFileListInfo extends AsyncTask<Void, Void, String> {

    Context contextSaveFileListInfo;
    Activity activitySaveFileListInfo;
    String email, sessionKey, memberID, blobKey, servingURL;
    ProgressDialog progressDialogSaveFileListInfo;

    AsyncTaskSaveFileListInfo(Context context, Bundle bundle){
        this.contextSaveFileListInfo = context;
        this.activitySaveFileListInfo = (Activity)context;

        this.email = bundle.getString("email", "");
        this.sessionKey = bundle.getString("session_key", "");
        this.memberID = bundle.getString("member_id");
        this.blobKey = bundle.getString("blob_key", "");
        this.servingURL = bundle.getString("serving_url", "");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialogSaveFileListInfo = new ProgressDialog(activitySaveFileListInfo);
        progressDialogSaveFileListInfo.setTitle("WayuHealth");
        progressDialogSaveFileListInfo.setMessage("Saving Details to Database...");
        progressDialogSaveFileListInfo.setIndeterminate(false);
        progressDialogSaveFileListInfo.show();
    }

    @Override
    protected String doInBackground(Void... params) {

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();

        try{
            SaveFileListInfo saveFileListInfo = apiServiceHandle.saveFileListInfo();
            saveFileListInfo
                    .setEmail(email)
                    .setSessionKey(sessionKey)
                    .setMemId(memberID)
                    .setBlobKey(blobKey)
                    .setServingUrl(servingURL);

            com.google.api.client.http.HttpResponse httpResponse = saveFileListInfo.executeUnparsed();
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
            JSONObject jsonObject = new JSONObject(result);
            int success = jsonObject.getInt("success");
            if(success == 1){
                progressDialogSaveFileListInfo.dismiss();
                Toast.makeText(activitySaveFileListInfo, "Image Successfully Uploaded", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(activitySaveFileListInfo, "Could not Save Details to Database.", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
