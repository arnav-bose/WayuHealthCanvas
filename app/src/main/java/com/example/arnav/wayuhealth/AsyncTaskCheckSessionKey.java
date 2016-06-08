package com.example.arnav.wayuhealth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arnav on 02/06/2016.
 */
public class AsyncTaskCheckSessionKey extends AsyncTask <Void, Void, JSONObject> {

    Context contextCheckSessionKey;
    Activity activityCheckSessionKey;
    String session_key, email;

    AsyncTaskCheckSessionKey(Context contextCheckSessionKey, Bundle bundleCheckSessionKey){
        this.contextCheckSessionKey = contextCheckSessionKey;
        activityCheckSessionKey = (Activity)contextCheckSessionKey;
        this.session_key = bundleCheckSessionKey.getString("session_key");
        this.email = bundleCheckSessionKey.getString("email");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject jsonObject = null;
        try {
            LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();
            try {
                LoginWC.Authenticate authEnticate = apiServiceHandle.authenticate()
                        .setEmail(email)
                        .setSessionKey(session_key);
                HttpResponse httpResponse = authEnticate.executeUnparsed();
                String response = httpResponse.parseAsString();

                jsonObject = new JSONObject(response);
            }catch (HttpResponseException r){

            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        try {
            if(result.getInt("success") == 1 ){
                Intent i = new Intent(activityCheckSessionKey, Dashboard.class);
                activityCheckSessionKey.startActivity(i);
                activityCheckSessionKey.finish();
            }
            else{
                Thread threadSessionKeyExpire = new Thread(){
                    public void run(){
                        activityCheckSessionKey.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(contextCheckSessionKey, "Session Key Expired. Please Login Again.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(activityCheckSessionKey, Login.class);
                                activityCheckSessionKey.startActivity(i);
                                activityCheckSessionKey.finish();
                            }
                        });

                    }
                };
                threadSessionKeyExpire.start();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
