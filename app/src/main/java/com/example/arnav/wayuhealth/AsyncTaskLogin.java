package com.example.arnav.wayuhealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.wayuconnectdev.loginWC.LoginWC;
import com.appspot.wayuconnectdev.loginWC.LoginWC.Patsignin;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Arnav on 01/06/2016.
 */
public class AsyncTaskLogin extends AsyncTask<String, Void, JSONObject> {

    private static final String KEY_SUCCESS = "success";
    Context contextLogin;
    Activity activityLogin;
    ProgressDialog progressDialogLogin;
    String username, password;
    SharedPreferences sharedPreferences;

    AsyncTaskLogin(Context context){
        this.contextLogin = context;
        activityLogin = (Activity)context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogLogin = new ProgressDialog(activityLogin);
        progressDialogLogin.setTitle("WayuHealth");
        progressDialogLogin.setMessage("Logging In...");
        progressDialogLogin.setIndeterminate(false);
        progressDialogLogin.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        username = params[0];
        password = params[1];

        LoginWC apiServiceHandle = AppConstants.getApiServiceHandle();

        try{
            Patsignin signin = apiServiceHandle.patsignin();
            signin
                    .setEmail(username)
                    .setPassword(password)
                    .setDeviceRegid("")
                    .setDeviceType("android");

            HttpResponse httpResponse = signin.executeUnparsed();
            String response = httpResponse.parseAsString();
            Log.d("FALCON: JSON RESPONSE: ", response);

            //JSON Parsing
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {
            if (jsonObject != null) {
                // loginErrorMsg.setText("");
                String res = jsonObject.getString(KEY_SUCCESS);

                if (Integer.parseInt(res) == 1) {
                    AppData.sharedPreferences = contextLogin.getSharedPreferences(AppData.mySharedPreferences, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppData.sharedPreferences.edit();
                    editor.putString("email", username);
                    String sessionKey = jsonObject.getString("session_key");

                    JSONArray jsonArray = jsonObject.optJSONArray("members");
                    JSONObject jsonObjectMembers = jsonArray.getJSONObject(0);
                    String memberID = jsonObjectMembers.optString("mem_id").toString();
                    editor.putString("session_key", sessionKey);
                    editor.putString("mem_id", memberID);
                    editor.commit();
                    progressDialogLogin.dismiss();
                    Intent i = new Intent(activityLogin, Dashboard.class);
                    activityLogin.startActivity(i);
                    activityLogin.finish();
                }
                else{
                    Toast.makeText(contextLogin, "Incorrect username and password", Toast.LENGTH_SHORT).show();
                    progressDialogLogin.dismiss();
                }
            }
            else{
                Toast.makeText(contextLogin, "Connection Error. Try again", Toast.LENGTH_SHORT).show();
                progressDialogLogin.dismiss();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        super.onPostExecute(jsonObject);
    }
}
